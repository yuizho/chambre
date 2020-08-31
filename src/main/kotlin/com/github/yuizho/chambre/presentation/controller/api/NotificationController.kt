package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.NotificationService
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

@RequestMapping("/notify")
@RestController
class NotificationController(
        private val notificationService: NotificationService
) {
    // TODO: add the notification for entry user

    @GetMapping("/approved")
    fun approvedNotify(@RequestParam("roomId") roomId: String): Flux<ServerSentEvent<String>> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    it.authentication.principal as User
                }
                .switchIfEmpty(Mono.error(BusinessException("no session information")))
                .flatMapMany { user ->
                    notificationService.notify(roomId, user)
                            .map { message ->
                                ServerSentEvent
                                        .builder(message.payload)
                                        .event(message.eventType.name)
                                        .build()
                            }
                }

    }
}