package com.github.yuizho.chambre.application.service.room

import com.github.yuizho.chambre.domain.room.ReactiveRoomRepository
import com.github.yuizho.chambre.domain.room.Room
import com.github.yuizho.chambre.domain.room.User
import com.github.yuizho.chambre.exception.BusinessException
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class RoomService(
        private val reactiveRoomRepository: ReactiveRoomRepository
) {
    fun users(id: Room.Id): Mono<List<User>> {
        return reactiveRoomRepository.findRoomBy(id)
                .switchIfEmpty(Mono.error(BusinessException("invalid room id.")))
                .map { room ->
                    room.users.toList().sortedBy { it.name }
                }
    }
}