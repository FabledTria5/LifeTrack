package com.example.lifetrack.domain.repository

import com.example.lifetrack.domain.model.TaskItem
import com.example.lifetrack.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TasksRepository {

    suspend fun createTask(taskName: String, taskDate: Long, taskPriority: TaskPriority)

    suspend fun updateTask(taskItem: TaskItem)

    suspend fun removeOutdatedTasks()

    fun getUpcomingTasks(): Flow<List<TaskItem>>

}