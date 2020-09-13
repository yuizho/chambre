package com.github.yuizho.chambre.application.service.room.dto

import com.github.yuizho.chambre.domain.room.Room

data class CreatingRoomResult(
        val authToken: String,
        val room: Room
)