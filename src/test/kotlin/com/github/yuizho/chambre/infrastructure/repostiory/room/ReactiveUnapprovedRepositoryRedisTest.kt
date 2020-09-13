package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.yuizho.chambre.RedisConfig
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.UnapprovedUser
import com.github.yuizho.chambre.domain.room.User
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
    fun `fetch unapprovedUser`() {
        // given
        val expected = UnapprovedUser(User.Id("1"), "userA")
        val roomId = Room.Id.from("1")

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .hset(
                        UnapprovedUser.createSchemaPrefix(roomId),
                        mapOf(expected.id.value to ObjectMapper().writeValueAsString(expected))
                )

        // when
        val actual = reactiveUnapprovedUserRepositoryRedis.findUnapprovedUserBy(roomId, expected.id)

        // then
        StepVerifier.create(actual)
                .expectNext(expected)
                .verifyComplete()
    }

    @Test
    fun `save unapprovedUser`() {
        // given
        val expected = UnapprovedUser(User.Id("1"), "userB")
        val roomId = Room.Id.from("2")

        // when
        val actual = reactiveUnapprovedUserRepositoryRedis.put(roomId, expected)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
    }

    @Test
    fun `fetch unapprovedUsers`() {
        // given
        val expected = listOf(
                UnapprovedUser(User.Id("1"), "userA"), UnapprovedUser(User.Id("2"), "userB")
        )
        val roomId = Room.Id.from("3")

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .hset(
                        UnapprovedUser.createSchemaPrefix(roomId),
                        mapOf(
                                expected[0].id.value to ObjectMapper().writeValueAsString(expected[0]),
                                expected[1].id.value to ObjectMapper().writeValueAsString(expected[1])
                        )
                )

        // when
        val actual = reactiveUnapprovedUserRepositoryRedis.findUnapprovedUsers(roomId)

        // then
        StepVerifier.create(actual)
                .expectNext(expected[0])
                .expectNext(expected[1])
                .verifyComplete()
    }

    @Test
    fun `contains returns true when the date is registered`() {
        // given
        val expected = UnapprovedUser(User.Id("1"), "userA")
        val roomId = Room.Id.from("4")

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .hset(
                        UnapprovedUser.createSchemaPrefix(roomId),
                        mapOf(expected.id.value to ObjectMapper().writeValueAsString(expected))
                )

        // when
        val actual = reactiveUnapprovedUserRepositoryRedis.contains(roomId, expected.id)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
    }

    @Test
    fun `remove unapprovedUser`() {
        // given
        val expected = UnapprovedUser(User.Id("1"), "userA")
        val roomId = Room.Id.from("5")

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .hset(
                        UnapprovedUser.createSchemaPrefix(roomId),
                        mapOf(expected.id.value to ObjectMapper().writeValueAsString(expected))
                )

        // when
        val actual = reactiveUnapprovedUserRepositoryRedis.remove(roomId, expected.id)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
        val registeredValue = Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .hget(UnapprovedUser.createSchemaPrefix(roomId), expected.id.value)
        assertThat(registeredValue).isNull()
    }
}