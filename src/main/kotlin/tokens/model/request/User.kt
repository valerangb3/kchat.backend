package com.kchat.tokens.model.request

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val login: String
)