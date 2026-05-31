package restaurant.controller

import org.springframework.web.bind.annotation.*
import restaurant.model.Dish
import restaurant.repository.DishRepository
import restaurant.repository.OrderRepository

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = ["http://localhost:5173"])
class AdminController(
    private val dishRepository: DishRepository,
    private val orderRepository: OrderRepository
) {

    @GetMapping("/dishes")
    fun getAllDishes(): List<Dish> = dishRepository.findAll()

    @PostMapping("/dishes")
    fun addDish(@RequestBody dish: Dish): Map<String, String> {
        println("🚀🚀🚀 ПОЛУЧЕН ЗАПРОС НА ДОБАВЛЕНИЕ БЛЮДА 🚀🚀🚀")
        println("Блюдо: ${dish.name}, цена: ${dish.price}, описание: ${dish.description}")
        
        return try {
            val saved = dishRepository.save(dish)
            println("✅ Блюдо сохранено с ID: ${saved.id}")
            mapOf("status" to "ok", "message" to "Блюдо добавлено")
        } catch (e: Exception) {
            println("❌ Ошибка при сохранении: ${e.message}")
            e.printStackTrace()
            mapOf("status" to "error", "message" to (e.message ?: "Неизвестная ошибка"))
        }
    }

    @PutMapping("/dishes/{id}")
    fun updateDish(@PathVariable id: Long, @RequestBody dish: Dish): Map<String, String> {
        val existing = dishRepository.findById(id).orElse(null)
            ?: return mapOf("status" to "error", "message" to "Блюдо не найдено")
        
        existing.name = dish.name
        existing.price = dish.price
        existing.description = dish.description
        existing.category = dish.category
        
        dishRepository.save(existing)
        return mapOf("status" to "ok", "message" to "Блюдо обновлено")
    }

    @DeleteMapping("/dishes/{id}")
    fun deleteDish(@PathVariable id: Long): Map<String, String> {
        dishRepository.deleteById(id)
        return mapOf("status" to "ok", "message" to "Блюдо удалено")
    }

    @PutMapping("/dishes/{id}/featured")
    fun setFeatured(@PathVariable id: Long, @RequestParam featured: Boolean): Map<String, String> {
        val dish = dishRepository.findById(id).orElse(null)
            ?: return mapOf("status" to "error", "message" to "Блюдо не найдено")
        
        dish.isFeatured = featured
        dishRepository.save(dish)
        return mapOf("status" to "ok", "message" to if (featured) "Блюдо добавлено в избранное" else "Блюдо убрано из избранного")
    }

    @GetMapping("/dishes/featured")
    fun getFeaturedDishes(): List<Dish> = dishRepository.findByIsFeaturedTrue()

    @GetMapping("/orders")
    fun getAllOrders(): List<Map<String, Any?>> {
        val orders = orderRepository.findAll()
        return orders.map { order ->
            mapOf(
                "id" to order.id,
                "date" to order.orderDate.toString(),
                "status" to order.status,
                "totalSum" to order.totalSum,
                "deliveryType" to order.deliveryType,
                "address" to order.address,
                "userName" to order.user?.username,
                "items" to order.items.map { item ->
                    mapOf(
                        "dishName" to item.dish?.name,
                        "quantity" to item.quantity,
                        "price" to item.priceAtOrder
                    )
                }
            )
        }
    }

    @PutMapping("/orders/{id}/status")
    fun updateOrderStatus(@PathVariable id: Long, @RequestParam status: String): Map<String, String> {
        val order = orderRepository.findById(id).orElse(null)
            ?: return mapOf("status" to "error", "message" to "Заказ не найден")
        order.status = status
        orderRepository.save(order)
        return mapOf("status" to "ok", "message" to "Статус обновлён")
    }
}