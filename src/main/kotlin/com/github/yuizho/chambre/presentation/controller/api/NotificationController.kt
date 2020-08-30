package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.NotificationService
import com.github.yuizho.chambre.domain.room.User
import org.springframework.http.codec.ServerSentEvent
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RequestMapping("/notify")
@RestController
class NotificationController(
        private val notificationService: NotificationService
) {
    // TODO: add the notification for entry user

    @GetMapping("/{roomId}")
    fun notify(@PathVariable roomId: String): Flux<ServerSentEvent<String>> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    it.authentication.principal as User
                }
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