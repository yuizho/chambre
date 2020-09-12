package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.UserService
import com.github.yuizho.chambre.presentation.controller.api.dto.EntryParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.EntryResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/user")
@RestController
class UserController(
        private val userService: UserService
) {
    @PostMapping("/entry")
    fun entry(@RequestBody @Valid param: EntryParameter): Mono<EntryResponse> {
        return userService.entry(
                param.roomId,
                param.roomKey,
                param.userName
        ).map { userId ->
            EntryResponse(userId)
        }
    }
}