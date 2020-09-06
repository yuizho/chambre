package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

interface Event<T> {
    val id: EventId
    val to: Set<User>
    val payload: T

    fun getEventName(): String
}

interface ApprovedEvent<T> : Event<T> {
    override val id: Id
    override val to: Set<User>
    override val payload: T

    class Id private constructor(
            override val id: String
    ) : EventId(id) {
        companion object {
            const val SCHEMA_PREFIX = "event:"

            @JvmStatic
            @JsonCreator
            fun fromIdWithSchemaPrefix(id: String): Id {
                return Id(id.removePrefix(SCHEMA_PREFIX))
            }

            @JvmStatic
            fun from(id: String): Id {
                return Id(id)
            }
        }

        @JsonValue
        override fun getIdIdWithSchemaPrefix(): String = SCHEMA_PREFIX + id
    }
}

interface UnapprovedEvent<T> : Event<T> {
    override val id: Id
    override val to: Set<User>
    override val payload: T

    class Id private constructor(
            override val id: String
    ) : EventId(id) {

        companion object {
            const val SCHEMA_PREFIX = "unapproved-event:"

            @JvmStatic
            @JsonCreator
            fun fromIdWithSchemaPrefix(id: String): Id {
                return Id(id.removePrefix(SCHEMA_PREFIX))
            }

            @JvmStatic
            fun from(id: String): Id {
                return Id(id)
            }
        }

        @JsonValue
        override fun getIdIdWithSchemaPrefix(): String = SCHEMA_PREFIX + id
    }
}

abstract class EventId(
        open val id: String
) {
    abstract fun getIdIdWithSchemaPrefix(): String

    override fun toString(): String {
        return "Id(id='$id')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EventId

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}