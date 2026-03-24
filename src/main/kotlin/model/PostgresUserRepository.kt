package com.kchat.model

import com.kchat.model.db.UserDao
import com.kchat.utils.withTransaction

class PostgresUserRepository : UserRepository {
    override suspend fun addUser(user: User) {
        withTransaction {
            UserDao.new {
                login = user.login
                firstName = user.firstName
                lastName = user.lastName
                birthDate = user.birthDate ?: -1
                registrationDate = user.registrationDate
                passwordHash = user.passwordHash
                phone = user.phone ?: ""
                photoUrl = user.photo ?: ""
            }
        }
    }
}