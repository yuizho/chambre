package com.github.yuizho.chambre.domain.user

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

data class User @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: String,
        @param:JsonProperty("name")
        val name: String,
        @param:JsonProperty("role")
        val role: Role,
        @param:JsonProperty("status")
        val status: Status
)

enum class Role(
        @field:JsonValue
        val value: Int
) {
    NORMAL(0),
    ADMIN(1);

    @JsonCreator
    fun of(value: Int): Role {
        return values().find { it.value == value }
                ?: throw IllegalArgumentException("unexpected value is passed to get Role.")
    }
}

enum class Status(
        @field:JsonValue
        val value: Int
) {
    NEEDS_APPROVAL(0),
    AVAILABLE(1),
    UNAVAILABLE(2);

    @JsonCreator
    fun of(value: Int): Status {
        return values().find { it.value == value }
                ?: throw IllegalArgumentException("unexpected value is passed to get Status.")
    }
}

