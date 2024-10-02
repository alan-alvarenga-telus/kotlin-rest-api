package com.example.model

object TaskRepository {
    private val tasks = mutableListOf<Task>(
        Task("cleaning", "Clean the house", Priority.Low),
        Task("gardening", "Mow the lawn", Priority.Medium),
        Task("shopping", "Buy the groceries", Priority.High),
        Task("painting", "Paint the fence", Priority.Medium)
    )

    fun allTasks(): List<Task> = tasks
    fun tasksByPriority(priority: Priority) = tasks.filter {
        it.priority == priority
    }

    fun tasksByName(name: String) = tasks.find {
        it.name.equals(name, ignoreCase = true)
    }

    fun addTask(task: Task) {
        if (tasksByName(task.name) != null) {
            throw IllegalStateException("Cannot duplicate task name")
        }
        tasks.add(task)
    }

    fun removeTask(name: String): MutableList<Task> {
        tasks.removeIf({ it.name == name })
        return tasks
    }
}