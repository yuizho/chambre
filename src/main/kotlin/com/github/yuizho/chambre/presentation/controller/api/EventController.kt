package com.github.yuizho.chambre.presentation.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.Status
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.presentation.dto.Destination
import com.github.yuizho.chambre.presentation.dto.EventType
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
        private val reactiveRedisOperations: ReactiveRedisOperations<String, Message>,
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val objectMapper: ObjectMapper
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

    @PostMapping("/entry")
    fun entry(@RequestBody param: EntryParameter): Mono<String> {
        // TODO: fingar print check (to prevent deprecate entry)
        val room = reactiveRoomRepository.findRoomBy(param.roomId)
        val newUser = User(
                // TODO: it should be primary (by UUID)
                "4",
                param.userName,
                Role.NORMAL,
                Status.NEEDS_APPROVAL
        )
        // TODO: room null check
        // TODO: check the room already has same user name?
        return room
                .flatMap {
                    // add the user to room
                    it.users.add(newUser)
                    reactiveRoomRepository.save(it)
                }
                .flatMap {
                    reactiveRedisOperations.opsForStream<String, Message>()
                            .add(MapRecord.create(
                                    "event:${param.roomId}",
                                    mapOf(
                                            "to" to Destination.ToAdmin,
                                            "eventType" to EventType.ENTRY,
                                            "payload" to objectMapper.writeValueAsString(newUser)
                                    )
                            ))
                }
                .then(Mono.just("ok"))
    }
}

data class EntryParameter(
        val roomId: String,
        val userName: String
)