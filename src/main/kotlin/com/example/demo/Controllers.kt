package com.example.demo

import lombok.Data
import lombok.NoArgsConstructor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
internal class BasicController(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
){

    @Autowired
    private lateinit var jwtUtil: JwtUtil

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager
    @PostMapping("/logins")
    fun login(@RequestBody request: LoginRequestDTO): ResponseEntity<String> {
        val token = UsernamePasswordAuthenticationToken(request.username, request.password)
        val authenticate = authenticationManager.authenticate(token)
        val user: User = authenticate.principal as User
        val jwt = jwtUtil.generateToken(user.email,user.systemRoleName!!)
        return ResponseEntity.ok(jwt)
    }

    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("api/v1/users")
    fun get(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello")
    }

    @PostMapping("/create/user")
    fun create(@RequestBody user : UserDto): ResponseEntity<String> {
        userRepository.save(User(user.email,passwordEncoder.encode(user.password)))
        return ResponseEntity.ok("Saved")
    }
}


data class LoginRequestDTO(
    val username: String,
    val password: String
)

data class UserDto(
    var email : String,
    var password: String
)