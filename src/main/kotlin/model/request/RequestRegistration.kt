package com.kchat.model.request

import kotlinx.serialization.Serializable

@Serializable
data class RequestRegistration(
    val login: String,
    val password: String
)
