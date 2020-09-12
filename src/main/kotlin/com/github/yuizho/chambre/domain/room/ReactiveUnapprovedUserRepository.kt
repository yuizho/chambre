package com.github.yuizho.chambre.domain.room

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveUnapprovedUserRepository {
    fun findUnapprovedUserBy(roomId: Room.Id, userId: String): Mono<UnapprovedUser>
    fun findUnapprovedUsers(roomId: Room.Id): Flux<UnapprovedUser>
    fun contains(roomId: Room.Id, userId: String): Mono<Boolean>
    fun put(roomId: Room.Id, unapprovedUser: UnapprovedUser): Mono<Boolean>
    fun remove(roomId: Room.Id, userId: String): Mono<Boolean>
}