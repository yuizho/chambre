package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.config.RedisProperties
import com.github.yuizho.chambre.domain.room.Event
import com.github.yuizho.chambre.domain.room.EventPublisher
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class EventPublisherImpl(
        private val reactiveRedisOperations: ReactiveRedisOperations<String, Event<*>>,
        private val redisProperties: RedisProperties,
        private val objectMapper: ObjectMapper
) : EventPublisher {
    override fun publish(event: Event<*>): Mono<Boolean> {
        return reactiveRedisOperations.opsForStream<String, Event<*>>()
                .add(MapRecord.create(
                        event.id.getIdIdWithSchemaPrefix(),
                        mapOf(
                                "to" to objectMapper.writeValueAsString(event.to),
                                "eventType" to event.getEventName(),
                                "payload" to objectMapper.writeValueAsString(event.payload)
                        )
                )).flatMap {
                    reactiveRedisOperations.expire(
                            event.id.getIdIdWithSchemaPrefix(),
                            Duration.ofSeconds(redisProperties.expireSec)
                    )
                }
    }
}