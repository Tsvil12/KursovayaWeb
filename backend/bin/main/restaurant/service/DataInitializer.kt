package restaurant.service

import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import restaurant.model.*
import restaurant.repository.*

@Component
class DataInitializer(
    private val userRepository: UserRepository,
    private val categoryRepository: CategoryRepository,
    private val dishRepository: DishRepository
) : CommandLineRunner {

    private val passwordEncoder = BCryptPasswordEncoder()

    override fun run(vararg args: String?) {
        // Создаём админа
        if (userRepository.findByUsername("admin") == null) {
            val admin = User(
                username = "admin",
                password = passwordEncoder.encode("admin"),
                role = "ADMIN"
            )
            userRepository.save(admin)
            println("Админ создан: admin / admin")
        }

        // Создаём клиента
        if (userRepository.findByUsername("user") == null) {
            val user = User(
                username = "user",
                password = passwordEncoder.encode("user"),
                role = "CLIENT"
            )
            userRepository.save(user)
            println("Клиент создан: user / user")
        }

        // Создаём категории
        val categories = listOf("Супы", "Салаты", "Горячее")
        val categoryEntities = mutableMapOf<String, Category>()
        
        for (catName in categories) {
            val existing = categoryRepository.findByName(catName)
            if (existing == null) {
                val category = Category(name = catName)
                categoryRepository.save(category)
                categoryEntities[catName] = category
                println("Категория создана: $catName")
            } else {
                categoryEntities[catName] = existing
            }
        }

        // Создаём блюда (используем список строк)
        val dishesData = listOf(
            listOf("Борщ", "250.0", "Супы", "Классический борщ со сметаной"),
            listOf("Солянка", "280.0", "Супы", "Мясная сборная с лимоном"),
            listOf("Цезарь", "300.0", "Салаты", "С курицей и пармезаном"),
            listOf("Греческий", "270.0", "Салаты", "С фетой и оливками"),
            listOf("Пельмени", "320.0", "Горячее", "С олениной и сметаной"),
            listOf("Котлеты", "350.0", "Горячее", "По-киевски с картофелем")
        )

        for (dishData in dishesData) {
            val name = dishData[0]
            val price = dishData[1].toDouble()
            val categoryName = dishData[2]
            val description = dishData[3]
            
            val existing = dishRepository.findByName(name)
            if (existing == null) {
                val category = categoryEntities[categoryName]
                val dish = Dish(
                    name = name,
                    price = price,
                    description = description,
                    category = category
                )
                dishRepository.save(dish)
                println("Блюдо создано: $name")
            } else {
                println("Блюдо уже существует: $name")
            }
        }
        
        println("Инициализация данных завершена!")
    }
}