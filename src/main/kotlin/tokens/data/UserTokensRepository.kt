package com.kchat.tokens.data

import com.kchat.tokens.model.UserTokens
import com.kchat.utils.TokenPair
import java.util.UUID

interface UserTokensRepository {
    suspend fun createToken(userTokens: UserTokens)
    suspend fun refreshToken(uuid: UUID): TokenPair?
}