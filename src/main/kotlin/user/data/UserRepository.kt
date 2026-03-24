package com.kchat.user.data

import com.kchat.user.model.User
import com.kchat.user.model.request.RequestRegistration

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun login(user: User): Boolean
    suspend fun registration(credential: RequestRegistration): User
}