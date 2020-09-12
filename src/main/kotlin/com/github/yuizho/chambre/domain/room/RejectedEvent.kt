package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator

data class RejectedEvent @JsonCreator constructor(
        override val id: Event.Id,
        override val to: Set<User.Id>,
        override val payload: RejectedPayload = RejectedPayload()
) : Event<RejectedPayload> {
    companion object {
        const val EVENT_TYPE = "REJECT"
    }

    override fun getEventName(): String {
        return EVENT_TYPE
    }
}

class RejectedPayload