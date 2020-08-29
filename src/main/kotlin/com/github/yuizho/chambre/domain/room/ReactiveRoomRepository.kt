package com.github.yuizho.chambre.domain.room

import reactor.core.publisher.Mono

interface ReactiveRoomRepository {
    fun findRoomBy(id: Room.Id): Mono<Room>
    fun save(room: Room): Mono<Boolean>
}