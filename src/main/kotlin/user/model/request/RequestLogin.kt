package com.kchat.user.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestLogin(
    val login: String,
    val password: String
)