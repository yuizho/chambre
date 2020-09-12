package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.security.dto.UserSession
import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import com.github.yuizho.chambre.domain.room.EventPublisher
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.exception.BusinessException
import com.github.yuizho.chambre.presentation.controller.api.dto.ApproveParamter
import com.github.yuizho.chambre.presentation.controller.api.dto.ApproveResponse
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/gm")
@RestController
class GmController(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val participantRepository: ReactiveParticipantRepository,
        private val eventPublisher: EventPublisher
) {
    @PostMapping("/approve")
    fun approve(@RequestBody @Valid param: ApproveParamter): Mono<ApproveResponse> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    Pair(
                            User(param.userId, param.userName, Role.NORMAL),
                            (it.authentication.principal as UserSession).roomId
                    )
                }
                .switchIfEmpty(Mono.error(BusinessException("no session information")))
                .flatMap { (user, roomId) ->
                    // add user to the room
                    reactiveRoomRepository.findRoomBy(roomId).flatMap { r ->
                        r.approve(eventPublisher, user)
                                .flatMap { token ->
                                    reactiveRoomRepository.save(r)
                                            .map { Pair(r, token) }
                                }
                    }.map { (room, token) ->
                        Triple(user, room, token)
                    }
                }
                .flatMap { (user, room, token) ->
                    // save auth info
                    participantRepository
                            .save(Participant(
                                    Participant.Id.from(token),
                                    room.id.getIdIdWithSchemaPrefix(),
                                    user.id
                            ))
                            .map { token }
                }
                .then(Mono.just(ApproveResponse()))

    }
}