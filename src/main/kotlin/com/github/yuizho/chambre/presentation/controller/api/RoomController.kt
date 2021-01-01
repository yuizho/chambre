package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.auth.dto.UserSession
import com.github.yuizho.chambre.application.service.room.RoomService
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.presentation.controller.api.dto.CreateParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.CreateResult
import com.github.yuizho.chambre.presentation.controller.api.dto.RoomResponse
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

    @GetMapping("/status/{roomId}")
    fun status(@PathVariable("roomId") roomId: String): Mono<RoomResponse> {
        return roomService.room(Room.Id.from(roomId))
                .map { room -> RoomResponse(roomId, room.name, room.status) }
    }

    @PostMapping("/create")
    fun create(
            @RequestBody @Valid param: CreateParameter,
            uriBuilder: UriComponentsBuilder
    ): Mono<CreateResult> {
        return roomService.create(param.userName, param.roomName, param.password)
                .map {
                    CreateResult(
                            it.room.id.id,
                            uriBuilder.path("/room/${it.room.id.id}").build().toUriString(),
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