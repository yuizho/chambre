package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.*
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.RecordId
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class ReactiveUnapprovedEventStreamRepositoryRedis(
        private val reactiveRedisOperations: ReactiveRedisOperations<String, Message>,
        private val streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>,
        private val objectMapper: ObjectMapper
) : ReactiveUnapprovedEventStreamRepository {
    override fun push(roomId: Room.Id, message: Message): Mono<RecordId> {
        return reactiveRedisOperations.opsForStream<String, Message>()
                .add(MapRecord.create(
                        "unapproved-event:${roomId.getIdIdWithSchemaPrefix()}",
                        mapOf(
                                "to" to objectMapper.writeValueAsString(message.to),
                                "eventType" to message.eventType,
                                "payload" to message.payload
                        )
                ))
    }

    override fun receive(roomId: Room.Id): Flux<Message> {
        // TODO: shold be fromStart?
        return streamReceiver.receive(StreamOffset.latest("unapproved-event:${roomId.getIdIdWithSchemaPrefix()}"))
                .map {
                    // TODO: add error handling
                    Message(
                            objectMapper.readValue(
                                    it.value["to"]!!,
                                    object : TypeReference<Set<User>>() {}
                            ),
                            EventType.valueOf(it.value["eventType"]!!),
                            it.value["payload"]!!
                    )
                }
    }

}