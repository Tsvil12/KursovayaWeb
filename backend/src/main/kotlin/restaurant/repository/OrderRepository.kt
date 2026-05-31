package restaurant.repository

import restaurant.model.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface OrderRepository : JpaRepository<Order, Long> {
    fun findByUserId(userId: Long): List<Order>
    
    @Query("SELECT o FROM Order o WHERE o.status = 'PENDING' AND o.orderDate < :time")
    fun findPendingOrdersOlderThan(time: LocalDateTime): List<Order>
}