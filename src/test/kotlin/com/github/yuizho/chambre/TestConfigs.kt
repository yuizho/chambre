package com.github.yuizho.chambre

import org.testcontainers.containers.FixedHostPortGenericContainer
import org.testcontainers.containers.GenericContainer

class RedisConfig {
    companion object {
        const val REDIS_PORT = 26379
        
        val redis: GenericContainer<*> = FixedHostPortGenericContainer<Nothing>("redis:6.0-alpine")
                .withFixedExposedPort(REDIS_PORT, 6379)
    }
}