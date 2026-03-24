package com.kchat.tokens.model

data class JwtConfig(
    val jwtSecret: String,
    val jwtAudience: String,
    val jwtDomain: String,
    val jwtRealm: String
)