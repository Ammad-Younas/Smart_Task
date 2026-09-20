package com.madi.smarttask.core.domain.model

data class Task(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val isCompleted: Boolean = false,
    val category: Category = Category.WORK,
    val priority: Priority = Priority.LOW,
    val dueDate: Long = 0L,
)
