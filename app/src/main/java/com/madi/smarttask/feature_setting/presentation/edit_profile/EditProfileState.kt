package com.madi.smarttask.feature_setting.presentation.edit_profile

import com.madi.smarttask.core.domain.states.PasswordTextFieldState
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState

data class EditProfileState(
    val emailState: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val usernameState: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val passwordState: PasswordTextFieldState = PasswordTextFieldState(),
    val isLoading: Boolean = false
)
