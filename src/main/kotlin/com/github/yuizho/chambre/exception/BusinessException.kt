package com.github.yuizho.chambre.exception

class BusinessException(
        override val message: String,
        override val cause: Throwable? = null
) : RuntimeException()