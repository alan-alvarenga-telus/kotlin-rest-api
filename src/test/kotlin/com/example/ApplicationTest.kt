package com.example

import com.example.model.Priority
import com.example.model.Task
import com.example.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlin.test.*
import io.ktor.serialization.kotlinx.json.*

class ApplicationTest {

    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }

    @Test
    fun tasksCanBeFoundByPriority() = testApplication {
        application {
            configureRouting()
            configureSerialization()
        }
        val response = client.get("/tasks/byPriority/Medium")
        val responseText = response.bodyAsText()
        println("Response body: $responseText") // Add this line for debugging
        val result = Json.decodeFromString<List<Task>>(responseText)
        assertEquals(HttpStatusCode.OK, response.status)

        val expectedTaskNames = listOf("gardening", "painting")
        val actualTaskNames = result.map(Task::name)
        assertContentEquals(expectedTaskNames, actualTaskNames)
    }

    @Test
    fun invalidPriorityProduces400() = testApplication {
        val response = client.get("/tasks/byPriority/Invalid")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun unusedPriorityProduces404() = testApplication {
        val response = client.get("/tasks/byPriority/Vital")
        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun newTasksCanBeAdded() = testApplication {
        val client = createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation){
                json()
            }
        }
        application {
            configureRouting()
            configureSerialization()
        }
        val task = Task("swimming", "Go to the beach", Priority.Low)
        val response1 = client.post("/tasks") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(task)
        }
        assertEquals(HttpStatusCode.NoContent, response1.status)
        val response2 = client.get("/tasks");
        val responseText = response2.bodyAsText()
        val tasks = Json.decodeFromString<List<Task>>(responseText)
        val taskNames = tasks.map { it.name }
        assertContains(taskNames, "swimming")
    }

}

