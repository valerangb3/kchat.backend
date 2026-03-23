package com.kchat

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kchat.model.Priority
import com.kchat.model.Task
import com.kchat.model.TaskRepository
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.http.content.staticResources
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.serialization.SerializationException
import java.sql.Connection
import java.sql.DriverManager
import java.time.Duration
import kotlin.time.Duration.Companion.seconds
import org.jetbrains.exposed.sql.*

fun Application.configureRouting() {
    routing {
        staticResources("/task-ui", "task-ui")
        staticResources("static", "static")

        get("/") {
            call.respondText("Hello World!")
        }


        route("/tasks") {
            get {
                val tasks = TaskRepository.allTasks()
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
                    val tasks = TaskRepository.tasksByPriority(priority)
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

                val task = TaskRepository.taskByName(name)
                if (task == null) {
                    call.respond(HttpStatusCode.NotFound)
                    return@get
                }

                call.respond(task)
            }

            post {
                try {
                    val task = call.receive<Task>()
                    TaskRepository.addTask(task)
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

                if (TaskRepository.removeTask(name)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }
            }
        }
    }
}
