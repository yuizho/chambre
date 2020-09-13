package com.github.yuizho.chambre.application.service.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.application.service.room.dto.EventMessage
import com.github.yuizho.chambre.domain.room.EventSubscriber
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class EventSubscribeService(
        private val eventSubscriber: EventSubscriber,
        private val objectMapper: ObjectMapper
) {
    fun subscribe(roomId: Room.Id, userId: User.Id): Flux<EventMessage> {
        return eventSubscriber.subscribe(roomId)
                .filter {
                    it.to.any { id -> id == userId }
                }
                .map {
                    EventMessage(
                            it.getEventName(),
                            objectMapper.writeValueAsString(it.payload)
                    )
                }
                .log()
    }
}