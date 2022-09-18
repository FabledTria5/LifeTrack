package com.example.lifetrack.data.repository

import com.example.lifetrack.data.db.dao.TasksDao
import com.example.lifetrack.data.db.entity.TaskEntity
import com.example.lifetrack.data.mapper.toTasksList
import com.example.lifetrack.domain.model.TaskItem
import com.example.lifetrack.domain.model.TaskPriority
import com.example.lifetrack.domain.repository.TasksRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TasksRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao
) : TasksRepository {

    override suspend fun createTask(
        taskName: String,
        taskDate: Long,
        taskPriority: TaskPriority
    ) {
        tasksDao.insertTask(
            taskEntity = TaskEntity(
                taskName = taskName,
                scheduledTo = taskDate,
                taskPriority = taskPriority
            )
        )
    }

    override suspend fun updateTask(taskItem: TaskItem) =
        tasksDao.updateTask(taskName = taskItem.taskName, targetState = !taskItem.isComplete)

    override suspend fun removeOutdatedTasks() = tasksDao.deleteOutdatedTasks()

    override fun getUpcomingTasks(): Flow<List<TaskItem>> = tasksDao.getUpcomingTasks()
        .map { it.toTasksList() }

}