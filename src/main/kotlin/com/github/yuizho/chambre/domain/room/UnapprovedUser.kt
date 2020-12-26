package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import reactor.core.publisher.Mono
import java.util.*

class UnapprovedUser @JsonCreator constructor(
        id: Id,
        name: String
) : User(id, name, Role.NORMAL) {
    companion object {
        fun createSchemaPrefix(roomId: Room.Id): String {
            return "unapproved:${roomId.getIdIdWithSchemaPrefix()}"
        }
    }

    fun apply(publisher: EventPublisher, roomId: Room.Id, to: Set<Id>): Mono<Boolean> {
        return publisher.publish(
                AppliedEvent(
                        Event.Id.from(roomId.getIdIdWithSchemaPrefix()),
                        to,
                        AppliedPayload(
                                UUID.randomUUID().toString(),
                                id.value,
                                name
                        )
                )
        )
    }

    override fun toString(): String {
        return "UnapprovedUser(id=$id, name='$name', role=$role)"
    }
}