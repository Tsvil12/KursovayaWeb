package restaurant.model

import jakarta.persistence.*

@Entity
@Table(name = "order_items")
data class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    val order: Order,
    
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    val dish: Dish,
    
    @Column(nullable = false)
    val quantity: Int = 1,
    
    @Column(nullable = false)
    val priceAtOrder: Double = 0.0  // цена на момент заказа
)