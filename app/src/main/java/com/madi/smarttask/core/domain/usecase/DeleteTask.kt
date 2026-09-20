package com.madi.smarttask.core.domain.usecase

import com.madi.smarttask.core.domain.repository.TaskRepository

class DeleteTask(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long) {
        repository.deleteTask(id)
    }
}
