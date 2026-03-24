package com.kchat.tokens.data

import com.kchat.tokens.db.UserTokensDao
import com.kchat.tokens.db.toUserTokens
import com.kchat.tokens.model.UserTokens
import com.kchat.utils.withTransaction

class PostgresUserTokensRepository : UserTokensRepository {
    override suspend fun create(userTokens: UserTokens) {
        withTransaction {
            UserTokensDao.new {
                userUuid = userTokens.userUUID
                refreshToken = userTokens.refreshToken
                expiresAt = userTokens.expiresAt
            }.toUserTokens()
        }
    }

    override suspend fun refreshToken(token: String): String {
        TODO("Not yet implemented")
    }
}