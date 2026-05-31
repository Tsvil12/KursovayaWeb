package restaurant.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val orderDate: LocalDateTime = LocalDateTime.now(),
    
    @Column(nullable = false)
    var status: String = "PENDING",  // PENDING, PAID, CANCELLED
    
    @Column(nullable = false)
    var totalSum: Double = 0.0,
    
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    val user: User,
    
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    val items: MutableList<OrderItem> = mutableListOf()
)