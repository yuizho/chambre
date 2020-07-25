package com.github.yuizho.chambre.config

import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.presentation.dto.Message
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
class RedisConfig {
    @Bean
    fun messageReactiveRedisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Message> {
        return createReactiveRedisOperationsByContext(factory)
    }

    @Bean
    fun roomReactiveRedisOperations(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Room> {
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