package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class JoinedEvent @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User>,
        override val payload: JoinedPayload
) : Event<JoinedPayload> {
    companion object {
        const val EVENT_TYPE = "JOIN"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

data class JoinedPayload @JsonCreator constructor(
        @param:JsonProperty("id")
        val joinedUserId: String,
        @param:JsonProperty("name")
        val joinedUserName: String
)