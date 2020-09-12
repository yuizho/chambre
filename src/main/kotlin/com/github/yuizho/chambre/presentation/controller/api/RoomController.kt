package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.RoomService
import com.github.yuizho.chambre.application.service.security.dto.UserSession
import com.github.yuizho.chambre.presentation.controller.api.dto.UsersResponse
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/api/room")
@RestController
class RoomController(
        private val roomService: RoomService
) {
    @GetMapping("/users")
    fun users(): Mono<UsersResponse> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    (it.authentication.principal as UserSession).roomId
                }.flatMap { roomId ->
                    roomService.users(roomId)
                }.map { users ->
                    UsersResponse(users)
                }
    }
}