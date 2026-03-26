package com.kchat.user.data

import com.kchat.user.db.UserDao
import com.kchat.user.db.toUser
import com.kchat.user.model.User
import com.kchat.user.model.request.AuthCredentials
import com.kchat.utils.withTransaction
import org.mindrot.jbcrypt.BCrypt

class PostgresUserRepository : UserRepository {
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

    override suspend fun login(credential: AuthCredentials): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun registration(credential: AuthCredentials) = withTransaction {
        val hash = BCrypt.hashpw(credential.password, BCrypt.gensalt())
        val user = UserDao.new {
            login = credential.login
            passwordHash = hash
            registrationDate = System.currentTimeMillis()
            isActive = false
        }.toUser()
        user
    }
}