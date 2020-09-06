package com.github.yuizho.chambre.domain.room

import reactor.core.publisher.Mono

interface EventPublisher {
    fun publish(event: Event<*>): Mono<Boolean>
}