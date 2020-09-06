package com.github.yuizho.chambre.domain.room

import org.springframework.data.redis.connection.stream.RecordId
import reactor.core.publisher.Mono

interface EventPublisher {
    fun publish(event: Event<*>): Mono<RecordId>
}