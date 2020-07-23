package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.presentation.dto.Message
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.http.codec.ServerSentEvent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RequestMapping("/notify")
@RestController
class NotificationController(
        private val streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>
) {
    companion object {
        const val ROOM_ID = "CHAMBRE"
    }

    @GetMapping("/{id}")
    fun notify(@PathVariable id: String): Flux<ServerSentEvent<Message>> {
        return streamReceiver.receive(StreamOffset.latest(ROOM_ID))
                .map { Message(it.value["id"]!!, it.value["value"]!!) }
                .map { message ->
                    ServerSentEvent.builder(message).event("notify").build()
                }
    }
}