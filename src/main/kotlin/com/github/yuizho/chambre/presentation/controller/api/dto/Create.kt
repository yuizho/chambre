package com.github.yuizho.chambre.presentation.controller.api.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateParameter(
        @field:NotNull
        @field:Size(min = 1, max = 20)
        val userName: String,
        @field:NotNull
        @field:Size(min = 1, max = 20)
        val roomName: String
)

data class CreateResult(
        val roomId: String,
        val roomUrl: String,
        val roomPassword: String,
        val authToken: String
)