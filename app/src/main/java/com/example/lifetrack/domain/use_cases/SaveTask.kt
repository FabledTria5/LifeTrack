package com.example.lifetrack.domain.use_cases

import com.example.lifetrack.domain.model.Resource
import com.example.lifetrack.domain.model.TaskPriority
import com.example.lifetrack.domain.repository.TasksRepository
import timber.log.Timber
import java.time.LocalDate
import javax.inject.Inject

class SaveTask @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke(
        taskName: String,
        taskDate: LocalDate,
        taskTasksPriority: TaskPriority
    ): Resource {
        return try {
            tasksRepository.createTask(
                taskName = taskName,
                taskDate = taskDate.toEpochDay(),
                taskPriority = taskTasksPriority
            )
            Resource.Success
        } catch (e: Exception) {
            Timber.e(e)
            Resource.Failure
        }
    }

}