package com.github.yuizho.chambre.domain.auth

import reactor.core.publisher.Mono

interface ReactiveParticipantRepository {
    fun findAuthContainerBy(token: String): Mono<Participant>
    fun save(participant: Participant): Mono<Boolean>
}