package com.madi.smarttask.feature_auth.domain.usecase

import com.madi.smarttask.core.domain.util.ValidationUtil
import com.madi.smarttask.feature_auth.domain.model.RegisterResult
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import javax.inject.Inject

class RegisterUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, username: String, password: String): RegisterResult {
        val usernameError = ValidationUtil.validateUsername(username)
        val emailError = ValidationUtil.validateEmail(email)
        val passwordError = ValidationUtil.validatePassword(password)

        if (usernameError != null || emailError != null || passwordError != null) {
            return RegisterResult(
                usernameError = usernameError,
                emailError = emailError,
                passwordError = passwordError
            )
        }

        val result = repository.register(email, username, password)
        return RegisterResult(
            result = result
        )
    }
}
