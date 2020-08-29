package com.github.yuizho.chambre.application.service.security

import com.github.yuizho.chambre.domain.auth.ReactiveAuthContainerRepository
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import reactor.core.publisher.Mono

interface ReactiveRoomUserService {
    fun retrieveUser(token: String): Mono<User>
}

class ReactiveRoomUserServiceImpl(
        private val reactiveAuthContainerRepository: ReactiveAuthContainerRepository,
        private val reactiveRoomRepository: ReactiveRoomRepository
) : ReactiveRoomUserService {
    override fun retrieveUser(token: String): Mono<User> {
        return reactiveAuthContainerRepository.findAuthContainerBy(token)
                .flatMap { ac ->
                    reactiveRoomRepository.findRoomBy(Room.Id.fromIdWithSchemaPrefix(ac.roomId))
                            .map { r -> Pair(ac.userId, r) }
                }.map { p ->
                    p.second.users.find { u -> u.id == p.first }
                }
    }
}