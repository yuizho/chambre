package com.github.yuizho.chambre.infrastructure.repostiory.user

import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.domain.user.ReactiveUserRepository
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ReactiveUserRepositoryRedis(
        private val redisOperations: ReactiveRedisOperations<String, User>
) : ReactiveUserRepository {
    override fun findUserBy(id: String): Mono<User> {
        return redisOperations
                .opsForValue()
                .get(id)
    }

    override fun save(user: User): Mono<Boolean> {
        return redisOperations
                .opsForValue()
                .set(user.id, user)
    }
}