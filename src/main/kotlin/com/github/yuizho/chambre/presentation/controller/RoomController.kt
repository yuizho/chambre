package com.github.yuizho.chambre.presentation.controller

import com.github.yuizho.chambre.presentation.dto.Message
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.connection.stream.StreamOffset
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.stream.StreamReceiver
import org.springframework.http.codec.ServerSentEvent
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.util.*

@RequestMapping
@Controller
class RoomController {
    @GetMapping("/room")
    fun index(model: Model): String {
        //val flux = Flux.range(0, 1).map { "count: $it" }
        //.delayElements(Duration.ofSeconds(1L));
        //model.addAttribute("items", ReactiveDataDriverContextVariable(flux, 1))
        val uuid = UUID.randomUUID().toString()
        model.addAttribute("uuid", Mono.just(uuid))
        return "index.html"
    }
}

@RestController
class ServerPushController(
        private val reactiveRedisOperations: ReactiveRedisOperations<String, Message>,
        private val streamReceiver: StreamReceiver<String, MapRecord<String, String, String>>
) {
    companion object {
        const val ROOM_ID = "CHAMBRE"
    }

    // TODO: demo end point
    @PostMapping("/send/message")
    fun sendMessage(@RequestBody message: Message): Mono<String> {
        // ココ続けてreturnしないとブロックされてしまう
        return reactiveRedisOperations.opsForStream<String, Message>()
                .add(MapRecord.create(ROOM_ID, mapOf("id" to message.id, "value" to message.value)))
                .log("finished to send message")
                .map { "Ok" }
    }

    @GetMapping("/push/{id}")
    fun receive(@PathVariable id: String): Flux<ServerSentEvent<Message>> {
        return streamReceiver.receive(StreamOffset.latest(ROOM_ID))
                .map { Message(it.value["id"]!!, it.value["value"]!!) }
                .map { message ->
                    ServerSentEvent.builder(message).event("push").build()
                }
    }

}