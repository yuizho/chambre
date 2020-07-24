package com.github.yuizho.chambre.domain.user

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class User @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: String,
        @param:JsonProperty("name")
        val name: String
)

enum class Role(val value: Int) {
    NORMAL(0),
    ADMIN(1)
}

enum class Status(val value: Int) {
    NEEDS_APPROVAL(0),
    APPROVED(1),
    REJECTED(2)
}

