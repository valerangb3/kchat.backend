package com.kchat.routes.handlers.register

import com.auth0.jwt.JWT
import com.kchat.tokens.data.UserTokensRepository
import com.kchat.tokens.model.RefreshResponse
import com.kchat.tokens.model.Tokens
import com.kchat.tokens.model.request.Token
import com.kchat.user.model.request.AuthResponse
import com.kchat.user.model.request.ResponseRegistration
import com.kchat.user.model.request.toResponseUser
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import kotlinx.serialization.SerializationException
import java.util.UUID

internal fun Route.handleRefresh(userTokensRepository: UserTokensRepository) {
    /*authenticate("auth-jwt") {

    }*/
    post("/refresh") {
        var token: Token? = null
        try {
            token = call.receive<Token>()
            if (token == null) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    RefreshResponse(
                        message = "Error: request without refresh token",
                        code = HttpStatusCode.BadRequest.value
                    )
                )
                return@post
            } else {
                val decodedJWT = JWT.decode(token.refreshToken)
                val claimUuid = decodedJWT.getClaim("uuid")
                if (claimUuid == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        RefreshResponse(
                            message = "Error: token without uuid claim",
                            code = HttpStatusCode.BadRequest.value
                        )
                    )
                    return@post
                } else {
                    val uuid = UUID.fromString(claimUuid.asString())
                    val tokenInfo = userTokensRepository.refreshToken(uuid)
                    if (tokenInfo == null) {
                        call.respond(
                            HttpStatusCode.BadRequest,
                            RefreshResponse(
                                message = "Error: probably user with jwt not exist",
                                code = HttpStatusCode.BadRequest.value
                            )
                        )
                        return@post
                    } else {
                        val tokens = Tokens(
                            accessToken = tokenInfo.accessToken,
                            refreshToken = tokenInfo.refreshToken,
                        )
                        val authResponse = RefreshResponse(
                            tokens = tokens,
                            message = "JWT was successfully updated",
                            code = HttpStatusCode.OK.value
                        )

                        call.respond(
                            status = HttpStatusCode.OK,
                            message = authResponse
                        )
                    }
                }
            }

        } catch (ex: SerializationException) {

        }
    }
}