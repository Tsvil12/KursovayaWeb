package restaurant.controller

import org.springframework.web.bind.annotation.*
import org.springframework.security.crypto.password.PasswordEncoder
import restaurant.repository.UserRepository
import restaurant.security.JwtUtil
import restaurant.model.User

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = ["http://localhost:5173"])
class AuthController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtUtil: JwtUtil
) {

    @PostMapping("/login")
    fun login(@RequestParam username: String, @RequestParam password: String): Map<String, Any> {
        val user = userRepository.findByUsername(username)
        
        return if (user != null && passwordEncoder.matches(password, user.password)) {
            val token = jwtUtil.generateToken(user.username, user.role)
            mapOf(
                "status" to "ok",
                "token" to token,
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

    @PostMapping("/register")
    fun register(@RequestParam username: String, @RequestParam password: String): Map<String, Any> {
        // Проверяем, не существует ли уже пользователь
        val existingUser = userRepository.findByUsername(username)
        if (existingUser != null) {
            return mapOf(
                "status" to "error",
                "message" to "Пользователь с таким именем уже существует"
            )
        }
        
        // Создаём нового пользователя
        val newUser = User(
            username = username,
            password = passwordEncoder.encode(password),
            role = "CLIENT"
        )
        
        val savedUser = userRepository.save(newUser)
        
        // Генерируем токен
        val token = jwtUtil.generateToken(savedUser.username, savedUser.role)
        
        return mapOf(
            "status" to "ok",
            "token" to token,
            "role" to savedUser.role,
            "username" to savedUser.username,
            "userId" to savedUser.id
        )
    }
}