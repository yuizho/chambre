package com.github.yuizho.chambre.domain.room

import reactor.core.publisher.Mono

interface ReactiveUnapprovedUserRepository {
    fun findUnapprovedUserBy(roomId: Room.Id, userId: String): Mono<UnapprovedUser>
    fun put(roomId: Room.Id, unapprovedUser: UnapprovedUser): Mono<Boolean>
}