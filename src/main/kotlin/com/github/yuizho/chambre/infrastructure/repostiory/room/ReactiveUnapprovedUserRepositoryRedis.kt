package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.config.RedisProperties
import com.github.yuizho.chambre.domain.room.ReactiveUnapprovedUserRepository
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.UnapprovedUser
import com.github.yuizho.chambre.domain.room.User
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.time.Duration

@Repository
class ReactiveUnapprovedUserRepositoryRedis(
        private val redisOperations: ReactiveStringRedisTemplate,
        private val redisProperties: RedisProperties,
        private val objectMapper: ObjectMapper
) : ReactiveUnapprovedUserRepository {
    override fun findUnapprovedUserBy(roomId: Room.Id, userId: User.Id): Mono<UnapprovedUser> {
        return redisOperations
                .opsForHash<String, String>()
                .get(UnapprovedUser.createSchemaPrefix(roomId), userId.value)
                .map { objectMapper.readValue(it, UnapprovedUser::class.java) }
    }

    override fun findUnapprovedUsers(roomId: Room.Id): Flux<UnapprovedUser> {
        return redisOperations
                .opsForHash<String, String>()
                .values(UnapprovedUser.createSchemaPrefix(roomId))
                .map { value ->
                    objectMapper.readValue(value, UnapprovedUser::class.java)
                }
    }

    override fun contains(roomId: Room.Id, userId: User.Id): Mono<Boolean> {
        return redisOperations
                .opsForHash<String, String>()
                .hasKey(UnapprovedUser.createSchemaPrefix(roomId), userId.value)
    }

    override fun put(roomId: Room.Id, unapprovedUser: UnapprovedUser): Mono<Boolean> {
        return Mono.fromCallable {
            objectMapper.writeValueAsString(unapprovedUser)
        }.subscribeOn(Schedulers.boundedElastic()).flatMap { serializedUnapprovedUser ->
            redisOperations
                    .opsForHash<String, String>()
                    .put(
                            UnapprovedUser.createSchemaPrefix(roomId),
                            unapprovedUser.id.value,
                            serializedUnapprovedUser
                    ).flatMap {
                        redisOperations.expire(
                                UnapprovedUser.createSchemaPrefix(roomId),
                                Duration.ofSeconds(redisProperties.expireSec)
                        )
                    }
        }
    }

    override fun remove(roomId: Room.Id, userId: User.Id): Mono<Boolean> {
        return redisOperations
                .opsForHash<String, String>()
                .remove(UnapprovedUser.createSchemaPrefix(roomId), userId.value)
                .map { it == 1L }
    }
}