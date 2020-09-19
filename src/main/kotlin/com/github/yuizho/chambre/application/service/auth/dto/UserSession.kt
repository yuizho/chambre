package com.github.yuizho.chambre.application.service.auth.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.RSASSASigner
import java.io.Serializable
import java.security.KeyFactory
import java.security.spec.PKCS8EncodedKeySpec

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

    @JsonIgnore
    fun getTypedRoomId(): Room.Id {
        return Room.Id.fromIdWithSchemaPrefix(roomId)
    }

    @JsonIgnore
    fun getTypedUserId(): User.Id {
        return User.Id(userId)
    }

    fun toJws(objectMapper: ObjectMapper, privateKey: ByteArray): JWSObject {
        val copied = UserSession(
                // trim schema prefix
                roomId.replace("room:", ""),
                userId,
                name,
                role
        )
        val payload = objectMapper.writeValueAsString(copied)
        val key = KeyFactory.getInstance("RSA")
                .generatePrivate(PKCS8EncodedKeySpec(privateKey))
        return JWSObject(JWSHeader(JWSAlgorithm.RS256), Payload(payload)).also {
            it.sign(RSASSASigner(key))
        }
    }
}