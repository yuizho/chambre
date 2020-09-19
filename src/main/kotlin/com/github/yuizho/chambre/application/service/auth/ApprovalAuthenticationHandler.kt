package com.github.yuizho.chambre.application.service.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.application.service.auth.dto.UserSession
import com.github.yuizho.chambre.config.SecurityProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono


class ApprovalAuthenticationSuccessHandler(
        private val securityProperties: SecurityProperties,
        private val privateKey: ByteArray,
        private val objectMapper: ObjectMapper
) : ServerAuthenticationSuccessHandler {
    companion object {
        // TODO: propertyで名前変えられるようにしたい
        const val TOKEN_COOKIE_NAME = "chambre-token"
    }

    override fun onAuthenticationSuccess(
            webFilterExchange: WebFilterExchange,
            authentication: Authentication
    ): Mono<Void> = Mono.fromRunnable {
        when (val principal = authentication.principal) {
            is UserSession -> {
                val jws = principal.toJws(objectMapper, privateKey)
                // TODO: cookieのpath, secure, httponly, expirationなどを設定できるようにする
                webFilterExchange.exchange.response.cookies.add(
                        TOKEN_COOKIE_NAME,
                        ResponseCookie.from(TOKEN_COOKIE_NAME, jws.serialize()).build()
                )
            }
        }
        webFilterExchange.exchange.response.statusCode = HttpStatus.OK
    }
}

class ApprovalAuthenticationFailureHandler : ServerAuthenticationFailureHandler {
    override fun onAuthenticationFailure(
            webFilterExchange: WebFilterExchange,
            exception: AuthenticationException
    ): Mono<Void> = Mono.fromRunnable {
        webFilterExchange.exchange.response.statusCode = HttpStatus.FORBIDDEN
    }
}