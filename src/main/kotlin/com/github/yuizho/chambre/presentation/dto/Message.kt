package com.github.yuizho.chambre.presentation.dto

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.UnapprovedUser
import com.github.yuizho.chambre.domain.room.User

// TODO: should move to domain
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