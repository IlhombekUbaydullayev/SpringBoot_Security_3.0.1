package com.example.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
internal class JwtAuthenticationProvider : AuthenticationProvider {
    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()
        val userDetails = userDetailsService?.loadUserByUsername(username)

        if (userDetails != null) {
            if (passwordEncoder.matches(password, userDetails.password)) {

                return UsernamePasswordAuthenticationToken(username, password, userDetails.authorities)
            }
        }
        throw BadCredentialsException("Error!!")
    }

    override fun supports(authenticationType: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java == authenticationType
    }
}