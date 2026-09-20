package com.madi.smarttask.core.data.repository

import com.madi.smarttask.core.data.local.dao.TaskDao
import com.madi.smarttask.core.data.local.dao.TaskStatsDao
import com.madi.smarttask.core.data.local.entity.TaskEntity
import com.madi.smarttask.core.data.local.entity.TaskStatsEntity
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.repository.TaskRepository
import com.madi.smarttask.feature_task.task.domain.model.Stats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class TaskRepositoryImpl (
    private val dao: TaskDao,
    private val taskStatsDao: TaskStatsDao
): TaskRepository {

    override fun getTasks(): Flow<List<Task>> {
        return dao.getTasks()
            .onStart { recalculateAndUpdateStats() }
            .map { entities -> entities.map { it.toTask() } }
    }

    override fun getStats(): Flow<Stats> {
        return taskStatsDao.getStats()
            .onStart { recalculateAndUpdateStats() }
            .map { entity -> entity?.toStats() ?: Stats(0, 0, 0, 0) }
    }

    override suspend fun getTaskById(id: Long): Task? {
        return dao.getTaskById(id)?.toTask()
    }

    override suspend fun insertTask(task: Task) {
        dao.insertTask(TaskEntity.fromTask(task))
        recalculateAndUpdateStats()
    }

    override suspend fun updateTask(task: Task) {
        dao.updateTask(TaskEntity.fromTask(task))
        recalculateAndUpdateStats()
    }

    override suspend fun deleteTask(id: Long) {
        dao.deleteTask(id)
        recalculateAndUpdateStats()
    }

    private suspend fun recalculateAndUpdateStats() {
        val tasks = dao.getAllTasksList()
        val currentTime = System.currentTimeMillis()
        val total = tasks.size
        val completed = tasks.count { it.isComplete }
        val overdue = tasks.count { !it.isComplete && it.dueDate in 1..<currentTime }
        val pending = tasks.count { !it.isComplete && (it.dueDate == 0L || it.dueDate >= currentTime) }

        taskStatsDao.insertOrUpdateStats(
            TaskStatsEntity(
                id = 1,
                totalTasks = total,
                completedTasks = completed,
                pendingTasks = pending,
                overdueTasks = overdue
            )
        )
    }
}
