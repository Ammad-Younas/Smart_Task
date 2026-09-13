package com.madi.smarttask.feature_task.edit_task.presentation

import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority

sealed class EditTaskEvent {
    data class EnteredTitle(val value: String) : EditTaskEvent()
    data class EnteredDescription(val value: String) : EditTaskEvent()
    data class SelectedPriority(val priority: Priority) : EditTaskEvent()
    data class SelectedCategory(val category: Category) : EditTaskEvent()
    data class SelectedDueDate(val dateMillis: Long) : EditTaskEvent()
    data class ToggledCompletion(val isCompleted: Boolean) : EditTaskEvent()
    object UpdateTask : EditTaskEvent()
    object DeleteTask : EditTaskEvent()
}