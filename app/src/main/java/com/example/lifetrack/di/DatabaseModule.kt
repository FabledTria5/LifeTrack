package com.example.lifetrack.di

import android.content.Context
import androidx.room.Room
import com.example.lifetrack.data.db.TasksDatabase
import com.example.lifetrack.data.db.dao.TasksDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): TasksDatabase = Room
        .databaseBuilder(
            context = context,
            klass = TasksDatabase::class.java,
            name = "tasks_database"
        )
        .fallbackToDestructiveMigration()
        .build()

    @Provides
    @Singleton
    fun provideDao(tasksDatabase: TasksDatabase): TasksDao = tasksDatabase.tasksDao()
}