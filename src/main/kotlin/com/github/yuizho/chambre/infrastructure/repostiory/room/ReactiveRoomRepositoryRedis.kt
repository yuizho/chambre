package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.github.yuizho.chambre.domain.room.EventPublisher
import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Room
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ReactiveRoomRepositoryRedis(
        private val redisOperations: ReactiveRedisOperations<String, Room>,
        private val eventpublisher: EventPublisher
) : ReactiveRoomRepository {
    override fun findRoomBy(id: Room.Id): Mono<Room> {
        return redisOperations
                .opsForValue()
                .get(id.getIdIdWithSchemaPrefix())
                .doOnNext { room ->
                    room.publisher = eventpublisher
                }
    }

    override fun save(room: Room): Mono<Boolean> {
        return redisOperations
                .opsForValue()
                .set(room.id.getIdIdWithSchemaPrefix(), room)
    }
}