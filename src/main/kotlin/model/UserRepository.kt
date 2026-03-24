package com.kchat.model

import com.kchat.model.request.RequestRegistration

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun login(user: User): Boolean
    suspend fun registration(credential: RequestRegistration): User
    suspend fun refreshToken(token: String): String
}