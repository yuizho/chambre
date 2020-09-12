package com.github.yuizho.chambre.presentation.controller.api.dto

import com.github.yuizho.chambre.domain.room.User

data class UsersResponse(
        val users: List<User>
)