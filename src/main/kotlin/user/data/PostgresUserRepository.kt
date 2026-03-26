package com.kchat.user.data

import com.kchat.tokens.data.UserTokensRepository
import com.kchat.user.db.UserDao
import com.kchat.user.db.UserTable
import com.kchat.user.db.toUser
import com.kchat.user.model.User
import com.kchat.user.model.request.AuthCredentials
import com.kchat.utils.withTransaction
import org.jetbrains.exposed.v1.core.eq
import org.mindrot.jbcrypt.BCrypt

class PostgresUserRepository : UserRepository {

    private fun checkPassword(pass: String, hash: String): Boolean {
        return BCrypt.checkpw(pass, hash)
    }

    override suspend fun addUser(user: User) {
        withTransaction {
            UserDao.new {
                login = user.login
                firstName = user.firstName ?: ""
                lastName = user.lastName ?: ""
                birthDate = user.birthDate ?: -1
                registrationDate = user.registrationDate
                passwordHash = user.passwordHash
                phone = user.phone ?: ""
                photoUrl = user.photo ?: ""
            }
        }
    }

    override suspend fun login(credential: AuthCredentials): LoginResult = withTransaction {
        val userDao = UserDao.find { UserTable.login eq credential.login }
            .limit(1)
            .firstOrNull()
        if (userDao != null) {
            if (checkPassword(credential.password, userDao.passwordHash)) {
                LoginResult.SuccessResult(userDao.toUser())
            } else {
                LoginResult.FailLoginResult
            }
        } else
            LoginResult.NotFoundResult
    }

    override suspend fun registration(credential: AuthCredentials) = withTransaction {
        val hash = BCrypt.hashpw(credential.password, BCrypt.gensalt())
        UserDao.new {
            login = credential.login
            passwordHash = hash
            registrationDate = System.currentTimeMillis()
            isActive = false
        }.toUser()
    }
}