package com.kchat.tokens.data

import com.kchat.tokens.db.UserTokensDao
import com.kchat.tokens.db.UserTokensTable
import com.kchat.tokens.db.toUserTokens
import com.kchat.tokens.model.UserTokens
import com.kchat.utils.TokenManager
import com.kchat.utils.TokenPair
import com.kchat.utils.withTransaction
import org.jetbrains.exposed.v1.core.eq
import java.util.UUID

class PostgresUserTokensRepository(
    private val tokenManager: TokenManager
) : UserTokensRepository {
    override suspend fun createToken(userTokens: UserTokens) {
        withTransaction {
            UserTokensDao.new {
                userUuid = userTokens.userUUID
                refreshToken = userTokens.refreshToken
                expiresAt = userTokens.refreshTokenExpiresAt
            }.toUserTokens()
        }
    }

    override suspend fun refreshToken(uuid: UUID): TokenPair? = withTransaction {
        val userTokens = UserTokensDao.find {
            UserTokensTable.userUuid eq uuid
        }.firstOrNull()
        if (userTokens == null) return@withTransaction null
        val user = userTokens.userRef
        val userLogin = user.login
        val tokensInfo = tokenManager.generateTokens(userLogin, uuid)
        userTokens.expiresAt = tokensInfo.refreshExpire
        userTokens.refreshToken = tokensInfo.refreshToken
        tokensInfo
    }
}