package com.github.yuizho.chambre.application.service.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

class ReactiveApprovalAuthenticationManager(
        private val reactiveRoomUserService: ReactiveRoomUserService
) : ReactiveAuthenticationManager {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(ReactiveAuthenticationManager::class.java)
    }

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        // get authToken from authentication
        val authToken = authentication.credentials as String

        // retrieve User
        val user = reactiveRoomUserService.retrieveUser(authToken)

        // check the User
        return user
                .switchIfEmpty(Mono.error(DisabledException("the authenticationToken ($authToken) is not available")))
                .doOnNext {
                    it.status.validate()
                }.map {
                    UserAuthenticationToken(it).also { uat ->
                        uat.isAuthenticated = true
                    } as Authentication
                }.doOnError {
                    logger.warn(it.message)
                }
    }
}