package com.madi.smarttask.core.domain.usecase

import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow

class GetTasks(
    private val repository: TaskRepository,
) {
    operator fun invoke(): Flow<List<Task>> {
        return repository.getTasks()
    }
}
