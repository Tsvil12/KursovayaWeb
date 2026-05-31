package restaurant.controller

import org.springframework.web.bind.annotation.*
import restaurant.model.Dish
import restaurant.repository.DishRepository

@RestController
@RequestMapping("/api/admin")
class AdminController(
    private val dishRepository: DishRepository
) {

    // Получить все блюда (для админки)
    @GetMapping("/dishes")
    fun getAllDishes(): List<Dish> = dishRepository.findAll()

    // Обновить блюдо
    @PutMapping("/dishes/{id}")
    fun updateDish(@PathVariable id: Long, @RequestBody dish: Dish): Map<String, Any> {
        val existing = dishRepository.findById(id).orElse(null)
        if (existing == null) {
            return mapOf("status" to "error", "message" to "Блюдо не найдено")
        }
        val updated = existing.copy(
            name = dish.name,
            price = dish.price,
            description = dish.description,
            category = dish.category
        )
        dishRepository.save(updated)
        return mapOf("status" to "ok", "message" to "Блюдо обновлено")
    }

    // Добавить новое блюдо
    @PostMapping("/dishes")
    fun addDish(@RequestBody dish: Dish): Map<String, Any> {
        dishRepository.save(dish)
        return mapOf("status" to "ok", "message" to "Блюдо добавлено", "id" to dish.id)
    }

    // Удалить блюдо
    @DeleteMapping("/dishes/{id}")
    fun deleteDish(@PathVariable id: Long): Map<String, Any> {
        dishRepository.deleteById(id)
        return mapOf("status" to "ok", "message" to "Блюдо удалено")
    }
}