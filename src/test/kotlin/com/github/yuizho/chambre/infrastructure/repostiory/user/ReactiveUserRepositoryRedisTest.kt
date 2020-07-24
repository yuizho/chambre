package com.github.yuizho.chambre.infrastructure.repostiory.user

import com.github.yuizho.chambre.domain.user.User
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import reactor.test.StepVerifier
import redis.clients.jedis.Jedis

@SpringBootTest
class ReactiveUserRepositoryRedisTest {

    @Autowired
    lateinit var reactiveUserRepositoryRedis: ReactiveUserRepositoryRedis

    val jedis = Jedis("localhost", 6379)

    @Test
    fun `fetch user by user_id`() {
        // given
        val expected = User("2", "yuizho")
        jedis.set(
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