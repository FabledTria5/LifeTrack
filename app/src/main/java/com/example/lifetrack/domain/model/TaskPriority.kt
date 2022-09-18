package com.example.lifetrack.domain.model

sealed class TaskPriority(val name: String, val color: Long, val priority: Int) {
    object LowPriority : TaskPriority(name = "Low", color = 0xFF90EE90, priority = 1)
    object MediumPriority : TaskPriority(name = "Medium", color = 0xFFFFC300, priority = 2)
    object HighPriority : TaskPriority(name = "High", color = 0xFFFF5733, priority = 3)

    companion object {
        fun getPriority(priority: Int) = when (priority) {
            1 -> LowPriority
            2 -> MediumPriority
            3 -> HighPriority
            else -> throw Exception("Unsupported priority")
        }
    }
}