package com.example.lifetrack.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.lifetrack.data.db.dao.TasksDao
import com.example.lifetrack.data.db.entity.TaskEntity
import com.example.lifetrack.data.db.utils.LocalDateConverter
import com.example.lifetrack.data.db.utils.PriorityConverter

@Database(entities = [TaskEntity::class], version = 4, exportSchema = true)
@TypeConverters(LocalDateConverter::class, PriorityConverter::class)
abstract class TasksDatabase : RoomDatabase() {

    abstract fun tasksDao(): TasksDao

}