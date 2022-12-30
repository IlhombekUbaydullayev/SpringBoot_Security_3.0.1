package com.example.demo

import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component

@Component
internal class GoogleCloudAuthenticationProvider : AuthenticationProvider {
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val username = authentication.name
        val password = authentication.credentials.toString()

        val user = getUserFromGoogleCloud(username, password)
        if (user != null) {
            return UsernamePasswordAuthenticationToken(username, password, user.authorities)
        }
        throw BadCredentialsException("Error!!")
    }

    private fun getUserFromGoogleCloud(username: String, password: String): User? {
        val users: MutableMap<String, String?> = HashMap()
        users["ugur"] = "123"
        return if (users[username] != null) {
            User(username, password, emptyList())
        } else null
    }

    override fun supports(authenticationType: Class<*>): Boolean {
        return UsernamePasswordAuthenticationToken::class.java == authenticationType
    }
}