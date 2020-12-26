package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AppliedEvent @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User.Id>,
        override val payload: AppliedPayload
) : Event<AppliedPayload> {
    companion object {
        const val EVENT_TYPE = "APPLIED"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

data class AppliedPayload @JsonCreator constructor(
        @param:JsonProperty("eventId")
        val eventId: String,
        @param:JsonProperty("userId")
        val appliedUserId: String,
        @param:JsonProperty("name")
        val appliedUserName: String
)