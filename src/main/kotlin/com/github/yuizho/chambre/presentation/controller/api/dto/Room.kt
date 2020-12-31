package com.github.yuizho.chambre.presentation.controller.api.dto

import com.github.yuizho.chambre.domain.room.Room

data class RoomResponse(
        val id: String,
        val name: String,
        val status: Room.Status
)