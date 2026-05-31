package restaurant.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    
    @Column(nullable = false)
    var orderDate: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var status: String = "PENDING",
    
    @Column(nullable = false)
    var totalSum: Double = 0.0,
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User? = null,
    
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var items: MutableList<OrderItem> = mutableListOf()
) {
    constructor() : this(0)
}