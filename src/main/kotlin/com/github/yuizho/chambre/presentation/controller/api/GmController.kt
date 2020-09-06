package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.security.dto.UserSession
import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import com.github.yuizho.chambre.domain.room.*
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
                    val us = it.authentication.principal as UserSession
                    Pair(
                            User(param.userId, param.userName, Role.NORMAL, Status.AVAILABLE),
                            us.roomId
                    )
                }
                .switchIfEmpty(Mono.error(BusinessException("no session information")))
                .flatMap {
                    // add user to the room
                    reactiveRoomRepository.findRoomBy(it.second).flatMap { r ->
                        r.approve(eventPublisher, it.first)
                                .flatMap { token ->
                                    reactiveRoomRepository.save(r)
                                            .map { Pair(r, token) }
                                }
                    }.map { p ->
                        Triple(it.first, p.first, p.second)
                    }
                }
                .flatMap {
                    // save auth info
                    participantRepository
                            .save(Participant(
                                    Participant.Id.from(it.third),
                                    it.second.id.getIdIdWithSchemaPrefix(),
                                    it.first.id
                            ))
                            .map { _ -> it.third }
                }
                .then(Mono.just(ApproveResponse()))

    }
}