package com.madi.smarttask.feature_name.domain.usecase

import com.madi.smarttask.feature_name.domain.repository.UserRepository

class SaveUserName(
    private val repository: UserRepository
) {
    suspend operator fun invoke(name: String) {
        repository.saveUserName(name)
    }
}
