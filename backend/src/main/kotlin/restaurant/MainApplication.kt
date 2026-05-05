package com.restaurant

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.bind.annotation.*
import org.springframework.stereotype.*

@SpringBootApplication
class MainApplication

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
}

@RestController
class SimpleController {
    
    @GetMapping("/api/public/menu")
    fun getMenu(): Map<String, Any> {
        return mapOf(
            "status" to "ok",
            "message" to "Сервер работает!",
            "dishes" to listOf(
                mapOf("name" to "Борщ", "price" to 250),
                mapOf("name" to "Пельмени", "price" to 320),
                mapOf("name" to "Цезарь", "price" to 280)
            )
        )
    }
    
    @GetMapping("/")
    fun home(): String {
        return "Сервер Паб Русский работает!"
    }
}