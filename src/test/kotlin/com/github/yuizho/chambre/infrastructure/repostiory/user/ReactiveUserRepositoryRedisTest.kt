package com.github.yuizho.chambre.infrastructure.repostiory.user

import com.github.yuizho.chambre.RedisConfig
import com.github.yuizho.chambre.domain.user.Role
import com.github.yuizho.chambre.domain.user.Status
import com.github.yuizho.chambre.domain.user.User
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
class ReactiveUserRepositoryRedisTest {
    @Autowired
    lateinit var reactiveUserRepositoryRedis: ReactiveUserRepositoryRedis

    @Container
    val redis = RedisConfig.redis

    @Test
    fun `fetch user by user_id`() {
        // given
        val expected = User("2", "yuizho", Role.ADMIN, Status.AVAILABLE)
        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
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
        val expected = User("1", "yuizho", Role.NORMAL, Status.NEEDS_APPROVAL)

        // when
        val actual = reactiveUserRepositoryRedis.save(expected)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
    }
}