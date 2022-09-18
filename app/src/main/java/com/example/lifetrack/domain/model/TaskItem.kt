package com.example.lifetrack.domain.model

import androidx.compose.runtime.Stable

@Stable
data class TaskItem(
    val taskName: String = "",
    val isComplete: Boolean = false,
    val taskPriority: TaskPriority = TaskPriority.LowPriority,
    val taskType: TaskType = TaskType.TODAY
)

enum class TaskType(val displayName: String) {
    TODAY(displayName = "Today"),
    TOMORROW(displayName = "Tomorrow"),
    WEEK(displayName = "This week")
}