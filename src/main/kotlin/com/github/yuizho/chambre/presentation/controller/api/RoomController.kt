package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.auth.dto.UserSession
import com.github.yuizho.chambre.application.service.room.RoomService
import com.github.yuizho.chambre.presentation.controller.api.dto.CreateParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.CreateResult
import com.github.yuizho.chambre.presentation.controller.api.dto.UsersResponse
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/api/room")
@RestController
class RoomController(
        private val roomService: RoomService
) {

    @PostMapping("/create")
    fun create(
            @RequestBody @Valid param: CreateParameter,
            uriBuilder: UriComponentsBuilder
    ): Mono<CreateResult> {
        return roomService.create(param.userName, param.roomName)
                .map {
                    CreateResult(
                            it.room.id.id,
                            uriBuilder.path("/room/${it.room.id.id}").build().toUriString(),
                            it.room.key,
                            it.authToken
                    )
                }
    }

    @GetMapping("/users")
    fun users(): Mono<UsersResponse> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    (it.authentication.principal as UserSession).getTypedRoomId()
                }.flatMap { roomId ->
                    roomService.users(roomId)
                }.map { users ->
                    UsersResponse(users)
                }
    }
}