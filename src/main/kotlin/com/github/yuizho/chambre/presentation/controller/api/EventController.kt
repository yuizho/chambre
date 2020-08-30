package com.github.yuizho.chambre.presentation.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.*
import com.github.yuizho.chambre.exception.BusinessException
import com.github.yuizho.chambre.presentation.controller.api.dto.EntryParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.EntryResponse
import com.github.yuizho.chambre.presentation.dto.EventType
import com.github.yuizho.chambre.presentation.dto.Message
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/event")
@RestController
class EventController(
        private val reactiveRedisOperations: ReactiveRedisOperations<String, Message>,
        private val reactiveRoomRepository: ReactiveRoomRepository,
        private val objectMapper: ObjectMapper
) {
    @PostMapping("/entry")
    fun entry(@RequestBody @Valid param: EntryParameter): Mono<EntryResponse> {
        // TODO: fingar print check (to prevent deprecate entry)
        val room = reactiveRoomRepository.findRoomBy(Room.Id.from(param.roomId))
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
                .then(Mono.just(EntryResponse()))
    }
}