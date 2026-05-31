package restaurant.controller

import org.springframework.web.bind.annotation.*
import restaurant.repository.DishRepository

@RestController
@RequestMapping("/api/public")
@CrossOrigin(origins = ["http://localhost:5173"])
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
                    "category" to it.category?.name,
                    "isFeatured" to it.isFeatured
                )
            }
        )
    }

    @GetMapping("/featured")
    fun getFeatured(): Map<String, Any> {
        val dishes = dishRepository.findByIsFeaturedTrue()
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