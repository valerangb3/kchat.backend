package com.kchat.tokens.db

import com.kchat.tokens.model.UserTokens
import com.kchat.user.db.UserDao
import com.kchat.user.db.UserTable
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IntIdTable
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.dao.IntEntity
import org.jetbrains.exposed.v1.dao.IntEntityClass

object UserTokensTable : IntIdTable("user_tokens") {
    val userUuid = javaUUID("user_uuid")
        .references(UserTable.id)
    val refreshToken = varchar("refresh_token", 300)
    val expiresAt = long("expires_at")
}

class UserTokensDao(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<UserTokensDao>(UserTokensTable)

    val userRef by UserDao referencedOn UserTokensTable.userUuid
    var userUuid by UserTokensTable.userUuid
    var refreshToken by UserTokensTable.refreshToken
    var expiresAt by UserTokensTable.expiresAt
}

fun UserTokensDao.toUserTokens() = UserTokens(
    userUUID = this.userUuid,
    refreshToken = this.refreshToken,
    refreshTokenExpiresAt = this.expiresAt
)