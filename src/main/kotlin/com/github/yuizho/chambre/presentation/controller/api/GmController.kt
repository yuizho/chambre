package com.github.yuizho.chambre.presentation.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
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
import java.util.*
import javax.validation.Valid

@RequestMapping("/gm")
@RestController
class GmController(
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val participantRepository: ReactiveParticipantRepository,
        private val reactiveUnapprovedEventStreamRepository: ReactiveUnapprovedEventStreamRepository,
        private val reactiveEventStreamRepository: ReactiveEventStreamRepository,
        private val objectMapper: ObjectMapper
) {
    @PostMapping("/approve")
    fun entry(@RequestBody @Valid param: ApproveParamter): Mono<ApproveResponse> {
        val authToken = UUID.randomUUID().toString()
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
                        r.users.add(it.first)
                        reactiveRoomRepository.save(r).map { r }
                    }.map { r ->
                        Pair(it.first, r)
                    }
                }
                .flatMap {
                    // save auth info
                    val participant = Participant(
                            Participant.Id.from(authToken),
                            it.second.id.getIdIdWithSchemaPrefix(),
                            it.first.id
                    )
                    participantRepository
                            .save(participant)
                            .map { _ -> Triple(it.first, it.second, participant) }
                }
                .flatMap {
                    // push authtoken to approved user
                    reactiveUnapprovedEventStreamRepository.push(
                            it.second.id,
                            Message(
                                    setOf(it.first),
                                    EventType.APPROVED,
                                    objectMapper.writeValueAsString(it.third)
                            )
                    ).map { _ -> it }
                }
                .flatMap {
                    // push notify to all
                    reactiveEventStreamRepository.push(
                            it.second.id,
                            Message(
                                    it.second.users,
                                    EventType.JOIN,
                                    objectMapper.writeValueAsString(it.first)
                            )
                    )
                }
                .then(Mono.just(ApproveResponse(authToken)))
    }
}