package com.github.yuizho.chambre.presentation.controller.site

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.*

@RequestMapping
@Controller
class RoomController {
    @GetMapping("/room")
    fun index(model: Model): String {
        //val flux = Flux.range(0, 1).map { "count: $it" }
        //.delayElements(Duration.ofSeconds(1L));
        //model.addAttribute("items", ReactiveDataDriverContextVariable(flux, 1))
        val uuid = UUID.randomUUID().toString()
        model.addAttribute("uuid", Mono.just(uuid))
        model.addAttribute(
                "context",
                ReactiveSecurityContextHolder.getContext()
                        .map { it.authentication.principal }
        )
        return "index.html"
    }
}