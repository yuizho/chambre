package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import reactor.core.publisher.Mono

data class UnapprovedUser @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: String,
        @param:JsonProperty("name")
        val name: String
) {
    companion object {
        fun createSchemaPrefix(roomId: Room.Id): String {
            return "unapproved:${roomId.getIdIdWithSchemaPrefix()}"
        }
    }

    fun apply(publisher: EventPublisher, roomId: Room.Id, to: Set<User>): Mono<Boolean> {
        return publisher.publish(
                Applied(
                        Event.Id.from(roomId.getIdIdWithSchemaPrefix()),
                        to,
                        AppliedPayload(id, name)
                )
        ).then(Mono.just(true))
    }
}