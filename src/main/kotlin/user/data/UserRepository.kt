package com.kchat.user.data

import com.kchat.user.model.User
import com.kchat.user.model.request.AuthCredentials

interface UserRepository {
    suspend fun addUser(user: User)
    suspend fun login(credential: AuthCredentials): LoginResult
    suspend fun registration(credential: AuthCredentials): User
    //suspend fun findByLogin(login: String)
}

sealed interface LoginResult {
    object NotFoundResult : LoginResult
    object FailLoginResult : LoginResult
    class SuccessResult(val user: User) : LoginResult
}