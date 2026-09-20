package com.madi.smarttask.feature_task.task.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.usecase.TaskUseCases
import com.madi.smarttask.core.domain.util.ValidationUtil
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases,
) : ViewModel() {

    private val _state = MutableStateFlow(TaskState())
    val state: StateFlow<TaskState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Any>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getTasks()
        getStats()
    }

    private fun getTasks() {
        taskUseCases.getTasks().onEach { tasks ->
            _state.update { it.copy(tasks = tasks) }
        }.launchIn(viewModelScope)
    }

    private fun getStats() {
        taskUseCases.getStats().onEach { stats ->
            _state.update { it.copy(stats = stats) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: TaskEvent) {
        when (event) {
            is TaskEvent.EnteredTitle -> {
                _state.update {
                    it.copy(
                        title = it.title.copy(
                            text = event.value,
                            error = null,
                        ),
                    )
                }
            }
            is TaskEvent.EnteredDescription -> {
                _state.update {
                    it.copy(
                        description = it.description.copy(
                            text = event.value,
                        ),
                    ) 
                }
            }
            is TaskEvent.SelectedCategory -> {
                _state.update {
                    it.copy(
                        category = event.category
                    )
                }
            }
            is TaskEvent.SelectedPriority -> {
                _state.update {
                    it.copy(
                        priority = event.priority
                    )
                }
            }
            is TaskEvent.SelectedDueDate -> {
                _state.update {
                    it.copy(
                        dueDate = event.dateMillis
                    )
                }
            }
            is TaskEvent.ToggleTaskCompletion -> {
                viewModelScope.launch {
                    taskUseCases.updateTask(event.task.copy(isCompleted = event.isCompleted))
                }
            }
            is TaskEvent.DeleteTask -> {
                viewModelScope.launch {
                    taskUseCases.deleteTask(event.id)
                }
            }
            is TaskEvent.SaveTask -> {
                saveTask()
            }
            else -> {}
        }
    }

    fun saveTask() {
        viewModelScope.launch {
            val titleText = state.value.title.text
            val titleError = ValidationUtil.validateTitle(titleText)
            if (titleError != null) {
                _state.update { it.copy(title = it.title.copy(error = titleError)) }
                return@launch
            }

            try {
                taskUseCases.insertTask(
                    Task(
                        title = titleText,
                        description = state.value.description.text.ifBlank { null },
                        priority = state.value.priority,
                        category = state.value.category,
                        dueDate = state.value.dueDate,
                    )
                )
                _state.update {
                    TaskState(
                        tasks = state.value.tasks,
                        stats = state.value.stats
                    )
                }
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.task_saved)))
                _eventFlow.emit(TaskEvent.TaskSaved)
            } catch (_: Exception) {
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.unknownError()))
            }
        }
    }
}
