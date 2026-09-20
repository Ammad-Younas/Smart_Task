package com.madi.smarttask.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.madi.smarttask.feature_task.task.domain.model.Stats

@Entity(tableName = "task_stats")
data class TaskStatsEntity(
    @PrimaryKey
    val id: Int = 1,
    val totalTasks: Int = 0,
    val completedTasks: Int = 0,
    val pendingTasks: Int = 0,
    val overdueTasks: Int = 0
) {
    fun toStats(): Stats {
        return Stats(
            totalTasks = totalTasks,
            completedTasks = completedTasks,
            pendingTasks = pendingTasks,
            overdueTasks = overdueTasks
        )
    }

    companion object {
        fun fromStats(stats: Stats): TaskStatsEntity {
            return TaskStatsEntity(
                id = 1,
                totalTasks = stats.totalTasks,
                completedTasks = stats.completedTasks,
                pendingTasks = stats.pendingTasks,
                overdueTasks = stats.overdueTasks
            )
        }
    }
}
