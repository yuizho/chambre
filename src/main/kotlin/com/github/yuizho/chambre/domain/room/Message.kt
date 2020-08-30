package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.databind.ObjectMapper

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
    ENTRY(UnapprovedUser::class.java)
}