package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.room.Message
import com.github.yuizho.chambre.domain.room.ReactiveEventStreamRepository
import com.github.yuizho.chambre.domain.room.ReactiveUnapprovedEventStreamRepository
import com.github.yuizho.chambre.domain.room.Room
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

    fun notify(roomId: String, userId: String): Flux<Message> {
        return reactiveEventStreamRepository.receive(Room.Id.from(roomId))
                .filter { room ->
                    room.to.any { user -> user.id == userId }
                }
                .log()
    }
}