package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import com.github.yuizho.chambre.domain.room.*
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class GameMasterService(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val participantRepository: ReactiveParticipantRepository,
        private val unapprovedUserRepository: ReactiveUnapprovedUserRepository,
        private val eventPublisher: EventPublisher
) {
    fun approve(user: UnapprovedUser, roomId: Room.Id): Mono<String> {
        return unapprovedUserRepository.contains(roomId, user.id)
                .doOnNext { contained ->
                    if (!contained) {
                        throw BusinessException("the user you try to approve has not applied.")
                    }
                }
                .flatMap {
                    reactiveRoomRepository.findRoomBy(roomId).flatMap { room ->
                        // add user to the room
                        room.approve(eventPublisher, user)
                                .flatMap { token ->
                                    reactiveRoomRepository.save(room)
                                            .map { Pair(room, token) }
                                }
                    }.flatMap { (room, token) ->
                        // save auth info
                        participantRepository
                                .save(Participant(
                                        Participant.Id.from(token),
                                        room.id.getIdIdWithSchemaPrefix(),
                                        user.id.value
                                ))
                                .map { token }
                    }
                }
    }

    fun reject(userId: User.Id, roomId: Room.Id): Mono<Void> {
        return unapprovedUserRepository.contains(roomId, userId)
                .doOnNext { contained ->
                    if (!contained) {
                        throw BusinessException("the user you try to reject has not applied.")
                    }
                }
                .flatMap {
                    Flux.merge(
                            unapprovedUserRepository.remove(roomId, userId),
                            eventPublisher.publish(RejectedEvent(
                                    Event.Id.from(roomId.getIdIdWithSchemaPrefix()),
                                    setOf(userId)
                            ))
                    ).then()
                }
    }
}