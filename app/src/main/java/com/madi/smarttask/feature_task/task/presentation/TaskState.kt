package com.madi.smarttask.feature_task.task.presentation

import com.madi.smarttask.core.data.local.entity.CategoryEntity
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState
import com.madi.smarttask.feature_task.task.domain.model.Stats

data class TaskState(
    val title: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val description: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val priority: Priority = Priority.LOW,
    val category: Category = Category.WORK,
    val dueDate: Long = 0L,
    val isLoading: Boolean = false,
    val tasks: List<Task> = emptyList(),
    val categories: List<CategoryEntity> = emptyList(),
    val stats: Stats = Stats(0, 0, 0, 0),
)
