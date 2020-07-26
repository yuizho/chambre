package com.github.yuizho.chambre.application.service.security

import com.github.yuizho.chambre.domain.room.Status
import org.springframework.security.authentication.DisabledException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import reactor.core.publisher.Mono

class ReactiveApprovalAuthenticationManager(
        private val reactiveRoomUserService: ReactiveRoomUserService
) : ReactiveAuthenticationManager {
    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        // get authToken from authentication
        val authToken = authentication.credentials as String

        // retrieve User
        val user = reactiveRoomUserService.retrieveUser(authToken)

        // check the User
        return user.doOnNext {
            // TODO: Status should have this logic?
            if (it.status != Status.AVAILABLE) {
                throw DisabledException("the user status is not available. Status: ${it.status}")
            }
        }.map { UserAuthenticationToken(it) }
    }
}