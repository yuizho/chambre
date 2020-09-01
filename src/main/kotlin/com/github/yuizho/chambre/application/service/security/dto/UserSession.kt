package com.github.yuizho.chambre.application.service.security.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User

data class UserSession @JsonCreator constructor(
        @param:JsonProperty("roomId")
        val roomId: Room.Id,
        @param:JsonProperty("userId")
        val userId: String,
        @param:JsonProperty("name")
        val name: String,
        @param:JsonProperty("role")
        val role: Role
) {
    constructor(roomId: Room.Id, user: User) : this(roomId, user.id, user.name, user.role)
}