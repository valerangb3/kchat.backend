package com.kchat.model

import java.util.UUID

data class User(
    val id: UUID,
    val login: String,
    val firstName: String,
    val lastName: String,
    val birthDate: Long?,
    val registrationDate: Long,
    val passwordHash: String,
    val phone: String?,
    val photo: String?
)