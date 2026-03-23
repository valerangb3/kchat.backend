package com.kchat.model.db

import org.jetbrains.exposed.v1.core.dao.id.IdTable
import org.jetbrains.exposed.v1.core.dao.id.java.UUIDTable
import org.jetbrains.exposed.v1.core.java.javaUUID
import java.util.UUID

object UserTable : IdTable<UUID>("user") {
    override val id = javaUUID("id").entityId()

    val login = varchar("login", 50)
    val firstName = varchar("firstName", 100)
    val lastName = varchar("lastName", 100)
    val birthDate = long("birthDate")
    val registrationDate = long("registrationDate")
    val passwordHash = varchar("passwordHash", 150)
    val phone = varchar("phone", 20)
    val photoUrl = varchar("phone", 20)

    override val primaryKey = PrimaryKey(id)
}

class UserDao {
}