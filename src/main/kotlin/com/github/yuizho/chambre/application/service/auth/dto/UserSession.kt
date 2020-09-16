package com.github.yuizho.chambre.application.service.auth.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import java.io.Serializable

data class UserSession @JsonCreator constructor(
        @param:JsonProperty("roomId")
        val roomId: String,
        @param:JsonProperty("userId")
        val userId: String,
        @param:JsonProperty("name")
        val name: String,
        @param:JsonProperty("role")
        val role: Role
) : Serializable {
    constructor(roomId: Room.Id, user: User) :
            this(
                    roomId.getIdIdWithSchemaPrefix(),
                    user.id.value,
                    user.name,
                    user.role
            )

    fun getTypedRoomId(): Room.Id {
        return Room.Id.fromIdWithSchemaPrefix(roomId)
    }

    fun getTypedUserId(): User.Id {
        return User.Id(userId)
    }
}