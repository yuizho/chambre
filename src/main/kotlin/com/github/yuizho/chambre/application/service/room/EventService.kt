package com.github.yuizho.chambre.application.service.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.*
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class EventService(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val reactiveUnapprovedUserRepository: ReactiveUnapprovedUserRepository,
        private val reactiveEventStreamRepository: ReactiveEventStreamRepository,
        private val objectMapper: ObjectMapper
) {
    fun entry(roomId: String, userId: String, userName: String): Mono<*> {
        val roomId = Room.Id.from(roomId)
        val newUser = UnapprovedUser(
                userId,
                userName
        )
        // TODO: should implement password check?
        return reactiveRoomRepository.findRoomBy(roomId)
                .switchIfEmpty(Mono.error(BusinessException("invalid room id.")))
                .flatMap { room ->
                    reactiveUnapprovedUserRepository.contains(roomId, userId)
                            .map { joined -> Pair<Room, Boolean>(room, joined) }
                }
                .doOnNext { pair ->
                    if (pair.second) {
                        throw BusinessException("you have already joined this room.")
                    }
                    // TODO: should check same name user?
                }
                .flatMap { pair ->
                    reactiveUnapprovedUserRepository.put(roomId, newUser)
                            .map { pair.first }
                }
                .flatMap { room ->
                    reactiveEventStreamRepository.push(
                            roomId,
                            Message(
                                    setOf(room.adminUser()),
                                    EventType.ENTRY,
                                    // TODO: want to convert it in Repository
                                    objectMapper.writeValueAsString(newUser)
                            )
                    )
                }
    }
}