package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.RoomService
import com.github.yuizho.chambre.application.service.security.dto.UserSession
import com.github.yuizho.chambre.presentation.controller.api.dto.CreateParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.CreateResult
import com.github.yuizho.chambre.presentation.controller.api.dto.UsersResponse
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/api/room")
@RestController
class RoomController(
        private val roomService: RoomService
) {

    @PostMapping("/create")
    fun create(@RequestBody @Valid param: CreateParameter): Mono<CreateResult> {
        return roomService.create(param.userName, param.roomName)
                .map {
                    CreateResult(
                            it.room.id.id,
                            // TODO: create URL
                            "",
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