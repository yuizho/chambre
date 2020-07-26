package com.github.yuizho.chambre.application.service.security

import com.github.yuizho.chambre.domain.room.User
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

class UserAuthenticationToken(
        private val user: User
) : AbstractAuthenticationToken(listOf(user.role.grantedAuthority)) {
    override fun getCredentials(): Any? {
        return null
    }

    override fun getPrincipal(): Any = user

    override fun getName(): String = user.name
}