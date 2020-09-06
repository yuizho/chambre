package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue

interface Event<T> {
    val id: Id
    val to: Set<User>
    val payload: T

    fun getEventName(): String

    class Id private constructor(
            val id: String
    ) {
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
        fun getIdIdWithSchemaPrefix(): String = SCHEMA_PREFIX + id

        override fun toString(): String {
            return "Id(id='$id')"
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Id

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id.hashCode()
        }
    }
}