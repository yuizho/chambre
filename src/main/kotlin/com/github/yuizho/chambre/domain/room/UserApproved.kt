package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class UserApproved @JsonCreator constructor(
        override val id: UnapprovedEvent.Id,
        override val to: Set<User>,
        override val payload: UserApprovedPayload
) : UnapprovedEvent<UserApprovedPayload> {
    companion object {
        const val EVENT_TYPE = "APPROVED"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

data class UserApprovedPayload @JsonCreator constructor(
        @param:JsonProperty("token")
        val token: String
)