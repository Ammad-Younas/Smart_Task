package com.madi.smarttask.feature_name.domain.usecase

import com.madi.smarttask.feature_name.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class GetUserName(
    private val repository: UserRepository
) {
    operator fun invoke(): Flow<String?> {
        return repository.getUserName()
    }
}
