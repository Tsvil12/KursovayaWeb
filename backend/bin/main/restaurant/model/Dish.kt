package restaurant.model

import jakarta.persistence.*

@Entity
@Table(name = "dishes")
data class Dish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,
    
    @Column(nullable = false)
    val name: String = "",
    
    @Column(nullable = false)
    val price: Double = 0.0,
    
    @Column(length = 500)
    val description: String = "",
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    val category: Category? = null
)