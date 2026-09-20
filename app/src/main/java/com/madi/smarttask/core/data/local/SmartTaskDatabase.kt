package com.madi.smarttask.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.madi.smarttask.core.data.local.dao.TaskDao
import com.madi.smarttask.core.data.local.dao.TaskStatsDao
import com.madi.smarttask.core.data.local.entity.TaskEntity
import com.madi.smarttask.core.data.local.entity.TaskStatsEntity

@Database(
    entities = [TaskEntity::class, TaskStatsEntity::class],
    version = 2,
    exportSchema = false
)
abstract class SmartTaskDatabase : RoomDatabase() {
    abstract val taskDao: TaskDao
    abstract val taskStatsDao: TaskStatsDao

    companion object {
        const val DATABASE_NAME = "smart_task_db"
    }
}
