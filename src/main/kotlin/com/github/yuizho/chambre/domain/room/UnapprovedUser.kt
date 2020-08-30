package com.github.yuizho.chambre.domain.room

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class UnapprovedUser @JsonCreator constructor(
        @param:JsonProperty("id")
        val id: String,
        @param:JsonProperty("name")
        val name: String
) {
    companion object {
        fun createSchemaPrefix(roomId: Room.Id): String {
            return "unapproved:${roomId.getIdIdWithSchemaPrefix()}"
        }
    }
}