package com.kchat.model.db

import com.kchat.model.User
import org.jetbrains.exposed.v1.core.CustomFunction
import org.jetbrains.exposed.v1.core.Expression
import org.jetbrains.exposed.v1.core.castTo
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.core.java.UUIDColumnType
import org.jetbrains.exposed.v1.core.java.javaUUID
import org.jetbrains.exposed.v1.core.wrap
import org.jetbrains.exposed.v1.dao.java.UUIDEntity
import org.jetbrains.exposed.v1.dao.java.UUIDEntityClass
import java.util.UUID

object UserTable : IdTable<UUID>("user") {
    override val id = javaUUID("uuid") //TODO проблема была в том, что неправильно сопоставлены поля
        .defaultExpression(
            CustomFunction(
                "gen_random_uuid",
                UUIDColumnType()
            ).castTo(UUIDColumnType()) //TODO скорее всего этот каст не нужен
        )
        .entityId()


    val login = varchar("login", 50).uniqueIndex()
    val firstName = varchar("firstName", 100)
    val lastName = varchar("lastName", 100)
    val birthDate = long("birthDate")
    val registrationDate = long("registrationDate")
    val passwordHash = varchar("passwordHash", 150)
    val phone = varchar("phone", 20)
    val photoUrl = varchar("photo", 200)

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
}

fun daoToModel(dao: UserDao) = User(
    id = dao.id.value,
    login = dao.login,
    firstName = dao.firstName,
    lastName = dao.lastName,
    birthDate = dao.birthDate,
    registrationDate = dao.registrationDate,
    passwordHash = dao.passwordHash,
    phone = dao.phone,
    photo = dao.photoUrl
)