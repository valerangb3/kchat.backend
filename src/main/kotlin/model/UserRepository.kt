package com.kchat.model

interface UserRepository {
    suspend fun addUser(user: User)
}