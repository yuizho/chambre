package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.presentation.dto.Message
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RequestMapping("/event")
@RestController
class EventController(
        private val reactiveRedisOperations: ReactiveRedisOperations<String, Message>
) {
    companion object {
        const val ROOM_ID = "CHAMBRE"
    }

    // TODO: demo end point
    @PostMapping("/post")
    fun postMessage(@RequestBody message: Message): Mono<String> {
        // ココ続けてreturnしないとブロックされてしまう
        return reactiveRedisOperations.opsForStream<String, Message>()
                .add(MapRecord.create(ROOM_ID, mapOf("id" to message.id, "value" to message.value)))
                .log("finished to send message")
                .map { "Ok" }
    }
}