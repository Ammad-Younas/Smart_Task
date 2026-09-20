package com.madi.smarttask.feature_task.task.presentation

import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.Task

sealed class TaskEvent {
    data class EnteredTitle(val value: String) : TaskEvent()
    data class EnteredDescription(val value: String) : TaskEvent()
    data class SelectedPriority(val priority: Priority) : TaskEvent()
    data class SelectedCategory(val category: Category) : TaskEvent()
    data class SelectedDueDate(val dateMillis: Long) : TaskEvent()
    data class ToggleTaskCompletion(val task: Task, val isCompleted: Boolean) : TaskEvent()
    data class DeleteTask(val id: Long) : TaskEvent()
    object SaveTask : TaskEvent()
}
