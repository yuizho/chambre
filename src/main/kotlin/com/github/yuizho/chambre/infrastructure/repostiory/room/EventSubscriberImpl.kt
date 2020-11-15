package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.*
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux

@Repository
class EventSubscriberImpl(
        private val streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>,
        private val eventFactory: EventFactory,
        private val objectMapper: ObjectMapper
) : EventSubscriber {

    override fun subscribe(roomId: Room.Id): Flux<Event<*>> {
        val id = Event.Id.from(roomId.getIdIdWithSchemaPrefix())
        return streamReceiver.receive(StreamOffset.fromStart(id.getIdIdWithSchemaPrefix()))
                .map {
                    eventFactory.getEvent(
                            id,
                            objectMapper.readValue(
                                    it.value["to"] ?: throw RuntimeException("[to] is null."),
                                    object : TypeReference<Set<User.Id>>() {}
                            ),
                            it.value["eventType"] ?: throw RuntimeException("[eventType] is null."),
                            it.value["payload"] ?: throw RuntimeException("[payload] is null.")
                    )
                }
    }

}