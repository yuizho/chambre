package com.github.yuizho.chambre.config

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
class ServerPushConfig {
    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisOperations<String, Message> {
        val context = RedisSerializationContext
                // the serializer of key
                .newSerializationContext<String, Message>(StringRedisSerializer())
                // the serializer of value
                .value(Jackson2JsonRedisSerializer(Message::class.java))
                .build()
        return ReactiveRedisTemplate(factory, context)
    }

    @Bean
    fun streamReceiver(factory: ReactiveRedisConnectionFactory): StreamReceiver<String, MapRecord<String, String, String>> {
        return StreamReceiver.create(factory)
    }
}