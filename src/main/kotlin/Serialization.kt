package com.kchat

import com.kchat.model.Priority
import com.kchat.model.Task
import com.kchat.model.TaskRepository
import com.kchat.routes.userRoutes
import com.kchat.tokens.data.UserTokensRepository
import com.kchat.tokens.model.UserTokens
import com.kchat.user.model.User
import com.kchat.user.data.UserRepository
import com.kchat.user.model.request.AuthResponse
import com.kchat.user.model.request.RequestRegistration
import com.kchat.user.model.request.ResponseRegistration
import com.kchat.user.model.request.toResponseUser
import com.kchat.utils.TokenManager
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.SerializationException
import org.jetbrains.exposed.v1.exceptions.ExposedSQLException

private fun RoutingContext.registration() {

}

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        /*
        route("/tasks") {
            get {
                val tasks = taskRepository.allTasks()
                call.respond(tasks)
            }

            get("/byPriority/{priority?}") {
                val priorityAsText = call.parameters["priority"]
                if (priorityAsText == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                try {
                    val priority = Priority.valueOf(priorityAsText)
                    val tasks = taskRepository.tasksByPriority(priority)
                    if (tasks.isEmpty()) {
                        call.respond(HttpStatusCode.NotFound)
                        return@get
                    }

                    call.respond(tasks)
                } catch (ex: IllegalArgumentException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            get("/byName/{taskName}") {
                val name = call.parameters["taskName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@get
                }

                val task = taskRepository.taskByName(name)
                if (task == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(task)
            }

            post {
                try {
                    val task = call.receive<Task>()
                    taskRepository.addTask(task)
                    call.respond(HttpStatusCode.Created)
                } catch (ex: IllegalStateException) {
                    call.respond(HttpStatusCode.BadRequest)
                } catch (ex: SerializationException) {
                    call.respond(HttpStatusCode.BadRequest)
                }
            }

            delete("/{taskName}") {
                val name = call.parameters["taskName"]
                if (name == null) {
                    call.respond(HttpStatusCode.BadRequest)
                    return@delete
                }

                if (taskRepository.removeTask(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }*/

    }
}
