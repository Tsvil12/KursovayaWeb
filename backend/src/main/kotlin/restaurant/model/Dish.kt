package restaurant.model

import jakarta.persistence.*

@Entity
@Table(name = "dishes")
class Dish(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    
    @Column(nullable = false)
    var name: String = "",
    
    @Column(nullable = false)
    var price: Double = 0.0,
    
    @Column(length = 500)
    var description: String = "",
    
    @ManyToOne
    @JoinColumn(name = "category_id")
    var category: Category? = null,
    
    @Column(nullable = false)
    var isFeatured: Boolean = false
)