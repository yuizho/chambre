package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.security.core.GrantedAuthority

data class User @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: String,
        @param:JsonProperty("name")
        val name: String,
        // TODO: should be implemented by inherit?
        @param:JsonProperty("role")
        val role: Role,
        @param:JsonProperty("status")
        val status: Status
)

enum class Role(
        @field:JsonValue
        val value: Int,
        val grantedAuthority: GrantedAuthority
) {
    NORMAL(0, GrantedAuthority { "NORMAL" }),
    ADMIN(1, GrantedAuthority { "ADMIN" });

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

