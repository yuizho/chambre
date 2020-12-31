package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.application.service.room.dto.CreatingRoomResult
import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@Service
class RoomService(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val participantRepository: ReactiveParticipantRepository
) {
    fun create(userName: String, roomName: String, password: String): Mono<CreatingRoomResult> {
        val userId = User.Id(UUID.randomUUID().toString())
        val authToken = UUID.randomUUID().toString()
        val room = Room(
                Room.Id.from(UUID.randomUUID().toString()),
                roomName,
                password,
                Room.Status.OPENED,
                mutableSetOf(
                        User(
                                userId,
                                userName,
                                Role.ADMIN
                        )
                )
        )

        return Flux.merge(
                reactiveRoomRepository.save(room),
                participantRepository
                        .save(Participant(
                                Participant.Id.from(authToken),
                                room.id.getIdIdWithSchemaPrefix(),
                                userId.value
                        ))
        ).then(Mono.just(CreatingRoomResult(authToken, room)))
    }

    fun room(id: Room.Id): Mono<Room> =
            reactiveRoomRepository.findRoomBy(id)
                    .switchIfEmpty(Mono.error(BusinessException("invalid room id.")))

    fun users(id: Room.Id): Mono<List<User>> {
        return reactiveRoomRepository.findRoomBy(id)
                .switchIfEmpty(Mono.error(BusinessException("invalid room id.")))
                .map { room ->
                    room.users.toList().sortedBy { it.name }
                }
    }
}