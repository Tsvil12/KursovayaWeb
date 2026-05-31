package restaurant.model

import jakarta.persistence.*

@Entity
@Table(name = "order_items")
class OrderItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    var order: Order? = null,
    
    @ManyToOne
    @JoinColumn(name = "dish_id", nullable = false)
    var dish: Dish? = null,
    
    @Column(nullable = false)
    var quantity: Int = 1,
    
    @Column(nullable = false)
    var priceAtOrder: Double = 0.0
) {
    constructor() : this(0)
}