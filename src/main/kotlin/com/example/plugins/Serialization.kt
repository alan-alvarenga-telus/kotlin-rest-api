package com.example.plugins

import com.example.model.Task
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.contextual

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json(Json {
            serializersModule = SerializersModule {
                contextual(Task.serializer())
            }
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    routing {
        get("/json/kotlinx-serialization") {
                call.respond(mapOf("hello" to "world"))
            }
    }
}
