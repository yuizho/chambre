package com.github.yuizho.chambre.presentation.controller.api

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.presentation.dto.EventType
import com.github.yuizho.chambre.presentation.dto.Message
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.stream.StreamReceiver
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
        private val streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>,
        private val objectMapper: ObjectMapper
) {
    // TODO: add the notification for entry user

    @GetMapping("/{roomId}")
    fun notify(@PathVariable roomId: String): Flux<ServerSentEvent<String>> {
        return ReactiveSecurityContextHolder.getContext()
                .map { it.authentication.principal as User }
                .flatMapMany { user ->
                    streamReceiver.receive(StreamOffset.latest(roomId))
                            .map {
                                // TODO: add error handling
                                Message(
                                        objectMapper.readValue(
                                                it.value["to"]!!,
                                                object : TypeReference<Set<User>>() {}
                                        ),
                                        EventType.valueOf(it.value["eventType"]!!),
                                        it.value["payload"]!!
                                )
                            }
                            .filter { it.to.contains(user) }
                            .map { message ->
                                ServerSentEvent.builder(
                                        objectMapper.writeValueAsString(message.getPayloadObject())
                                ).event(message.eventType.name).build()
                            }
                }

    }
}