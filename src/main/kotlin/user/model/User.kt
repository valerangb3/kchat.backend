package com.kchat.user.model

import com.kchat.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID? = null,
    val login: String,
    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: Long? = null,
    val registrationDate: Long,
    val passwordHash: String,
    val phone: String? = null,
    val photo: String? = null
)