package com.github.yuizho.chambre.presentation.controller.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@RequestMapping("/api/csrf")
@RestController
class CsrfController {
    /**
     * An endpoint to set csrf token to cookie.
     * The cookie is set by Filter.
     */
    @GetMapping
    fun csrf(serverWebExchange: ServerWebExchange): Mono<Void> {
        return Mono.empty()
    }
}