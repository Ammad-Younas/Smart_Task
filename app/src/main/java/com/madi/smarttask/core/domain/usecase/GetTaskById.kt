package com.madi.smarttask.core.domain.usecase

import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.repository.TaskRepository

class GetTaskById(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(id: Long): Task? {
        return repository.getTaskById(id)
    }
}
