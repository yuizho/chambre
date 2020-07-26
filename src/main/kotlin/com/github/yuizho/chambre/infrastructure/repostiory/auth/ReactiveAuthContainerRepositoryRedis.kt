package com.github.yuizho.chambre.infrastructure.repostiory.auth

import com.github.yuizho.chambre.domain.auth.AuthContainer
import com.github.yuizho.chambre.domain.auth.ReactiveAuthContainerRepository
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ReactiveAuthContainerRepositoryRedis(
        private val redisOperations: ReactiveRedisOperations<String, AuthContainer>
) : ReactiveAuthContainerRepository {
    override fun findAuthContainerBy(token: String): Mono<AuthContainer> {
        return redisOperations
                .opsForValue()
                .get(token)
    }

    override fun save(authContainer: AuthContainer): Mono<Boolean> {
        return redisOperations
                .opsForValue()
                .set(authContainer.token, authContainer)
    }
}