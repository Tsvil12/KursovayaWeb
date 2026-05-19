package restaurant.controller

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/public")
class PublicController {

    @GetMapping("/menu")
    fun getMenu(): Map<String, Any> {
        return mapOf(
            "status" to "ok",
            "dishes" to listOf(
                mapOf("name" to "Борщ", "price" to 250),
                mapOf("name" to "Пельмени", "price" to 320),
                mapOf("name" to "Цезарь", "price" to 280)
            )
        )
    }
}