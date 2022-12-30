package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
internal class UserDetailsService(
    private val userRepository: UserRepository
): org.springframework.security.core.userdetails.UserDetailsService {
    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

//    @Throws(UsernameNotFoundException::class)
//    override fun loadUserByUsername(username: String): UserDetails {
//
//        val users: MutableMap<String, String> = HashMap()
//        val email = userRepository.findByEmailAndDeletedFalse(username)?.email
//        users["email"] = passwordEncoder.encode("123")
//        if (users.containsKey(username)) return Us(
//            username,
//            users[username], ArrayList()
//        )
//        throw UsernameNotFoundException(username)
//    }
@Throws(UsernameNotFoundException::class)
override fun loadUserByUsername(username: String): UserDetails {
    return userRepository.findByEmailAndDeletedFalse(username)
        ?: throw UsernameNotFoundException("$username topilmadi")
}
}
