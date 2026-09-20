package com.madi.smarttask.core.domain.usecase

data class TaskUseCases(
    val insertTask: InsertTask,
    val getTasks: GetTasks,
    val updateTask: UpdateTask,
    val deleteTask: DeleteTask,
    val getStats: GetStats,
    val getTaskById: GetTaskById,
)
