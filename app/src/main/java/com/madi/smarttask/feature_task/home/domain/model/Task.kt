package com.madi.smarttask.feature_task.home.domain.model

data class Task(
    val title: String,
    val description: String,
    val isCompleted: Boolean = false,
    val category: Category,
    val priority: Priority,
    val dueDate: Long = 0L,
)
