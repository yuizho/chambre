package com.github.yuizho.chambre.infrastructure.repostiory.auth

import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

@Repository
class ReactiveParticipantRepositoryRedis(
        private val redisOperations: ReactiveRedisOperations<String, Participant>
) : ReactiveParticipantRepository {
    override fun findAuthContainerBy(token: String): Mono<Participant> {
        return redisOperations
                .opsForValue()
                .get(token)
    }

    override fun save(participant: Participant): Mono<Boolean> {
        return redisOperations
                .opsForValue()
                .set(participant.token, participant)
    }
}