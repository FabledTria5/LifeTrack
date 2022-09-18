package com.example.lifetrack.data.db.utils

import androidx.room.TypeConverter
import com.example.lifetrack.domain.model.TaskPriority

object PriorityConverter {

    @TypeConverter
    fun toRepeatMode(priority: Int): TaskPriority = TaskPriority.getPriority(priority)

    @TypeConverter
    fun toRepeatModeOrdinal(taskPriority: TaskPriority): Int = taskPriority.priority

}