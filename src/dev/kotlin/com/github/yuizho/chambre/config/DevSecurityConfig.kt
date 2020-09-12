package com.github.yuizho.chambre.config

import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.web.server.ServerHttpSecurity

@Configuration
@Profile("dev")
class DevSecurityConfig : SecurityConfig() {
    override fun configureSpecifiedExchangeSpec(authorizeExchangeSpec: ServerHttpSecurity.AuthorizeExchangeSpec)
            : ServerHttpSecurity.AuthorizeExchangeSpec {
        return super.configureSpecifiedExchangeSpec(authorizeExchangeSpec)
                .pathMatchers("/demo/**").permitAll()
    }
}