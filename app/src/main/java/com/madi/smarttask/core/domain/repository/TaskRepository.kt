package com.madi.smarttask.core.domain.repository

import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.feature_task.task.domain.model.Stats
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    fun getTasks(): Flow<List<Task>>
    fun getStats(): Flow<Stats>
    suspend fun insertTask(task: Task)
    suspend fun updateTask(task: Task)
    suspend fun deleteTask(id: Long)
}
