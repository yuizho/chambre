package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class EventFactory(
        val objectMapper: ObjectMapper
) {
    fun getEvent(id: Event.Id, to: Set<User>, eventType: String, payload: String): Event<*> {
        return when (eventType) {
            AppliedEvent.EVENT_TYPE -> {
                AppliedEvent(
                        id,
                        to,
                        objectMapper.readValue(payload, AppliedPayload::class.java)
                )
            }
            UserApprovedEvent.EVENT_TYPE -> {
                UserApprovedEvent(
                        id,
                        to,
                        objectMapper.readValue(payload, UserApprovedPayload::class.java)
                )
            }
            JoinedEvent.EVENT_TYPE -> {
                JoinedEvent(
                        id,
                        to,
                        objectMapper.readValue(payload, JoinedPayload::class.java)
                )
            }
            else -> throw IllegalArgumentException("unexpected eventType.")
        }
    }
}