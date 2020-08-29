package com.github.yuizho.chambre.domain.auth

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Participant @JsonCreator constructor(
        @param:JsonProperty("token")
        val token: String,
        @param:JsonProperty("roomId")
        val roomId: String,
        @param:JsonProperty("userId")
        val userId: String
)