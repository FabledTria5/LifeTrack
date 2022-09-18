package com.example.lifetrack.di

import com.example.lifetrack.data.repository.TasksRepositoryImpl
import com.example.lifetrack.domain.repository.TasksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
interface BindingModule {

    @Binds
    @ViewModelScoped
    fun bindTasksRepository(tasksRepositoryImpl: TasksRepositoryImpl): TasksRepository

}