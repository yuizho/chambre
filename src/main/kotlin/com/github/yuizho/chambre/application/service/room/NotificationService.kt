package com.github.yuizho.chambre.application.service.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.EventSubscriber
import com.github.yuizho.chambre.domain.room.Room
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class NotificationService(
        private val eventSubscriber: EventSubscriber,
        private val objectMapper: ObjectMapper
) {
    fun notify(roomId: String, userId: String): Flux<Pair<String, String>> {
        return eventSubscriber.subscribe(Room.Id.from(roomId))
                .filter {
                    it.to.any { user -> user.id == userId }
                }
                .map {
                    Pair(it.getEventName(), objectMapper.writeValueAsString(it.payload))
                }
                .log()
    }
}