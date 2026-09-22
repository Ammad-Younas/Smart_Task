package com.madi.smarttask.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.madi.smarttask.core.data.local.dao.CategoryDao
import com.madi.smarttask.core.data.local.dao.NotificationDao
import com.madi.smarttask.core.data.local.dao.TaskDao
import com.madi.smarttask.core.data.local.dao.TaskStatsDao
import com.madi.smarttask.core.data.local.entity.CategoryEntity
import com.madi.smarttask.core.data.local.entity.NotificationEntity
import com.madi.smarttask.core.data.local.entity.TaskEntity
import com.madi.smarttask.core.data.local.entity.TaskStatsEntity

@Database(
    entities = [
        TaskEntity::class,
        TaskStatsEntity::class,
        CategoryEntity::class,
        NotificationEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class SmartTaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val taskStatsDao: TaskStatsDao
    abstract val categoryDao: CategoryDao
    abstract val notificationDao: NotificationDao

    companion object {
        const val DATABASE_NAME = "smart_task_db"
    }
}
