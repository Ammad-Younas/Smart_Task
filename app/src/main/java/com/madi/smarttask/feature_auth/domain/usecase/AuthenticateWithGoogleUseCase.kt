package com.madi.smarttask.feature_auth.domain.usecase

import com.madi.smarttask.core.util.SimpleResource
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class AuthenticateWithGoogleUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): SimpleResource {
        return repository.authenticateWithGoogle(idToken)
    }
}
