package com.example.model

import kotlinx.serialization.Serializable

@Serializable
enum class Priority {
    Low, Medium, High, Vital
}
@Serializable
class Task (
    val name: String,
    val description: String,
    val priority: Priority
)