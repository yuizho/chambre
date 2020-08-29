package com.github.yuizho.chambre.domain.auth

import reactor.core.publisher.Mono

interface ReactiveParticipantRepository {
    fun findAuthContainerBy(id: Participant.Id): Mono<Participant>
    fun save(participant: Participant): Mono<Boolean>
}