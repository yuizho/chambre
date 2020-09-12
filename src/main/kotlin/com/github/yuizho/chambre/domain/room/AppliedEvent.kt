package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class AppliedEvent @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User.Id>,
        override val payload: AppliedPayload
) : Event<AppliedPayload> {
    companion object {
        const val EVENT_TYPE = "APPLY"
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