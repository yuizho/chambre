package com.github.yuizho.chambre.domain.user

import com.github.yuizho.chambre.domain.room.User
import reactor.core.publisher.Mono

interface ReactiveUserRepository {
    fun findUserBy(id: String): Mono<User>
    fun save(user: User): Mono<Boolean>
}