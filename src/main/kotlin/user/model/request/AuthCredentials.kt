package com.kchat.user.model.request

import kotlinx.serialization.Serializable

@Serializable
data class AuthCredentials(
    val login: String,
    val password: String
)
