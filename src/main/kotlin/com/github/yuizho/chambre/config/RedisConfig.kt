package com.github.yuizho.chambre.config

import com.github.yuizho.chambre.domain.auth.Participant
import com.github.yuizho.chambre.domain.room.Event
import com.github.yuizho.chambre.domain.room.Room
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.stream.MapRecord
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.data.redis.stream.StreamReceiver

@Configuration
@EnableConfigurationProperties(RedisProperties::class)
class RedisConfig {
    @Bean
    fun publishReactiveRedisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Event<*>> {
        return createReactiveRedisOperationsByContext(factory)
    }

    @Bean
    fun roomReactiveRedisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Room> {
        return createReactiveRedisOperationsByContext(factory)
    }

    @Bean
    fun participantReactiveRedisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Participant> {
        return createReactiveRedisOperationsByContext(factory)
    }

    @Bean
    fun streamReceiver(factory: ReactiveRedisConnectionFactory): StreamReceiver<String, MapRecord<String, String, String>> {
        return StreamReceiver.create(factory)
    }

    private inline fun <reified T> createReactiveRedisOperationsByContext(factory: ReactiveRedisConnectionFactory):
            ReactiveRedisOperations<String, T> {
        val context = RedisSerializationContext
                // the serializer of key
                .newSerializationContext<String, T>(StringRedisSerializer())
                // the serializer of value
                .value(Jackson2JsonRedisSerializer(T::class.java))
                .build()
        return ReactiveRedisTemplate(factory, context)
    }
}

@ConfigurationProperties(prefix = "chambre.redis")
@ConstructorBinding
class RedisProperties(
        val expireSec: Long = -1
)
