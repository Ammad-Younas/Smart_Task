package com.madi.smarttask.core.domain.usecase

import com.madi.smarttask.core.domain.repository.TaskRepository
import com.madi.smarttask.feature_task.task.domain.model.Stats
import kotlinx.coroutines.flow.Flow

class GetStats(
    private val repository: TaskRepository
) {
    operator fun invoke(): Flow<Stats> {
        return repository.getStats()
    }
}
