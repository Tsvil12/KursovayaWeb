package restaurant.repository

import restaurant.model.Dish
import org.springframework.data.jpa.repository.JpaRepository

interface DishRepository : JpaRepository<Dish, Long> {
    fun findByName(name: String): Dish?
    fun findByCategoryId(categoryId: Long): List<Dish>
    fun findByIsFeaturedTrue(): List<Dish>
}