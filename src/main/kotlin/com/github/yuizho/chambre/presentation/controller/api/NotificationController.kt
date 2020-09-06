package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.NotificationService
import com.github.yuizho.chambre.application.service.security.dto.UserSession
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.http.codec.ServerSentEvent
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RequestMapping("/notify")
@RestController
class NotificationController(
        private val notificationService: NotificationService
) {
    @GetMapping("/unapproved")
    fun unapprovedNotify(
            @RequestParam("roomId") roomId: String,
            @RequestParam("userId") userId: String

    ): Flux<ServerSentEvent<String>> {
        return notificationService.notify(roomId, userId)
                .map { event ->
                    ServerSentEvent
                            .builder(event.second)
                            .event(event.first)
                            .build()
                }
    }

    @GetMapping("/approved")
    fun notify(@RequestParam("roomId") roomId: String): Flux<ServerSentEvent<String>> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    it.authentication.principal as UserSession
                }
                .switchIfEmpty(Mono.error(BusinessException("no session information")))
                .flatMapMany { user ->
                    notificationService.notify(roomId, user.userId)
                            .map { event ->
                                ServerSentEvent
                                        .builder(event.second)
                                        .event(event.first)
                                        .build()
                            }
                }

    }
}