package com.madi.smarttask.feature_task.task.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state.asStateFlow()

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.EnteredTitle -> {
                _state.update { it.copy(title = it.title.copy(text = event.value)) }
            }
            is TaskEvent.EnteredDescription -> {
                _state.update { it.copy(description = it.description.copy(text = event.value)) }
            }
            is TaskEvent.SelectedCategory -> {
                _state.update { it.copy(category = event.category) }
            }
            is TaskEvent.SelectedPriority -> {
                _state.update { it.copy(priority = event.priority) }
            }
            is TaskEvent.SelectedDueDate -> {
                _state.update { it.copy(dueDate = event.dateMillis) }
            }
            is TaskEvent.SaveTask -> {
                
            }
        }
    }
}
