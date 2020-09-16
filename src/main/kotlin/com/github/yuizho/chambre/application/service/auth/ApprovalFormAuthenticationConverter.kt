package com.github.yuizho.chambre.application.service.auth

import com.github.yuizho.chambre.application.service.auth.dto.ApprovalAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

class ApprovalFormAuthenticationConverter : ServerAuthenticationConverter {
    companion object {
        const val TOKEN_PARAMETER_NAME = "authToken"
    }

    override fun convert(exchange: ServerWebExchange): Mono<Authentication> {
        return exchange.formData.map { data ->
            ApprovalAuthenticationToken(data.getFirst(TOKEN_PARAMETER_NAME) ?: "")
        }
    }
}