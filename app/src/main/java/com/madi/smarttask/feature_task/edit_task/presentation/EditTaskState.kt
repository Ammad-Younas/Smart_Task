package com.madi.smarttask.feature_task.edit_task.presentation

import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState

data class EditTaskState(
    val id: Long = 0L,
    val title: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val description: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val priority: Priority = Priority.LOW,
    val category: Category = Category.WORK,
    val isCompleted: Boolean = false,
    val dueDate: Long = 0L,
    val isLoading: Boolean = false,
)
