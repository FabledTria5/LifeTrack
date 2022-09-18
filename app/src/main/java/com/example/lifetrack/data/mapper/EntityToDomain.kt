package com.example.lifetrack.data.mapper

import com.example.lifetrack.data.db.entity.TaskEntity
import com.example.lifetrack.domain.model.TaskItem
import com.example.lifetrack.domain.model.TaskType
import java.time.LocalDate

fun List<TaskEntity>.toTasksList(): List<TaskItem> {
    val todayAsEpochDay = LocalDate.now().toEpochDay()

    return map { entity ->
        TaskItem(
            taskName = entity.taskName,
            isComplete = entity.isComplete,
            taskPriority = entity.taskPriority,
            taskType = when (entity.scheduledTo - todayAsEpochDay) {
                0L -> TaskType.TODAY
                1L -> TaskType.TOMORROW
                else -> TaskType.WEEK
            }
        )
    }
}