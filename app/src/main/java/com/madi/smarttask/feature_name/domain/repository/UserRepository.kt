package com.madi.smarttask.feature_name.domain.repository

import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUserName(): Flow<String?>
    suspend fun saveUserName(name: String)
}
