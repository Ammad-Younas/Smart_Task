package com.madi.smarttask.feature_task.task_detail.presentation

import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.model.TaskDetail
import com.madi.smarttask.core.domain.model.TaskStatus

data class TaskDetailState(
    val taskDetail: TaskDetail = TaskDetail(
        task = Task(
            title = "",
            description = "",
            category = Category.WORK,
            priority = Priority.LOW,
            dueDate = 0L
        ),
        status = TaskStatus.TOTAL
    ),
    val isLoading: Boolean = false
)
