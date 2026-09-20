package com.madi.smarttask.core.domain.usecase

import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.repository.TaskRepository
import com.madi.smarttask.core.domain.util.ValidationUtil

class InsertTask(
    private val repository: TaskRepository,
) {
    suspend operator fun invoke(task: Task) {
        val titleError = ValidationUtil.validateTitle(task.title)
        if (titleError == null) {
            repository.insertTask(task)
        } else {
            throw IllegalArgumentException("Title cannot be empty")
        }
    }
}
