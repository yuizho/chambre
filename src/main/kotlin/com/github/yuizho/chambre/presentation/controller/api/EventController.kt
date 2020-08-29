package com.github.yuizho.chambre.presentation.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.Status
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.exception.BusinessException
import com.github.yuizho.chambre.presentation.controller.api.dto.EntryParameter
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
                .add(MapRecord.create(ROOM_ID, mapOf("to" to message.to, "payload" to message.payload)))
                .log("finished to send message")
                .map { "Ok" }
    }

    // TODO: validation
    @PostMapping("/entry")
    fun entry(@RequestBody param: EntryParameter): Mono<String> {
        // TODO: fingar print check (to prevent deprecate entry)
        val room = reactiveRoomRepository.findRoomBy(param.roomId)
        val newUser = User(
                // TODO: it should be primary (by UUID) or fingar print
                "4",
                param.userName,
                Role.NORMAL,
                Status.NEEDS_APPROVAL
        )
        return room
                .switchIfEmpty(Mono.error(BusinessException("invalid room id.")))
                .doOnNext { room ->
                    if (newUser in room.users) {
                        throw BusinessException("you have already joined this room.")
                    }
                    if (room.users.any { it.name == newUser.name }) {
                        throw BusinessException("same name user have already joined this room.")
                    }
                }
                .flatMap { room ->
                    // add the user to room
                    room.users.add(newUser)
                    reactiveRoomRepository.save(room).map { room }
                }
                .flatMap { room ->
                    reactiveRedisOperations.opsForStream<String, Message>()
                            .add(MapRecord.create(
                                    "event:${param.roomId}",
                                    mapOf(
                                            "to" to objectMapper.writeValueAsString(setOf(room.adminUser())),
                                            "eventType" to EventType.ENTRY,
                                            "payload" to objectMapper.writeValueAsString(newUser)
                                    )
                            ))
                }
                .then(Mono.just("ok"))
    }
}