package com.kchat.routes.handlers.register

import com.kchat.tokens.data.UserTokensRepository
import com.kchat.tokens.model.UserTokens
import com.kchat.user.data.LoginResult
import com.kchat.user.data.UserRepository
import com.kchat.user.model.request.AuthCredentials
import com.kchat.user.model.request.AuthResponse
import com.kchat.user.model.request.ResponseRegistration
import com.kchat.user.model.request.toResponseUser
import com.kchat.utils.TokenManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerializationException

internal fun Route.handleLogin(
    userRepository: UserRepository,
    userTokensRepository: UserTokensRepository,
    tokenManager: TokenManager
) {
    post("/login") {
        try {
            val credential = call.receive<AuthCredentials>()
            val loginResult = userRepository.login(credential)
            when (loginResult) {
                is LoginResult.NotFoundResult -> {
                    call.respond(
                        status = HttpStatusCode.NotFound,
                        message = ResponseRegistration(
                            message = "Error user not exist",
                            code = HttpStatusCode.NotFound.value,
                        )
                    )
                }
                is LoginResult.FailLoginResult -> {
                    call.respond(
                        status = HttpStatusCode.Unauthorized,
                        message = ResponseRegistration(
                            message = "Fail user login",
                            code = HttpStatusCode.Unauthorized.value,
                        )
                    )
                }
                is LoginResult.SuccessResult -> {
                    val uuid = loginResult.user.id
                    if (uuid != null) {
                        val userTokens = userTokensRepository.getByUUID(uuid)
                        if (userTokens != null) {
                            val refreshToken = userTokens.refreshToken
                            val tokensPair = userTokensRepository.refreshToken(uuid, refreshToken)
                            if (tokensPair != null) {
                                val authResponse = AuthResponse(
                                    accessToken = tokensPair.accessToken,
                                    accessExpiresAt = tokensPair.accessExpire,
                                    refreshToken = tokensPair.refreshToken,
                                    user = loginResult.user.toResponseUser()
                                )
                                call.respond(
                                    status = HttpStatusCode.OK,
                                    message = ResponseRegistration(
                                        message = "The ${loginResult.user.login} was successfully created",
                                        code = HttpStatusCode.OK.value,
                                        data = authResponse
                                    )
                                )
                            } else {
                                call.respond(
                                    status = HttpStatusCode.BadRequest,
                                    message = ResponseRegistration(
                                        message = "Error update refresh token",
                                        code = HttpStatusCode.BadRequest.value,
                                    )
                                )
                            }
                        } else {
                            val tokenInfo = tokenManager.generateTokens(
                                loginResult.user.login,
                                uuid
                            )
                            val authResponse = AuthResponse(
                                accessToken = tokenInfo.accessToken,
                                accessExpiresAt = tokenInfo.accessExpire,
                                refreshToken = tokenInfo.refreshToken,
                                user = loginResult.user.toResponseUser()
                            )
                            call.respond(
                                status = HttpStatusCode.Created,
                                message = ResponseRegistration(
                                    message = "The tokens for ${loginResult.user.login} was successfully created",
                                    code = HttpStatusCode.Created.value,
                                    data = authResponse
                                )
                            )
                        }
                    } else {
                        call.respond(
                            status = HttpStatusCode.Unauthorized,
                            message = ResponseRegistration(
                                message = "Error user login",
                                code = HttpStatusCode.Unauthorized.value,
                            )
                        )
                    }
                }
            }
        } catch (ex: SerializationException) {
            call.respond(
                status = HttpStatusCode.BadRequest,
                message = "User serialization error"
            )
        }
    }
}