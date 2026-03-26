package com.kchat.routes.handlers.register

import com.kchat.tokens.data.UserTokensRepository
import com.kchat.user.data.UserRepository
import com.kchat.user.model.request.AuthCredentials
import com.kchat.utils.TokenManager
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerializationException

internal fun Route.handleLogin(
    userRepository: UserRepository,
    userTokensRepository: UserTokensRepository,
    tokenManager: TokenManager
) {
    //TODO login request
    post("/login") {
        try {
            val user = call.receive<AuthCredentials>()
            //user.
        } catch (ex: SerializationException) {

        }
    }
}