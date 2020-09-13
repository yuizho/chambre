package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import org.springframework.security.core.GrantedAuthority

open class User @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: Id,
        @param:JsonProperty("name")
        val name: String,
        @param:JsonProperty("role")
        val role: Role
) {
    data class Id @JsonCreator(mode = JsonCreator.Mode.DELEGATING) constructor(
            @field:JsonValue
            val value: String
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as User

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }

    override fun toString(): String {
        return "User(id=$id, name='$name', role=$role)"
    }
}

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

