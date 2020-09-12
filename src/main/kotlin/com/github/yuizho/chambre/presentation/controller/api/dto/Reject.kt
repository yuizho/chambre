package com.github.yuizho.chambre.presentation.controller.api.dto

import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class RejectParameter(
        @field:NotNull
        @field:Size(min = 1, max = 36)
        val userId: String
)

class RejectResponse