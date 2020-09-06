package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Component

@Component
class EventFactory(
        val objectMapper: ObjectMapper
) {
    fun getEvent(id: String, to: Set<User>, eventType: String, payload: String): Event<*> {
        return when (eventType) {
            UserApproved.EVENT_TYPE -> {
                UserApproved(
                        UnapprovedEvent.Id.from(id),
                        to,
                        objectMapper.readValue(payload, UserApprovedPayload::class.java)
                )
            }
            Joined.EVENT_TYPE -> {
                Joined(
                        ApprovedEvent.Id.from(id),
                        to,
                        objectMapper.readValue(payload, JoinedPayload::class.java)
                )
            }
            else -> throw IllegalArgumentException("unexpected eventType.")
        }
    }
}