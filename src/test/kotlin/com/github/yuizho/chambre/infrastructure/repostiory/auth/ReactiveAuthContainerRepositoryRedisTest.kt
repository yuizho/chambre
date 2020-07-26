package com.github.yuizho.chambre.infrastructure.repostiory.auth

import com.github.yuizho.chambre.RedisConfig
import com.github.yuizho.chambre.domain.auth.AuthContainer
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
class ReactiveAuthContainerRepositoryRedisTest {
    @Autowired
    lateinit var reactiveAuthContainerRepositoryRedis: ReactiveAuthContainerRepositoryRedis

    @Container
    val redis = RedisConfig.redis

    @Test
    fun `fetch auth_container by token`() {
        // given
        val expected = AuthContainer(
                "9999999",
                "1",
                "2"
        )

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .set(
                        expected.token,
                        String(Jackson2JsonRedisSerializer(AuthContainer::class.java)
                                .serialize(expected))
                )

        // when
        val actual = reactiveAuthContainerRepositoryRedis.findAuthContainerBy(expected.token)

        // then
        StepVerifier.create(actual)
                .expectNext(expected)
                .verifyComplete()
    }

    @Test
    fun `save auth_container data`() {
        // given
        val expected = AuthContainer(
                "1234567890",
                "1",
                "2"
        )

        // when
        val actual = reactiveAuthContainerRepositoryRedis.save(expected)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
    }
}