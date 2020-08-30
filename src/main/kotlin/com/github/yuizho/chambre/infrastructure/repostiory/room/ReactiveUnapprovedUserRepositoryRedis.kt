package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.domain.room.ReactiveUnapprovedUserRepository
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.UnapprovedUser
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ReactiveUnapprovedUserRepositoryRedis(
        private val redisOperations: ReactiveStringRedisTemplate,
        private val objectMapper: ObjectMapper
) : ReactiveUnapprovedUserRepository {
    override fun findUnapprovedUserBy(roomId: Room.Id, userId: String): Mono<UnapprovedUser> {
        return redisOperations
                .opsForHash<String, String>()
                .get(UnapprovedUser.createSchemaPrefix(roomId), userId)
                .map { objectMapper.readValue(it, UnapprovedUser::class.java) }
    }

    override fun put(roomId: Room.Id, unapprovedUser: UnapprovedUser): Mono<Boolean> {
        return redisOperations
                .opsForHash<String, String>()
                .put(
                        UnapprovedUser.createSchemaPrefix(roomId),
                        unapprovedUser.id,
                        objectMapper.writeValueAsString(unapprovedUser)
                )
    }

    override fun contains(roomId: Room.Id, userId: String): Mono<Boolean> {
        return redisOperations
                .opsForHash<String, String>()
                .hasKey(UnapprovedUser.createSchemaPrefix(roomId), userId)
    }

    override fun remove(roomId: Room.Id, userId: String): Mono<Boolean> {
        return redisOperations
                .opsForHash<String, String>()
                .remove(UnapprovedUser.createSchemaPrefix(roomId), userId)
                .map { it == 1L }
    }
}