package com.kchat.tokens.model

import java.util.UUID

data class UserTokens(
    val userUUID: UUID,
    val refreshToken: String,
    val expiresAt: Long
)
