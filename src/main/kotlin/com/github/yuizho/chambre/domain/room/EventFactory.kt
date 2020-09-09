package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class EventFactory(
        val objectMapper: ObjectMapper
) {
    fun getEvent(id: Event.Id, to: Set<User>, eventType: String, payload: String): Event<*> {
        return when (eventType) {
            Applied.EVENT_TYPE -> {
                Applied(
                        id,
                        to,
                        objectMapper.readValue(payload, AppliedPayload::class.java)
                )
            }
            UserApproved.EVENT_TYPE -> {
                UserApproved(
                        id,
                        to,
                        objectMapper.readValue(payload, UserApprovedPayload::class.java)
                )
            }
            Joined.EVENT_TYPE -> {
                Joined(
                        id,
                        to,
                        objectMapper.readValue(payload, JoinedPayload::class.java)
                )
            }
            else -> throw IllegalArgumentException("unexpected eventType.")
        }
    }
}