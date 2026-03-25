package com.kchat.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kchat.tokens.model.JwtConfig
import java.util.Date
import java.util.UUID


class TokenManager(private val jwtConfig: JwtConfig) {
    fun generateTokens(userLogin: String, userUUID: UUID): TokenPair {
        val now = System.currentTimeMillis()
        val accessLifetime = 15 * 60 * 1000L
        val refreshLifetime = 30L * 24 * 60 * 60 * 1000

        val accessExpire = now + accessLifetime
        val refreshExpire = now + refreshLifetime

        val accessToken = JWT.create()
            .withSubject(SUBJECT)
            .withIssuer(jwtConfig.jwtDomain)
            .withAudience(jwtConfig.jwtAudience)
            .withClaim("userLogin", userLogin)
            .withClaim("uuid", userUUID.toString())
            .withExpiresAt(Date(accessExpire))
            .sign(Algorithm.HMAC256(jwtConfig.jwtSecret))

        val refreshToken = JWT.create()
            .withSubject(SUBJECT)
            .withIssuer(jwtConfig.jwtDomain)
            .withAudience(jwtConfig.jwtAudience)
            .withClaim("userLogin", userLogin)
            .withClaim("uuid", userUUID.toString())
            .withExpiresAt(Date(refreshExpire))
            .sign(Algorithm.HMAC256(jwtConfig.jwtSecret))

        return TokenPair(
            accessToken,
            accessExpire,
            refreshToken,
            refreshExpire
        )
    }

    companion object {
        private const val SUBJECT = "Authentication"
    }
}

data class TokenPair(
    val accessToken: String,
    val accessExpire: Long,
    val refreshToken: String,
    val refreshExpire: Long
)