package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.UserService
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.presentation.controller.api.dto.ApplyParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.ApplyResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/api/user")
@RestController
class UserController(
        private val userService: UserService
) {
    @PostMapping("/apply")
    fun apply(@RequestBody @Valid param: ApplyParameter): Mono<ApplyResponse> {
        return userService.apply(
                Room.Id.from(param.roomId),
                param.roomKey,
                param.userName
        ).map { userId ->
            ApplyResponse(userId)
        }
    }
}