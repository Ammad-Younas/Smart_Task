package com.madi.smarttask.core.domain.model

data class Task(
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val category: Category,
    val priority: Priority,
    val dueDate: Long = 0L,
)
