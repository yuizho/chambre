package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Applied @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User>,
        override val payload: AppliedPayload
) : Event<AppliedPayload> {
    companion object {
        // TODO: change event type
        const val EVENT_TYPE = "ENTRY"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

data class AppliedPayload @JsonCreator constructor(
        @param:JsonProperty("id")
        val appliedUserId: String,
        @param:JsonProperty("name")
        val appliedUserName: String
)