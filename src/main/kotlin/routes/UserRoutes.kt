package com.kchat.routes

import com.kchat.routes.handlers.register.handleLogin
import com.kchat.routes.handlers.register.handleLogout
import com.kchat.routes.handlers.register.handleRefresh
import com.kchat.routes.handlers.register.handleRegister
import com.kchat.tokens.data.UserTokensRepository
import com.kchat.user.data.UserRepository
import com.kchat.user.model.User
import com.kchat.utils.TokenManager
import io.ktor.http.HttpStatusCode
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException



fun Route.userRoutes(
    userRepository: UserRepository,
    userTokensRepository: UserTokensRepository,
    tokenManager: TokenManager
) {
    route("/user") {
        //TODO удалить как завершу задачу авторизации
        post {
            try {
                //val requestRegistration = call.receive<RequestRegistration>()
                val user = call.receive<User>()
                userRepository.addUser(user)
                call.respond(HttpStatusCode.Created)
            } catch (ex: SerializationException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: ExposedSQLException) {
                call.respond(HttpStatusCode.Conflict)
            }
        }

        handleRegister(
            userRepository,
            userTokensRepository,
            tokenManager
        )
        handleLogout(userTokensRepository)
        handleLogin(
            userRepository,
            userTokensRepository,
            tokenManager
        )
        handleRefresh(userTokensRepository)
    }
}