package com.github.yuizho.chambre.infrastructure.repostiory.auth

import com.github.yuizho.chambre.config.RedisProperties
import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.auth.ReactiveParticipantRepository
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class ReactiveParticipantRepositoryRedis(
        private val redisOperations: ReactiveRedisOperations<String, Participant>,
        private val redisProperties: RedisProperties
) : ReactiveParticipantRepository {
    override fun findParticipantBy(id: Participant.Id): Mono<Participant> {
        return redisOperations
                .opsForValue()
                .get(id.getIdIdWithSchemaPrefix())
    }

    override fun save(participant: Participant): Mono<Boolean> {
        return redisOperations
                .opsForValue()
                .set(
                        participant.token.getIdIdWithSchemaPrefix(),
                        participant,
                        Duration.ofSeconds(redisProperties.expireSec)
                )
    }
}