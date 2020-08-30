package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.RedisConfig
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.UnapprovedUser
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import reactor.test.StepVerifier
import redis.clients.jedis.Jedis

@Testcontainers
@SpringBootTest
class ReactiveUnapprovedRepositoryRedisTest {
    @Autowired
    lateinit var reactiveUnapprovedUserRepositoryRedis: ReactiveUnapprovedUserRepositoryRedis

    @Container
    val redis = RedisConfig.redis

    @Test
    fun `fetch unapprovedUser by room_id, user_id`() {
        // given
        val expected = UnapprovedUser("1", "userA")
        val roomId = Room.Id.from("1")

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .hset(
                        UnapprovedUser.createSchemaPrefix(roomId),
                        mapOf(expected.id to ObjectMapper().writeValueAsString(expected))
                )

        // when
        val actual = reactiveUnapprovedUserRepositoryRedis.findUnapprovedUserBy(roomId, expected.id)

        // then
        StepVerifier.create(actual)
                .expectNext(expected)
                .verifyComplete()
    }

    @Test
    fun `save unapprovedUser data`() {
        // given
        val expected = UnapprovedUser("1", "userB")
        val roomId = Room.Id.from("2")

        // when
        val actual = reactiveUnapprovedUserRepositoryRedis.put(roomId, expected)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()

        val registeredValue = Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .hget(UnapprovedUser.createSchemaPrefix(roomId), expected.id)
        assertThat(
                ObjectMapper().readValue(registeredValue, UnapprovedUser::class.java)
        ).isEqualTo(expected)
    }
}