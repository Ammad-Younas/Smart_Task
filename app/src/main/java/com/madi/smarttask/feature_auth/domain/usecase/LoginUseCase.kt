package com.madi.smarttask.feature_auth.domain.usecase

import com.madi.smarttask.core.domain.util.ValidationUtil
import com.madi.smarttask.feature_auth.domain.model.LoginResult
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): LoginResult {
        val emailError = ValidationUtil.validateEmail(email)
        val passwordError = ValidationUtil.validatePassword(password)

        if (emailError != null || passwordError != null) {
            return LoginResult(
                emailError = emailError,
                passwordError = passwordError
            )
        }

        val result = repository.login(email, password)
        return LoginResult(result = result)
    }
}
