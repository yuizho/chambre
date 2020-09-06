package com.github.yuizho.chambre.domain.room

import reactor.core.publisher.Flux

interface EventSubscriber {
    fun subscribe(roomId: Room.Id): Flux<Event<*>>
}