package com.madi.smarttask.core.domain.usecase

import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTaskById(
    private val repository: TaskRepository
) {
    operator fun invoke(id: Long): Flow<Task?> {
        return repository.getTaskById(id)
    }
}
