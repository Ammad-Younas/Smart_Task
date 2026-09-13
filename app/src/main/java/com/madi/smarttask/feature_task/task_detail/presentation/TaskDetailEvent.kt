package com.madi.smarttask.feature_task.task_detail.presentation

sealed class TaskDetailEvent {
    object DeleteTask : TaskDetailEvent()
}
