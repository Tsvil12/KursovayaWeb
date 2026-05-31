package restaurant.controller

import org.springframework.web.bind.annotation.*
import restaurant.repository.DishRepository

@RestController
@RequestMapping("/api/public")
class PublicController(
    private val dishRepository: DishRepository
) {

    @GetMapping("/menu")
    fun getMenu(): Map<String, Any> {
        val dishes = dishRepository.findAll()
        return mapOf(
            "status" to "ok",
            "dishes" to dishes.map { 
                mapOf(
                    "id" to it.id,
                    "name" to it.name,
                    "price" to it.price,
                    "description" to it.description,
                    "category" to it.category?.name
                )
            }
        )
    }
}