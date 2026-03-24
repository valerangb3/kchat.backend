package com.kchat.tokens.data

import com.kchat.tokens.model.UserTokens

interface UserTokensRepository {
    suspend fun create(userTokens: UserTokens)
    suspend fun refreshToken(token: String): String
}