package com.kchat

import com.kchat.model.PostgresTaskRepository
import com.kchat.tokens.data.PostgresUserTokensRepository
import com.kchat.tokens.model.JwtConfig
import com.kchat.user.data.PostgresUserRepository
import com.kchat.utils.TokenManager
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    //val repository = PostgresTaskRepository()
    val userRepository = PostgresUserRepository()
    val userTokensRepository = PostgresUserTokensRepository()
    val jwtConfig = JwtConfig(
        jwtSecret = environment.config.property("jwt.secret").getString(),
        jwtAudience = environment.config.property("jwt.audience").getString(),
        jwtDomain = environment.config.property("jwt.issuer").getString(),
        jwtRealm = environment.config.property("jwt.realm").getString(),
    )
    val tokenManager = TokenManager(jwtConfig)
    configureSerialization()
    configureDatabases()
    configureSockets()
    configureSecurity(jwtConfig)
    configureRouting(
        userRepository,
        userTokensRepository,
        tokenManager
    )
}
