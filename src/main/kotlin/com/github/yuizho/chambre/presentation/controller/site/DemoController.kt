package com.github.yuizho.chambre.presentation.controller.site

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/demo")
@Controller
class DemoController {
    @GetMapping("/gm")
    fun gm(model: Model): String {
        model.addAttribute(
                "context",
                ReactiveSecurityContextHolder.getContext()
                        .map { it.authentication.principal }
        )
        return "demo/gm"
    }

    @GetMapping("/user")
    fun user(model: Model): String {
        model.addAttribute(
                "context",
                ReactiveSecurityContextHolder.getContext()
                        .map { it.authentication.principal }
        )
        return "demo/user"
    }
}