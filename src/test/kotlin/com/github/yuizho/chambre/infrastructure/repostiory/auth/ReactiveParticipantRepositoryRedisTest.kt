package com.github.yuizho.chambre.infrastructure.repostiory.auth

import com.github.yuizho.chambre.RedisConfig
import com.github.yuizho.chambre.domain.auth.Participant
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier
import redis.clients.jedis.Jedis

@Testcontainers
@SpringBootTest
class ReactiveParticipantRepositoryRedisTest {
    @Autowired
    lateinit var reactiveParticipantRepositoryRedis: ReactiveParticipantRepositoryRedis

    @Container
    val redis = RedisConfig.redis

    @Test
    fun `fetch auth_container by token`() {
        // given
        val expected = Participant(
                Participant.Id.from("9999999"),
                "1",
                "2"
        )

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .set(
                        expected.token.getIdIdWithSchemaPrefix(),
                        String(Jackson2JsonRedisSerializer(Participant::class.java)
                                .serialize(expected))
                )

        // when
        val actual = reactiveParticipantRepositoryRedis.findParticipantBy(expected.token)

        // then
        StepVerifier.create(actual)
                .expectNext(expected)
                .verifyComplete()
    }

    @Test
    fun `save auth_container data`() {
        // given
        val expected = Participant(
                Participant.Id.fromIdWithSchemaPrefix("participant:1234567890"),
                "1",
                "2"
        )

        // when
        val actual = reactiveParticipantRepositoryRedis.save(expected)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
    }
}