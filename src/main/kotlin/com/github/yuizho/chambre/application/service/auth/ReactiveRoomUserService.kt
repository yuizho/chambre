package com.github.yuizho.chambre.application.service.auth

import com.github.yuizho.chambre.application.service.auth.dto.UserSession
import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Room
import reactor.core.publisher.Mono

interface ReactiveRoomUserService {
    fun retrieveUser(token: String): Mono<UserSession>
}

class ReactiveRoomUserServiceImpl(
        private val reactiveParticipantRepository: ReactiveParticipantRepository,
        private val reactiveRoomRepository: ReactiveRoomRepository
) : ReactiveRoomUserService {
    override fun retrieveUser(token: String): Mono<UserSession> {
        return reactiveParticipantRepository.findParticipantBy(Participant.Id.from(token))
                .flatMap { ac ->
                    reactiveRoomRepository.findRoomBy(Room.Id.fromIdWithSchemaPrefix(ac.roomId))
                            .map { r -> Pair(ac.userId, r) }
                }.map { (userId, room) ->
                    val user = room.users.find { u -> u.id.value == userId }
                            ?: throw IllegalStateException("there is no expected user.")
                    UserSession(room.id, user)
                }
    }
}