package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.auth.dto.UserSession
import com.github.yuizho.chambre.application.service.room.UserService
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.presentation.controller.api.dto.ApplyParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.ApplyResponse
import com.github.yuizho.chambre.presentation.controller.api.dto.UserResponse
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/api/user")
@RestController
class UserController(
        private val userService: UserService
) {
    @GetMapping("/status")
    fun user(): Mono<UserResponse> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    (it.authentication.principal as UserSession)
                }.map { us ->
                    UserResponse(us.userId, us.name, us.role)
                }
    }

    @PostMapping("/apply")
    fun apply(@RequestBody @Valid param: ApplyParameter): Mono<ApplyResponse> {
        return userService.apply(
                Room.Id.from(param.roomId),
                param.roomKey,
                param.userName
        ).map { userId ->
            ApplyResponse(userId, param.userName)
        }
    }
}