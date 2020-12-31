package com.github.yuizho.chambre.presentation.controller.api.dto

import com.github.yuizho.chambre.domain.room.Role

data class UserResponse(
        val id: String,
        val name: String,
        val role: Role
)