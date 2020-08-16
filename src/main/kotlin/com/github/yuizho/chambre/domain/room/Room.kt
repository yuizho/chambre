package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue

data class Room @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: String,
        @param:JsonProperty("status")
        val status: Status,
        @param:JsonProperty("users")
        val users: MutableSet<User> = mutableSetOf()
) {

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
}