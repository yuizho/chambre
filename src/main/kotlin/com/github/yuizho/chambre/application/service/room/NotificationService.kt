package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.room.Message
import com.github.yuizho.chambre.domain.room.ReactiveEventStreamRepository
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class NotificationService(
        private val reactiveEventStreamRepository: ReactiveEventStreamRepository
) {
    fun notify(roomId: String, user: User): Flux<Message> {
        return reactiveEventStreamRepository.receive(Room.Id.from(roomId))
                .filter { it.to.contains(user) }
                .log()
    }
}