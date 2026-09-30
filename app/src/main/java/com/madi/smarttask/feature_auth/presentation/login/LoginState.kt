package com.madi.smarttask.feature_auth.presentation.login

import com.madi.smarttask.core.domain.states.PasswordTextFieldState
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState

data class LoginState(
    val emailState: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val passwordState: PasswordTextFieldState = PasswordTextFieldState(),
    val isLoading: Boolean = false
)
