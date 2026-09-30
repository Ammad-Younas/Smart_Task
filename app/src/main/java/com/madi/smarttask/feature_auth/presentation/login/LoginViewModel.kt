package com.madi.smarttask.feature_auth.presentation.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.util.Resource
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _loginState = mutableStateOf(LoginState())
    val loginState: State<LoginState> = _loginState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredEmail -> {
                _loginState.value = loginState.value.copy(
                    emailState = loginState.value.emailState.copy(
                        text = event.value,
                        error = null
                    )
                )
            }
            is LoginEvent.EnteredPassword -> {
                _loginState.value = loginState.value.copy(
                    passwordState = loginState.value.passwordState.copy(
                        text = event.value,
                        error = null
                    )
                )
            }
            is LoginEvent.TogglePasswordVisibility -> {
                _loginState.value = loginState.value.copy(
                    passwordState = loginState.value.passwordState.copy(
                        isPasswordVisible = !loginState.value.passwordState.isPasswordVisible
                    )
                )
            }
            is LoginEvent.Login -> {
                login()
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            _loginState.value = loginState.value.copy(
                emailState = loginState.value.emailState.copy(error = null),
                passwordState = loginState.value.passwordState.copy(error = null),
                isLoading = true
            )

            val loginResult = loginUseCase(
                email = loginState.value.emailState.text,
                password = loginState.value.passwordState.text
            )

            if (loginResult.emailError != null || loginResult.passwordError != null) {
                _loginState.value = loginState.value.copy(
                    emailState = loginState.value.emailState.copy(error = loginResult.emailError),
                    passwordState = loginState.value.passwordState.copy(error = loginResult.passwordError),
                    isLoading = false
                )
                return@launch
            }

            when (val result = loginResult.result) {
                is Resource.Success -> {
                    _loginState.value = loginState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.login_successful)))
                    _eventFlow.emit(UiEvent.Navigate(Screen.HomeScreen.route))
                }
                is Resource.Error -> {
                    _loginState.value = loginState.value.copy(isLoading = false)
                    _eventFlow.emit(
                        UiEvent.ShowSnackBar(result.uiText ?: UiText.unknownError())
                    )
                }
                else -> {
                    _loginState.value = loginState.value.copy(isLoading = false)
                }
            }
        }
    }
}
