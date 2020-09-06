package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.Event
import com.github.yuizho.chambre.domain.room.EventPublisher
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.RecordId
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class EventPublisherImpl(
        private val reactiveRedisOperations: ReactiveRedisOperations<String, Event<*>>,
        private val objectMapper: ObjectMapper
) : EventPublisher {
    override fun publish(event: Event<*>): Mono<RecordId> {
        return reactiveRedisOperations.opsForStream<String, Event<*>>()
                .add(MapRecord.create(
                        event.id.getIdIdWithSchemaPrefix(),
                        mapOf(
                                "to" to objectMapper.writeValueAsString(event.to),
                                "eventType" to event.getEventName(),
                                "payload" to objectMapper.writeValueAsString(event.payload)
                        )
                ))
    }
}