package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.room.*
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class UserService(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val reactiveUnapprovedUserRepository: ReactiveUnapprovedUserRepository,
        private val eventPublisher: EventPublisher
) {
    fun apply(roomId: String, roomKey: String, userName: String): Mono<String> {
        val roomId = Room.Id.from(roomId)
        val userId = UUID.randomUUID().toString()
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
                    reactiveUnapprovedUserRepository.findUnapprovedUsers(roomId)
                            .any { it.name == userName }
                            .map { joined -> Pair<Room, Boolean>(room, joined) }
                }
                .doOnNext { (_, duplicateUserName) ->
                    if (duplicateUserName) {
                        throw BusinessException("you have already joined this room.")
                    }
                }
                .flatMap { (room, _) ->
                    Flux.merge(
                            reactiveUnapprovedUserRepository.put(roomId, newUser),
                            newUser.apply(eventPublisher, roomId, setOf(room.adminUser()))
                    ).then(Mono.just(userId))
                }
    }
}