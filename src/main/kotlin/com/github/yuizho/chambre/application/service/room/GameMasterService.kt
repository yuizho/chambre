package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import com.github.yuizho.chambre.domain.room.*
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
    fun approve(user: User, roomId: Room.Id): Mono<String> {
        // TODO: check if the user is registered in unapproved
        return reactiveRoomRepository.findRoomBy(roomId).flatMap { room ->
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
                            user.id
                    ))
                    .map { token }
        }
    }

    fun reject(userId: String, roomId: Room.Id): Mono<Void> {
        // TODO: check if the user is registered in unapproved
        return Flux.merge(
                unapprovedUserRepository.remove(roomId, userId),
                eventPublisher.publish(RejectedEvent(
                        Event.Id.from(roomId.getIdIdWithSchemaPrefix()),
                        // TODO: just need userId
                        setOf(User(userId, "", Role.NORMAL))
                ))
        ).then()
    }
}