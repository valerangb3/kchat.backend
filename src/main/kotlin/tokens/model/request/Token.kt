package com.kchat.tokens.model.request

import kotlinx.serialization.Serializable

@Serializable
data class Token(
    val refreshToken: String
)
