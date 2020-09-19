package com.github.yuizho.chambre.application.service.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.application.service.auth.dto.UserSession
import com.github.yuizho.chambre.config.SecurityCookieProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseCookie
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.WebFilterExchange
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler
import reactor.core.publisher.Mono
import java.time.Duration


class ApprovalAuthenticationSuccessHandler(
        private val securityCookieProperties: SecurityCookieProperties,
        private val privateKey: ByteArray,
        private val objectMapper: ObjectMapper
) : ServerAuthenticationSuccessHandler {
    override fun onAuthenticationSuccess(
            webFilterExchange: WebFilterExchange,
            authentication: Authentication
    ): Mono<Void> = Mono.fromRunnable {
        when (val principal = authentication.principal) {
            is UserSession -> {
                val jws = principal.toJws(objectMapper, privateKey)
                val cookieName = securityCookieProperties.name
                webFilterExchange.exchange.response.cookies.add(
                        cookieName,
                        ResponseCookie.from(cookieName, jws.serialize())
                                .path(securityCookieProperties.path)
                                .httpOnly(securityCookieProperties.httpOnly)
                                .secure(securityCookieProperties.secure)
                                .maxAge(Duration.ofSeconds(securityCookieProperties.expireSec))
                                .build()
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