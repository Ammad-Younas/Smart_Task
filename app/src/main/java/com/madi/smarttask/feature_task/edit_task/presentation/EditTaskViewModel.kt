package com.madi.smarttask.feature_task.edit_task.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(EditTaskState())
    val state: StateFlow<EditTaskState> = _state.asStateFlow()

    fun onEvent(event: EditTaskEvent) {
        when (event) {
            is EditTaskEvent.EnteredTitle -> {
                _state.update { it.copy(title = it.title.copy(text = event.value)) }
            }
            is EditTaskEvent.EnteredDescription -> {
                _state.update { it.copy(description = it.description.copy(text = event.value)) }
            }
            is EditTaskEvent.SelectedCategory -> {
                _state.update { it.copy(category = event.category) }
            }
            is EditTaskEvent.SelectedPriority -> {
                _state.update { it.copy(priority = event.priority) }
            }
            is EditTaskEvent.SelectedDueDate -> {
                _state.update { it.copy(dueDate = event.dateMillis) }
            }
            is EditTaskEvent.ToggledCompletion -> {
                _state.update { it.copy(isCompleted = event.isCompleted) }
            }
            EditTaskEvent.DeleteTask -> {
            }
            EditTaskEvent.UpdateTask -> {
            }
        }
    }
}