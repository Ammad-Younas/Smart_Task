package com.madi.smarttask.feature_auth.domain.repository

import com.madi.smarttask.core.util.SimpleResource

interface AuthRepository {
    suspend fun login(email: String, password: String): SimpleResource
}
