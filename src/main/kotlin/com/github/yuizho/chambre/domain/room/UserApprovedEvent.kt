package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class UserApprovedEvent @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User.Id>,
        override val payload: UserApprovedPayload
) : Event<UserApprovedPayload> {
    companion object {
        const val EVENT_TYPE = "APPROVED"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

data class UserApprovedPayload @JsonCreator constructor(
        @param:JsonProperty("eventId")
        val eventId: String,
        @param:JsonProperty("token")
        val token: String
)