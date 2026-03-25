package com.kchat.routes.handlers.register

import com.kchat.tokens.data.UserTokensRepository
import com.kchat.tokens.model.RefreshResponse
import com.kchat.tokens.model.request.User
import io.ktor.http.HttpStatusCode
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import kotlinx.serialization.SerializationException
import java.util.UUID

internal fun Route.handleLogout(userTokensRepository: UserTokensRepository) {
    authenticate("auth-jwt") {
        delete("/logout") {
            try {
                val user = call.receive<User>()
                val jwtPrincipal = call.principal<JWTPrincipal>()
                if (jwtPrincipal == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        RefreshResponse(
                            message = "Error: cant get jwt principal",
                            code = HttpStatusCode.BadRequest.value
                        )
                    )
                    return@delete
                }
                val payload = jwtPrincipal.payload
                val id = payload.getClaim("uuid")
                if (id == null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        RefreshResponse(
                            message = "Error: token without uuid claim",
                            code = HttpStatusCode.BadRequest.value
                        )
                    )
                    return@delete
                }
                val uuid = UUID.fromString(id.asString())
                if (userTokensRepository.removeToken(uuid,user.login)) {
                    call.respond(
                        HttpStatusCode.OK,
                        RefreshResponse(
                            message = "Success: token was deleted",
                            code = HttpStatusCode.OK.value
                        )
                    )
                } else {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        RefreshResponse(
                            message =  "Error: user logins did not match",
                            code = HttpStatusCode.BadRequest.value
                        )
                    )
                }
                return@delete
            } catch (ex: SerializationException) {
                call.respond(
                    HttpStatusCode.BadRequest,
                    RefreshResponse(
                        message = "Error: serialization issue",
                        code = HttpStatusCode.BadRequest.value
                    )
                )
            }
        }
    }
}