package com.madi.smarttask.feature_task.task_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.domain.model.TaskDetail
import com.madi.smarttask.core.domain.model.TaskStatus
import com.madi.smarttask.core.domain.usecase.TaskUseCases
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
class TaskDetailViewModel @Inject constructor(
    private val taskUseCases: TaskUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TaskDetailState())
    val state: StateFlow<TaskDetailState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<Any>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        val taskId = savedStateHandle.get<Long>("taskId") ?: -1L
        if (taskId != -1L) {
            viewModelScope.launch {
                taskUseCases.getTaskById(taskId)?.let { task ->
                    val currentTime = System.currentTimeMillis()
                    val status = when {
                        task.isCompleted -> TaskStatus.COMPLETED
                        task.dueDate in 1..<currentTime -> TaskStatus.OVERDUE
                        else -> TaskStatus.PENDING
                    }
                    _state.update {
                        it.copy(
                            taskDetail = TaskDetail(
                                task = task,
                                status = status
                            )
                        )
                    }
                }
            }
        }
    }

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.DeleteTask -> {
                viewModelScope.launch {
                    val id = state.value.taskDetail.task.id
                    if (id != 0L) {
                        taskUseCases.deleteTask(id)
                    }
                    _eventFlow.emit(TaskEvent.TaskDeleted)
                }
            }
        }
    }
}
