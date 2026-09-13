package com.madi.smarttask.feature_task.task_detail.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.model.TaskDetail
import com.madi.smarttask.core.domain.model.TaskStatus

@HiltViewModel
class TaskDetailViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(
        TaskDetailState(
            taskDetail = TaskDetail(
                task = Task(
                    title = "Design UI for onboarding",
                    description = "Create modern and clean onboarding screens for SmartTask app. Include illustrations, smooth animations and clear messaging.",
                    category = Category.WORK,
                    priority = Priority.HIGH,
                    dueDate = System.currentTimeMillis()
                ),
                status = TaskStatus.COMPLETED
            )
        )
    )
    val state: StateFlow<TaskDetailState> = _state.asStateFlow()

    fun onEvent(event: TaskDetailEvent) {
        when (event) {
            is TaskDetailEvent.DeleteTask -> {
            }
        }
    }
}
