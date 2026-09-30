package com.madi.smarttask.feature_auth.presentation.register

import com.madi.smarttask.core.domain.states.PasswordTextFieldState
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState

data class RegisterState(
    val usernameState: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val emailState: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val passwordState: PasswordTextFieldState = PasswordTextFieldState(),
    val isLoading: Boolean = false
)
