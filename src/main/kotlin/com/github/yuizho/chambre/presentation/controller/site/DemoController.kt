package com.github.yuizho.chambre.presentation.controller.site

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import reactor.core.publisher.Mono
import java.util.*

@RequestMapping("/demo")
@Controller
class DemoController {
    @GetMapping("/gm")
    fun gm(model: Model): String {
        val uuid = UUID.randomUUID().toString()
        model.addAttribute("uuid", Mono.just(uuid))
        model.addAttribute(
                "context",
                ReactiveSecurityContextHolder.getContext()
                        .map { it.authentication.principal }
        )
        return "demo/gm"
    }

    @GetMapping("/user")
    fun user(model: Model): String {
        val uuid = UUID.randomUUID().toString()
        model.addAttribute("uuid", Mono.just(uuid))
        return "demo/user"
    }
}