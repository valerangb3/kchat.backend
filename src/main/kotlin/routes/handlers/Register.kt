package com.kchat.routes.handlers

import com.kchat.tokens.data.UserTokensRepository
import com.kchat.tokens.model.UserTokens
import com.kchat.user.data.UserRepository
import com.kchat.user.model.request.AuthResponse
import com.kchat.user.model.request.RequestRegistration
import com.kchat.user.model.request.ResponseRegistration
import com.kchat.user.model.request.toResponseUser
import com.kchat.utils.TokenManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException

internal suspend fun RoutingContext.handleRegister(
    userRepository: UserRepository,
    userTokensRepository: UserTokensRepository,
    tokenManager: TokenManager
) {
    var requestRegistration: RequestRegistration? = null
    try {
        requestRegistration = call.receive<RequestRegistration>()
        val user = userRepository.registration(requestRegistration)
        val userUuid = user.id
        if (userUuid == null) {
            call.respond(
                status = HttpStatusCode.NoContent,
                message = "User need uuid"
            )
            return
        }
        val tokenInfo = tokenManager.generateTokens(user.login)
        val refreshExpire = tokenInfo.refreshExpire

        userTokensRepository.create(UserTokens(
            userUUID = userUuid,
            refreshToken = tokenInfo.refreshToken,
            expiresAt = refreshExpire
        ))

        val authResponse = AuthResponse(
            accessToken = tokenInfo.accessToken,
            accessExpiresAt = tokenInfo.accessExpire,
            refreshToken = tokenInfo.refreshToken,
            user = user.toResponseUser()
        )

        call.respond(
            status = HttpStatusCode.Created,
            message = ResponseRegistration(
                message = "The ${user.login} was successfully created",
                code = HttpStatusCode.Created.value,
                data = authResponse
            )
        )
    } catch (ex: SerializationException) {
        call.respond(
            status = HttpStatusCode.BadRequest,
            message = "User serialization error"
        )
    } catch (ex: ExposedSQLException) {
        val login = requestRegistration?.login ?: "No name"
        call.respond(
            status = HttpStatusCode.Conflict,
            message = ResponseRegistration(
                message = "Login: $login already exist",
                code = HttpStatusCode.Conflict.value,
            )
        )
    }
}