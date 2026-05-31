package restaurant.controller

import org.springframework.web.bind.annotation.*
import restaurant.repository.DishRepository
import restaurant.repository.OrderRepository
import restaurant.model.Order
import restaurant.model.OrderItem
import restaurant.repository.UserRepository
import java.time.LocalDateTime
import jakarta.transaction.Transactional

@RestController
@RequestMapping("/api/client")
@CrossOrigin(origins = ["http://localhost:5173"])
class ClientController(
    private val orderRepository: OrderRepository,
    private val dishRepository: DishRepository,
    private val userRepository: UserRepository
) {

    @PostMapping("/orders")
    @Transactional
    fun createOrder(
        @RequestParam userId: Long,
        @RequestBody items: List<OrderItemRequest>
    ): Map<String, Any> {
        val user = userRepository.findById(userId).orElse(null)
            ?: return mapOf("status" to "error", "message" to "Пользователь не найден")

        var totalSum = 0.0
        val orderItems = mutableListOf<OrderItem>()

        for (item in items) {
            val dish = dishRepository.findById(item.dishId).orElse(null)
                ?: return mapOf("status" to "error", "message" to "Блюдо не найдено: ${item.dishId}")

            totalSum += dish.price * item.quantity
            
            val orderItem = OrderItem()
            orderItem.dish = dish
            orderItem.quantity = item.quantity
            orderItem.priceAtOrder = dish.price
            
            orderItems.add(orderItem)
        }

        val order = Order()
        order.user = user
        order.orderDate = LocalDateTime.now()
        order.status = "PENDING"
        order.totalSum = totalSum
        order.items = orderItems.toMutableList()

        for (item in orderItems) {
            item.order = order
        }

        val savedOrder = orderRepository.save(order)
        
        return mapOf(
            "status" to "ok", 
            "orderId" to savedOrder.id, 
            "totalSum" to totalSum
        )
    }

    @GetMapping("/orders")
    fun getOrders(@RequestParam userId: Long): List<Map<String, Any>> {
        val orders = orderRepository.findByUserId(userId)
        return orders.map { order ->
            mapOf(
                "id" to order.id,
                "date" to order.orderDate.toString(),
                "status" to order.status,
                "totalSum" to order.totalSum,
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
}

data class OrderItemRequest(
    val dishId: Long,
    val quantity: Int
)