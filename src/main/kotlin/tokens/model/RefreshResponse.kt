package com.kchat.tokens.model

import kotlinx.serialization.Serializable

@Serializable
data class Tokens(
    val accessToken: String,
    val refreshToken: String,
)

@Serializable
data class RefreshResponse(
    val tokens: Tokens? = null,
    val code: Int,
    val message: String
)
