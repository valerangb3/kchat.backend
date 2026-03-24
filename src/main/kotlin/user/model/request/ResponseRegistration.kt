package com.kchat.user.model.request

import com.kchat.user.model.User
import kotlinx.serialization.Serializable

@Serializable
data class ResponseRegistration(
    val message: String,
    val data: AuthResponse? = null,
    val code: Int,
)

@Serializable
data class ResponseUser(
    val login: String,
    val registrationDate: Long,

    val firstName: String? = null,
    val lastName: String? = null,
    val birthDate: Long? = null,
    val phone: String? = null,
    val photo: String? = null,
)

@Serializable
data class AuthResponse(
    val accessToken: String,
    val accessExpiresAt: Long,
    val refreshToken: String,
    val user: ResponseUser
)

fun User.toResponseUser() = ResponseUser(
    login = this.login,
    registrationDate = this.registrationDate,
    firstName = this.firstName,
    lastName = this.lastName,
    birthDate = this.birthDate,
    phone = this.phone,
    photo = this.photo
)