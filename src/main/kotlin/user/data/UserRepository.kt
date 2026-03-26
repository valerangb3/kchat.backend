package com.kchat.user.data

import com.kchat.user.model.User
import com.kchat.user.model.request.AuthCredentials

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun login(credential: AuthCredentials): Boolean
    suspend fun registration(credential: AuthCredentials): User
}