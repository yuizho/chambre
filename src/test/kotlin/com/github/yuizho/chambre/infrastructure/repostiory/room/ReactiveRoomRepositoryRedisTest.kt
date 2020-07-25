package com.github.yuizho.chambre.infrastructure.repostiory.room

import com.github.yuizho.chambre.RedisConfig
import com.github.yuizho.chambre.domain.room.Role
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.Status
import com.github.yuizho.chambre.domain.room.User
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
class ReactiveRoomRepositoryRedisTest {
    @Autowired
    lateinit var reactiveRoomRepositoryRedis: ReactiveRoomRepositoryRedis

    @Container
    val redis = RedisConfig.redis

    @Test
    fun `fetch room by room_id`() {
        // given
        val expected = Room(
                "1",
                Room.Status.OPEN,
                listOf(
                        User("1", "foo", Role.ADMIN, Status.AVAILABLE),
                        User("2", "bar", Role.NORMAL, Status.AVAILABLE)
                )
        )

        Jedis(redis.getHost(), RedisConfig.REDIS_PORT)
                .set(
                        expected.id,
                        String(Jackson2JsonRedisSerializer(Room::class.java)
                                .serialize(expected))
                )

        // when
        val actual = reactiveRoomRepositoryRedis.findRoomBy(expected.id)

        // then
        StepVerifier.create(actual)
                .expectNext(expected)
                .verifyComplete()
    }

    @Test
    fun `save room data`() {
        // given
        val expected = Room(
                "1",
                Room.Status.OPEN,
                listOf(
                        User("1", "foo", Role.ADMIN, Status.AVAILABLE),
                        User("2", "bar", Role.NORMAL, Status.AVAILABLE)
                )
        )

        // when
        val actual = reactiveRoomRepositoryRedis.save(expected)

        // then
        StepVerifier.create(actual)
                .expectNext(true)
                .verifyComplete()
    }
}