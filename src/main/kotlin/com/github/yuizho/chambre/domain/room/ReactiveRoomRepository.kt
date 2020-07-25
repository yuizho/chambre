package com.github.yuizho.chambre.domain.room

import reactor.core.publisher.Mono

interface ReactiveRoomRepository {
    fun findUserBy(id: String): Mono<Room>
    fun save(room: Room): Mono<Boolean>
}