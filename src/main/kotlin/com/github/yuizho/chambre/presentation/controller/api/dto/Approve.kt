package com.github.yuizho.chambre.presentation.controller.api.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class ApproveParamter(
        @field:NotNull
        @field:Size(min = 1, max = 36)
        val userId: String,
        @field:NotNull
        @field:Size(min = 1, max = 20)
        val userName: String
)

class ApproveResponse(
        val authToken: String
)