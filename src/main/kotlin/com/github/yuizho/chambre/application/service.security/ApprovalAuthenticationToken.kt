package com.github.yuizho.chambre.application.service.security

import org.springframework.security.authentication.AbstractAuthenticationToken

class ApprovalAuthenticationToken(
        private val authToken: String
) : AbstractAuthenticationToken(emptyList()) {
    override fun getCredentials(): Any {
        return authToken
    }

    override fun getPrincipal(): Any? = null

    override fun getName(): String? = null
}