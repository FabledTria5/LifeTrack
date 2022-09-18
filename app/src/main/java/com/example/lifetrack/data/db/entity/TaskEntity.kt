package com.example.lifetrack.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lifetrack.domain.model.TaskPriority

@Entity(tableName = "tasks_table")
data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "task_name")
    val taskName: String,
    @ColumnInfo(name = "is_complete")
    val isComplete: Boolean = false,
    @ColumnInfo(name = "scheduled_to")
    val scheduledTo: Long,
    @ColumnInfo(name = "task_priority")
    val taskPriority: TaskPriority,
)
