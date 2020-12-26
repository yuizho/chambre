package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class RejectedEvent @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User.Id>,
        override val payload: RejectedPayload = RejectedPayload(UUID.randomUUID().toString())
) : Event<RejectedPayload> {
    companion object {
        const val EVENT_TYPE = "REJECTED"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

data class RejectedPayload @JsonCreator constructor(
        @param:JsonProperty("eventId")
        val eventId: String
)