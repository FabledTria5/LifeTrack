package com.example.lifetrack.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.lifetrack.data.db.entity.TaskEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TasksDao {

    @Insert
    suspend fun insertTask(taskEntity: TaskEntity)

    @Query(value = "UPDATE tasks_table SET is_complete = :targetState WHERE task_name = :taskName")
    suspend fun updateTask(taskName: String, targetState: Boolean)

    @Query(value = "DELETE FROM tasks_table WHERE scheduled_to < :epochDay")
    suspend fun deleteOutdatedTasks(epochDay: Long = LocalDate.now().toEpochDay())

    @Query(value = "SELECT * FROM tasks_table WHERE (scheduled_to - :epochDay) <= 7")
    fun getUpcomingTasks(epochDay: Long = LocalDate.now().toEpochDay()): Flow<List<TaskEntity>>

}