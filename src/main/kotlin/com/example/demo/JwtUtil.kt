package com.example.demo

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.data.domain.AuditorAware
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import java.security.Key
import java.sql.Date
import java.util.*


@Service
class JwtUtil {
//    fun generate(username: String?): String {
//        return Jwts.builder()
//            .setSubject(username)
//            .setIssuer("backendstory.com")
//            .setIssuedAt(Date(System.currentTimeMillis()))
//            .setExpiration(Date(System.currentTimeMillis() + expireInMs))
//            .signWith(key)
//            .compact()
//    }
//
//    fun validate(token: String?): Boolean {
//        return getUsername(token) != null && isExpired(token)
//    }
//
//    fun getUsername(token: String?): String? {
//        val claims = getClaims(token)
//        return claims.subject
//    }
//
//    fun isExpired(token: String?): Boolean {
//        val claims = getClaims(token)
//        return claims.expiration.after(Date(System.currentTimeMillis()))
//    }
//
//    private fun getClaims(token: String?): Claims {
//        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody()
//    }
//
//    companion object {
//        private const val expireInMs = 300 * 1000
//        private val key: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
//    }

    fun generateToken(username: String?, systemRoleName: CompanyRoleName): String {
        val expireDate = java.util.Date(System.currentTimeMillis() + expireTime)
        return Jwts
            .builder()
            .setSubject(username)
            .setIssuedAt(java.util.Date())
            .setExpiration(expireDate)
            .claim("roles", systemRoleName.name)
            .signWith(SignatureAlgorithm.HS512, secretKey)
            .compact()
    }

    fun getEmailFromToken(token: String?): String? {
        return try {
            Jwts
                .parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val expireTime = (1000 * 60 * 60 * 24).toLong()
        private const val secretKey = "maxfiysuzhechkimbilmasin"
    }
}

class SpringSecurityAuditAwareImpl : AuditorAware<Long> {
    override fun getCurrentAuditor(): Optional<Long> {
        val authentication = SecurityContextHolder.getContext().authentication
        if (!(authentication == null || !authentication.isAuthenticated || "anonymousUser" == "" + authentication.principal)) {
            val uuid = (authentication.principal as User).id
            return Optional.of(uuid!!)
        }
        return Optional.empty()
    }
}