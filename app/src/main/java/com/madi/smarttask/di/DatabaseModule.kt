package com.madi.smarttask.di

import android.content.Context
import androidx.room.Room
import com.madi.smarttask.core.data.local.SmartTaskDatabase
import com.madi.smarttask.core.data.local.dao.NotificationDao
import com.madi.smarttask.core.data.local.dao.TaskDao
import com.madi.smarttask.core.data.repository.CategoryRepositoryImpl
import com.madi.smarttask.core.data.repository.TaskRepositoryImpl
import com.madi.smarttask.core.domain.repository.CategoryRepository
import com.madi.smarttask.core.domain.repository.TaskRepository
import com.madi.smarttask.core.domain.usecase.DeleteTask
import com.madi.smarttask.core.domain.usecase.GetStats
import com.madi.smarttask.core.domain.usecase.GetTaskById
import com.madi.smarttask.core.domain.usecase.GetTasks
import com.madi.smarttask.core.domain.usecase.InsertTask
import com.madi.smarttask.core.domain.usecase.TaskUseCases
import com.madi.smarttask.core.domain.usecase.UpdateTask
import com.madi.smarttask.feature_notification.domain.util.TaskNotificationScheduler
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
    fun provideSmartTaskDatabase(@ApplicationContext context: Context): SmartTaskDatabase {
        return Room.databaseBuilder(
            context,
            SmartTaskDatabase::class.java,
            SmartTaskDatabase.DATABASE_NAME,
        )
        .fallbackToDestructiveMigration(true)
        .build()
    }

    @Provides
    @Singleton
    fun provideNotificationDao(db: SmartTaskDatabase): NotificationDao {
        return db.notificationDao
    }

    @Provides
    @Singleton
    fun provideTaskDao(db: SmartTaskDatabase): TaskDao {
        return db.taskDao
    }

    @Provides
    @Singleton
    fun provideTaskRepository(
        db: SmartTaskDatabase,
        taskNotificationScheduler: TaskNotificationScheduler
    ): TaskRepository {
        return TaskRepositoryImpl(
            dao = db.taskDao,
            taskStatsDao = db.taskStatsDao,
            taskNotificationScheduler = taskNotificationScheduler
        )
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(db: SmartTaskDatabase): CategoryRepository {
        return CategoryRepositoryImpl(db.categoryDao)
    }

    @Provides
    @Singleton
    fun provideTaskUseCases(repository: TaskRepository): TaskUseCases {
        return TaskUseCases(
            insertTask = InsertTask(repository),
            getTasks = GetTasks(repository),
            updateTask = UpdateTask(repository),
            deleteTask = DeleteTask(repository),
            getStats = GetStats(repository),
            getTaskById = GetTaskById(repository),
        )
    }
}
