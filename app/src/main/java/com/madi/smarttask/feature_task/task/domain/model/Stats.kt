package com.madi.smarttask.feature_task.task.domain.model

data class Stats(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val overdueTasks: Int
)
