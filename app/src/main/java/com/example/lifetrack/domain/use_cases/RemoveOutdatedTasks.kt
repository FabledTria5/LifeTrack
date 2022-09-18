package com.example.lifetrack.domain.use_cases

import com.example.lifetrack.domain.repository.TasksRepository
import javax.inject.Inject

class RemoveOutdatedTasks @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke() = tasksRepository.removeOutdatedTasks()

}