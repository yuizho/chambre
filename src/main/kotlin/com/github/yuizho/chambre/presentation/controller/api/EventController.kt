package com.github.yuizho.chambre.presentation.controller.api

import com.github.yuizho.chambre.application.service.room.EventService
import com.github.yuizho.chambre.presentation.controller.api.dto.EntryParameter
import com.github.yuizho.chambre.presentation.controller.api.dto.EntryResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import javax.validation.Valid

@RequestMapping("/event")
@RestController
class EventController(
        private val eventService: EventService
) {
    @PostMapping("/entry")
    fun entry(@RequestBody @Valid param: EntryParameter): Mono<EntryResponse> {
        return eventService.entry(
                param.roomId,
                param.userId,
                param.userName
        ).then(Mono.just(EntryResponse()))
    }
}