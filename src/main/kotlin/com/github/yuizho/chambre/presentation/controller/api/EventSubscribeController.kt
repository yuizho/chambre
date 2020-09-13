package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.EventSubscribeService
import com.github.yuizho.chambre.application.service.security.dto.UserSession
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.http.codec.ServerSentEvent
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RequestMapping("/api/subscribe")
@RestController
class EventSubscribeController(
        private val eventSubscribeService: EventSubscribeService
) {
    @GetMapping("/unapproved")
    fun unapprovedSubscribe(
            @RequestParam("roomId") roomId: String,
            @RequestParam("userId") userId: String

    ): Flux<ServerSentEvent<String>> {
        return eventSubscribeService.subscribe(Room.Id.from(roomId), User.Id(userId))
                .map { event ->
                    ServerSentEvent
                            .builder(event.payload)
                            .event(event.name)
                            .build()
                }
    }

    @GetMapping("/approved")
    fun subscribe(): Flux<ServerSentEvent<String>> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    it.authentication.principal as UserSession
                }
                .switchIfEmpty(Mono.error(BusinessException("no session information")))
                .flatMapMany { userSession ->
                    eventSubscribeService.subscribe(userSession.getTypedRoomId(), userSession.getTypedUserId())
                            .map { event ->
                                ServerSentEvent
                                        .builder(event.payload)
                                        .event(event.name)
                                        .build()
                            }
                }

    }
}