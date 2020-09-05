package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.auth.Participant

data class Message(
        val to: Set<User>,
        val eventType: EventType,
        val payload: String

) {
    fun <T> getPayloadObject(): T {
        return ObjectMapper().readValue(payload, eventType.payloadClass) as T
    }
}

enum class EventType(
        val payloadClass: Class<*>
) {
    // approved notify
    ENTRY(UnapprovedUser::class.java),
    JOIN(User::class.java),

    // Unapproved notify
    APPROVED(Participant::class.java),
    REJECTED(String::class.java)
}