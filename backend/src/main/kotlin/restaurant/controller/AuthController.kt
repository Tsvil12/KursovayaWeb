package restaurant.controller

import org.springframework.web.bind.annotation.*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import restaurant.repository.UserRepository

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["http://localhost:5173"])
class AuthController(
    private val userRepository: UserRepository
) {

    private val passwordEncoder = BCryptPasswordEncoder()

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): Map<String, Any> {
        val user = userRepository.findByUsername(username)
        
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            mapOf(
                "status" to "ok",
                "message" to "Вход выполнен",
                "role" to user.role,
                "username" to user.username,
                "userId" to user.id
            )
        } else {
            mapOf(
                "status" to "error",
                "message" to "Неверный логин или пароль"
            )
        }
    }
}