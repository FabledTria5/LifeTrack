package com.example.lifetrack.domain.use_cases

import com.example.lifetrack.domain.repository.TasksRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUpcomingTasks @Inject constructor(
    private val tasksRepository: TasksRepository
) {

    operator fun invoke() = tasksRepository.getUpcomingTasks().map { list ->
        list.sortedByDescending { it.taskPriority.priority }
    }

}