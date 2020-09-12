package com.github.yuizho.chambre.presentation.controller.api.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class EntryParameter(
        @field:NotNull
        @field:Size(min = 1, max = 36)
        val roomId: String,
        @field:NotNull
        @field:Size(min = 1, max = 10)
        val roomKey: String,
        @field:NotNull
        @field:Size(min = 1, max = 20)
        val userName: String
)

data class EntryResponse(
        val userId: String
)