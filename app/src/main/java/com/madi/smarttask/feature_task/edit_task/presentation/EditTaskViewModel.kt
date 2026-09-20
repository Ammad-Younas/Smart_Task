package com.madi.smarttask.feature_task.edit_task.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState
import com.madi.smarttask.core.domain.usecase.TaskUseCases
import com.madi.smarttask.core.domain.util.ValidationUtil
import com.madi.smarttask.feature_task.task.presentation.TaskEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditTaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(EditTaskState())
    val state: StateFlow<EditTaskState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Any>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val taskId = savedStateHandle.get<Long>("taskId") ?: -1L
        if (taskId != -1L) {
            viewModelScope.launch {
                taskUseCases.getTaskById(taskId)?.let { task ->
                    _state.update {
                        it.copy(
                            id = task.id,
                            title = SmartTaskTextFieldState(text = task.title),
                            description = SmartTaskTextFieldState(text = task.description ?: ""),
                            priority = task.priority,
                            category = task.category,
                            dueDate = task.dueDate,
                            isCompleted = task.isCompleted
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: EditTaskEvent) {
        when (event) {
            is EditTaskEvent.EnteredTitle -> {
                _state.update { it.copy(title = it.title.copy(text = event.value, error = null)) }
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
                viewModelScope.launch {
                    if (state.value.id != 0L) {
                        taskUseCases.deleteTask(state.value.id)
                    }
                    _eventFlow.emit(TaskEvent.TaskDeleted)
                }
            }
            EditTaskEvent.UpdateTask -> {
                viewModelScope.launch {
                    val titleText = state.value.title.text
                    val titleError = ValidationUtil.validateTitle(titleText)
                    if (titleError != null) {
                        _state.update { it.copy(title = it.title.copy(error = titleError)) }
                        return@launch
                    }
                    taskUseCases.updateTask(
                        Task(
                            id = state.value.id,
                            title = titleText,
                            description = state.value.description.text.ifBlank { null },
                            isCompleted = state.value.isCompleted,
                            category = state.value.category,
                            priority = state.value.priority,
                            dueDate = state.value.dueDate
                        )
                    )
                    _eventFlow.emit(TaskEvent.TaskUpdated)
                }
            }
        }
    }
}
