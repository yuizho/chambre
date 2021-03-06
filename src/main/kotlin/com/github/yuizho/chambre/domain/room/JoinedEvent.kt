package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class JoinedEvent @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User.Id>,
        override val payload: JoinedPayload
) : Event<JoinedPayload> {
    companion object {
        const val EVENT_TYPE = "JOINED"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

data class JoinedPayload @JsonCreator constructor(
        @param:JsonProperty("eventId")
        val eventId: String,
        @param:JsonProperty("userId")
        val joinedUserId: String,
        @param:JsonProperty("name")
        val joinedUserName: String
)