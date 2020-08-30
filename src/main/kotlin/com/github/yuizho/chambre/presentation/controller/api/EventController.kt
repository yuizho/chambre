package com.github.yuizho.chambre.presentation.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.ReactiveUnapprovedUserRepository
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.UnapprovedUser
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
        private val reactiveUnapprovedUserRepository: ReactiveUnapprovedUserRepository,
        private val objectMapper: ObjectMapper
) {
    @PostMapping("/entry")
    fun entry(@RequestBody @Valid param: EntryParameter): Mono<EntryResponse> {
        val roomId = Room.Id.from(param.roomId)
        val room = reactiveRoomRepository.findRoomBy(roomId)
        val newUser = UnapprovedUser(
                param.userId,
                param.userName
        )
        // TODO: should implement password check?
        return room
                .switchIfEmpty(Mono.error(BusinessException("invalid room id.")))
                .flatMap { room ->
                    reactiveUnapprovedUserRepository.contains(roomId, param.userId)
                            .map { joined -> Pair<Room, Boolean>(room, joined) }
                }
                .doOnNext { pair ->
                    if (pair.second) {
                        throw BusinessException("you have already joined this room.")
                    }
                    // TODO: should check same name user?
                }
                .flatMap { pair ->
                    reactiveUnapprovedUserRepository.put(roomId, newUser)
                            .map { pair.first }
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