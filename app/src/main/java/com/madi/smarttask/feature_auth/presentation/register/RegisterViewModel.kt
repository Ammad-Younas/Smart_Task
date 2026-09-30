package com.madi.smarttask.feature_auth.presentation.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.util.Resource
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    private val _registerState = mutableStateOf(RegisterState())
    val registerState: State<RegisterState> = _registerState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EnteredUsername -> {
                _registerState.value = registerState.value.copy(
                    usernameState = registerState.value.usernameState.copy(
                        text = event.value,
                        error = null
                    )
                )
            }
            is RegisterEvent.EnteredEmail -> {
                _registerState.value = registerState.value.copy(
                    emailState = registerState.value.emailState.copy(
                        text = event.value,
                        error = null
                    )
                )
            }
            is RegisterEvent.EnteredPassword -> {
                _registerState.value = registerState.value.copy(
                    passwordState = registerState.value.passwordState.copy(
                        text = event.value,
                        error = null
                    )
                )
            }
            is RegisterEvent.TogglePasswordVisibility -> {
                _registerState.value = registerState.value.copy(
                    passwordState = registerState.value.passwordState.copy(
                        isPasswordVisible = !registerState.value.passwordState.isPasswordVisible
                    )
                )
            }
            is RegisterEvent.Register -> {
                register()
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _registerState.value = registerState.value.copy(
                usernameState = registerState.value.usernameState.copy(error = null),
                emailState = registerState.value.emailState.copy(error = null),
                passwordState = registerState.value.passwordState.copy(error = null),
                isLoading = true
            )

            val registerResult = registerUseCase(
                email = registerState.value.emailState.text,
                username = registerState.value.usernameState.text,
                password = registerState.value.passwordState.text
            )

            if (registerResult.usernameError != null || registerResult.emailError != null || registerResult.passwordError != null) {
                _registerState.value = registerState.value.copy(
                    usernameState = registerState.value.usernameState.copy(error = registerResult.usernameError),
                    emailState = registerState.value.emailState.copy(error = registerResult.emailError),
                    passwordState = registerState.value.passwordState.copy(error = registerResult.passwordError),
                    isLoading = false
                )
                return@launch
            }

            when (val result = registerResult.result) {
                is Resource.Success -> {
                    _registerState.value = registerState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.registration_successful)))
                    _eventFlow.emit(UiEvent.Navigate(Screen.LoginScreen.route))
                }
                is Resource.Error -> {
                    _registerState.value = registerState.value.copy(isLoading = false)
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(result.uiText ?: UiText.unknownError())
                    )
                }
                else -> {
                    _registerState.value = registerState.value.copy(isLoading = false)
                }
            }
        }
    }
}
