package com.kchat.user.db

import com.kchat.user.model.User
import org.jetbrains.exposed.v1.core.CustomFunction
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.java.UUIDColumnType
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object UserTable : IdTable<UUID>("user") {
    override val id = javaUUID("uuid")
        .defaultExpression(
            CustomFunction(
                "gen_random_uuid",
                UUIDColumnType()
            )
        )
        .entityId()


    val login = varchar("login", 50).uniqueIndex()
    val firstName = varchar("first_name", 100).nullable()
    val lastName = varchar("last_name", 100).nullable()
    val birthDate = long("birth_date").nullable()
    val registrationDate = long("registration_date")
    val passwordHash = varchar("password_hash", 150)
    val phone = varchar("phone", 20).nullable()
    val photoUrl = varchar("photo", 200).nullable()
    val isActive = bool("is_active").default(false)

    override val primaryKey = PrimaryKey(id)
}

class UserDao(uuid: EntityID<UUID>) : UUIDEntity(uuid) {
    companion object : UUIDEntityClass<UserDao>(UserTable)

    var login by UserTable.login
    var firstName by UserTable.firstName
    var lastName by UserTable.lastName
    var birthDate by UserTable.birthDate
    var registrationDate by UserTable.registrationDate
    var passwordHash by UserTable.passwordHash
    var phone by UserTable.phone
    var photoUrl by UserTable.photoUrl
    var isActive by UserTable.isActive
}

fun UserDao.toUser() = User(
    id = this.id.value,
    login = this.login,
    firstName = this.firstName,
    lastName = this.lastName,
    birthDate = this.birthDate,
    registrationDate = this.registrationDate,
    passwordHash = this.passwordHash,
    phone = this.phone,
    photo = this.photoUrl
)