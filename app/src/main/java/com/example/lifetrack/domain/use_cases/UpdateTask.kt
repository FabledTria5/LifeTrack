package com.example.lifetrack.domain.use_cases

import com.example.lifetrack.domain.model.TaskItem
import com.example.lifetrack.domain.repository.TasksRepository
import javax.inject.Inject

class UpdateTask @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    suspend operator fun invoke(taskItem: TaskItem) = tasksRepository.updateTask(taskItem)

}