@PostMapping("/orders")
@Transactional
fun createOrder(
    @RequestParam userId: Long,
    @RequestParam deliveryType: String,  // САМОВЫВОЗ или ДОСТАВКА
    @RequestParam(required = false) address: String?,
    @RequestBody items: List<OrderItemRequest>
): Map<String, Any> {
    val user = userRepository.findById(userId).orElse(null)
        ?: return mapOf("status" to "error", "message" to "Пользователь не найден")
    
    if (deliveryType == "ДОСТАВКА" && address.isNullOrBlank()) {
        return mapOf("status" to "error", "message" to "Для доставки нужен адрес")
    }

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
    order.deliveryType = deliveryType
    order.address = if (deliveryType == "ДОСТАВКА") address else null
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