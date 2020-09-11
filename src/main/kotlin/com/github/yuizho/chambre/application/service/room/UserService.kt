package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.room.*
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserService(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val reactiveUnapprovedUserRepository: ReactiveUnapprovedUserRepository,
        private val eventPublisher: EventPublisher
) {
    fun entry(roomId: String, roomKey: String, userId: String, userName: String): Mono<*> {
        val roomId = Room.Id.from(roomId)
        val newUser = UnapprovedUser(
                userId,
                userName
        )
        return reactiveRoomRepository.findRoomBy(roomId)
                .switchIfEmpty(Mono.error(BusinessException("invalid room id.")))
                .doOnNext { room ->
                    if (room.key != roomKey) {
                        throw BusinessException("invalid password.")
                    }
                }
                .flatMap { room ->
                    reactiveUnapprovedUserRepository.contains(roomId, userId)
                            .map { joined -> Pair<Room, Boolean>(room, joined) }
                }
                .doOnNext { pair ->
                    // TODO: ココでuserNameの重複有無のみをチェック (なので検索はroomIdのみで検索する感じ)
                    if (pair.second) {
                        throw BusinessException("you have already joined this room.")
                    }
                }
                .flatMap { pair ->
                    // TODO: ココでuserIdをGenerateする
                    reactiveUnapprovedUserRepository.put(roomId, newUser)
                            .map { pair.first }
                }
                .flatMap { room ->
                    newUser.apply(eventPublisher, roomId, setOf(room.adminUser()))
                }
    }
}