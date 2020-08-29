package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

data class Room @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: Id,
        @param:JsonProperty("status")
        val status: Status,
        @param:JsonProperty("users")
        val users: MutableSet<User> = mutableSetOf()
) {
    companion object {
        const val SCHEMA_PREFIX = "room:"
    }

    class Id private constructor(val id: String) {

        companion object {
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

    enum class Status(
            @field:JsonValue
            val value: Int
    ) {
        OPEN(0),
        CLOSE(1);

        @JsonCreator
        fun of(value: Int): Status {
            return values().find { it.value == value }
                    ?: throw IllegalArgumentException("unexpected value is passed to get User.Status.")
        }
    }

    fun adminUser(): User = users.first { it.role == Role.ADMIN }
}