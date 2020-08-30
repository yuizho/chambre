package com.github.yuizho.chambre.presentation.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.ReactiveEventStreamRepository
import com.github.yuizho.chambre.domain.room.Room
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
        private val reactiveEventStreamRepository: ReactiveEventStreamRepository,
        private val objectMapper: ObjectMapper
) {
    // TODO: add the notification for entry user

    @GetMapping("/{roomId}")
    fun notify(@PathVariable roomId: String): Flux<ServerSentEvent<String>> {
        return ReactiveSecurityContextHolder.getContext()
                .map {
                    it.authentication.principal as User
                }
                .flatMapMany { user ->
                    reactiveEventStreamRepository.receive(Room.Id.from(roomId))
                            .log()
                            .filter { it.to.contains(user) }
                            .map { message ->
                                ServerSentEvent.builder(
                                        objectMapper.writeValueAsString(message.getPayloadObject())
                                ).event(message.eventType.name).build()
                            }
                }

    }
}