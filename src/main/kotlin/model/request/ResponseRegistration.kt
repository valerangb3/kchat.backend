package com.kchat.model.request

import kotlinx.serialization.Serializable

@Serializable
data class ResponseRegistration(
    val data: String,
    val code: Int
)
