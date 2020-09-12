package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import com.github.yuizho.chambre.domain.room.EventPublisher
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class GameMasterService(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val participantRepository: ReactiveParticipantRepository,
        private val eventPublisher: EventPublisher
) {
    fun approve(user: User, roomId: Room.Id): Mono<String> {
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
}