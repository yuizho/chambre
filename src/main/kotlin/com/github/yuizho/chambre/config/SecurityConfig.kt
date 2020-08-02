package com.github.yuizho.chambre.config

import com.github.yuizho.chambre.application.service.security.*
import com.github.yuizho.chambre.domain.auth.ReactiveAuthContainerRepository
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.authentication.AuthenticationWebFilter
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.server.WebFilter

@Configuration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
@EnableWebFluxSecurity
class SecurityConfig {
    @Bean
    fun securityFilterChain(
            http: ServerHttpSecurity,
            authenticationManager: ReactiveAuthenticationManager
    ): SecurityWebFilterChain {
        // https://github.com/spring-projects/spring-security/blob/master/config/src/main/java/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.java#L211
        return http
                // FIXME: after authentication, authenticated entpoint still returns 403...
                // TODO: need csrf filter
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .logout().disable()

                .authorizeExchange()
                .pathMatchers("/room").permitAll()
                .pathMatchers("/auth").permitAll()
                .pathMatchers("/notify/**").permitAll()
                .anyExchange().hasAnyRole()
                .and()
                .addFilterAt(
                        authenticationWebFilter(
                                authenticationManager,
                                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/auth")
                        ),
                        SecurityWebFiltersOrder.AUTHENTICATION
                )
                .build()
    }

    @Bean
    fun authenticationManager(
            reactiveRoomUserService: ReactiveRoomUserService
    ): ReactiveAuthenticationManager {
        return ReactiveApprovalAuthenticationManager(reactiveRoomUserService)
    }

    @Bean
    fun reactiveRoomUserService(
            reactiveAuthContainerRepository: ReactiveAuthContainerRepository,
            reactiveRoomRepository: ReactiveRoomRepository
    ): ReactiveRoomUserService {
        return ReactiveRoomUserServiceImpl(
                reactiveAuthContainerRepository,
                reactiveRoomRepository
        )
    }

    fun authenticationWebFilter(
            authenticationManager: ReactiveAuthenticationManager,
            loginPath: ServerWebExchangeMatcher
    ): WebFilter {
        return AuthenticationWebFilter(authenticationManager).also {
            it.setRequiresAuthenticationMatcher(loginPath)
            it.setServerAuthenticationConverter(ApprovalFormAuthenticationConverter())
            it.setAuthenticationSuccessHandler(ApprovalAuthenticationSuccessHandler())
            it.setAuthenticationFailureHandler(ApprovalAuthenticationFailureHandler())
            it.setSecurityContextRepository(WebSessionServerSecurityContextRepository())
        }
    }
}
