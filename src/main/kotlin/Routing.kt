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

fun Application.configureRouting() {
    routing {
        staticResources("/task-ui", "task-ui")
        staticResources("static", "static")

        get("/") {
            call.respondText("Hello World!")
        }
    }
}
