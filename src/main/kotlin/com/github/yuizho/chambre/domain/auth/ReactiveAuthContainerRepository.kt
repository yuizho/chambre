package com.github.yuizho.chambre.domain.auth

import reactor.core.publisher.Mono

interface ReactiveAuthContainerRepository {
    fun findAuthContainerBy(token: String): Mono<AuthContainer>
    fun save(authContainer: AuthContainer): Mono<Boolean>
}