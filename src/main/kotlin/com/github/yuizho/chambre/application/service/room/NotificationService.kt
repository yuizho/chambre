package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.room.*
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux

@Service
class NotificationService(
        private val reactiveEventStreamRepository: ReactiveEventStreamRepository,
        private val reactiveUnapprovedEventStreamRepository: ReactiveUnapprovedEventStreamRepository
) {
    fun unapprovedNotify(roomId: String, userId: String): Flux<Message> {
        return reactiveUnapprovedEventStreamRepository.receive(Room.Id.from(roomId))
                .filter { room ->
                    room.to.any { user -> user.id == userId }
                }
                .log()
    }

    fun notify(roomId: String, user: User): Flux<Message> {
        return reactiveEventStreamRepository.receive(Room.Id.from(roomId))
                .filter { it.to.contains(user) }
                .log()
    }
}