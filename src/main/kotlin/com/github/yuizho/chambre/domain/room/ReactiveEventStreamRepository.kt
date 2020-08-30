package com.github.yuizho.chambre.domain.room

import org.springframework.data.redis.connection.stream.RecordId
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveEventStreamRepository {
    fun push(roomId: Room.Id, message: Message): Mono<RecordId>
    fun receive(roomId: Room.Id): Flux<Message>
}