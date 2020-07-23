package com.github.yuizho.chambre

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ChambreApplication

fun main(args: Array<String>) {
    runApplication<ChambreApplication>(*args)
}
