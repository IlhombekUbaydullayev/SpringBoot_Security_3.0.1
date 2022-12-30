package com.example.demo

import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtTokenFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtUtil: JwtUtil

//    @Throws(ServletException::class, IOException::class)
//    override fun doFilterInternal(
//        httpServletRequest: HttpServletRequest,
//        httpServletResponse: HttpServletResponse,
//        filterChain: FilterChain
//    ) {
//
//        val authorizationHeader = httpServletRequest.getHeader("Authorization")
//        if (authorizationHeader == null || authorizationHeader.isEmpty() || !authorizationHeader.startsWith("Bearer")) {
//            filterChain.doFilter(httpServletRequest, httpServletResponse)
//            return
//        }
//        val token =
//            authorizationHeader.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1].trim { it <= ' ' }
//        if (!jwtUtil.validate(token)) {
//            filterChain.doFilter(httpServletRequest, httpServletResponse)
//            return
//        }
//        val username = jwtUtil.getUsername(token)
//        val upassToken = UsernamePasswordAuthenticationToken(username, null, ArrayList())
//        upassToken.details = WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
//        SecurityContextHolder.getContext().authentication = upassToken
//
//        filterChain.doFilter(httpServletRequest, httpServletResponse)
//    }

    @Autowired
    private val userDetailsService: UserDetailsService? = null

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        httpServletRequest: HttpServletRequest,
        httpServletResponse: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authorization = httpServletRequest.getHeader("Authorization")
        if (authorization != null && authorization.startsWith("Bearer")) {
            authorization = authorization.substring(7)
            val email = jwtUtil.getEmailFromToken(authorization)
            if (email != null) {
                val userDetails = userDetailsService?.loadUserByUsername(email)
                val upassToken = UsernamePasswordAuthenticationToken(email, null,userDetails?.authorities)
                upassToken.details = WebAuthenticationDetailsSource().buildDetails(httpServletRequest)
                SecurityContextHolder.getContext().authentication = upassToken
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse)
    }
}