package com.github.yuizho.chambre.infrastructure.repostiory.user

import com.github.yuizho.chambre.domain.user.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier
import redis.clients.jedis.Jedis

@Testcontainers
@SpringBootTest
class ReactiveUserRepositoryRedisTest {

    companion object {
        const val REDIS_PORT = 26379
    }

    @Autowired
    lateinit var reactiveUserRepositoryRedis: ReactiveUserRepositoryRedis

    @Container
    val redis: GenericContainer<*> = FixedHostPortGenericContainer<Nothing>("redis:6.0-alpine")
            .withFixedExposedPort(REDIS_PORT, 6379)

    @Test
    fun `fetch user by user_id`() {
        // given
        val expected = User("2", "yuizho")
        Jedis(redis.getHost(), REDIS_PORT)
                .set(
                        expected.id,
                        String(Jackson2JsonRedisSerializer(User::class.java)
                                .serialize(expected))
                )

        // when
        val actual = reactiveUserRepositoryRedis.findUserBy(expected.id)

        // then
        StepVerifier.create(actual)
                .expectNext(expected)
                .verifyComplete()
    }

    @Test
    fun `save user data`() {
        // given
        val expected = User("1", "yuizho")

        // when
        val actual = reactiveUserRepositoryRedis.save(expected)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
    }
}