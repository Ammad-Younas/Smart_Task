package com.madi.smarttask.feature_auth.presentation.login

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.util.Constants
import com.madi.smarttask.core.util.Resource
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.domain.usecase.AuthenticateWithGoogleUseCase
import com.madi.smarttask.feature_auth.domain.usecase.LoginUseCase
import com.madi.smarttask.feature_name.domain.usecase.NameUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authenticateWithGoogleUseCase: AuthenticateWithGoogleUseCase,
    private val nameUseCases: NameUseCases
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

    fun onGoogleSignInClick(context: Context, webClientId: String) {
        viewModelScope.launch {
            _loginState.value = loginState.value.copy(isLoading = true)
            try {
                if (webClientId.isNotBlank() && webClientId != Constants.DEFAULT_WEB_CLIENT_ID) {
                    val credentialManager = CredentialManager.create(context)
                    val googleIdOption = GetGoogleIdOption.Builder()
                        .setFilterByAuthorizedAccounts(false)
                        .setServerClientId(webClientId)
                        .setAutoSelectEnabled(false)
                        .build()

                    val request = GetCredentialRequest.Builder()
                        .addCredentialOption(googleIdOption)
                        .build()

                    val result = credentialManager.getCredential(
                        request = request,
                        context = context
                    )

                    val credential = result.credential
                    if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                        val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                        onGoogleSignInResult(googleIdTokenCredential.idToken)
                    } else {
                        _loginState.value = loginState.value.copy(isLoading = false)
                    }
                } else {
                    _loginState.value = loginState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_configure_web_client_id)))
                }
            } catch (e: Exception) {
                _loginState.value = loginState.value.copy(isLoading = false)
                val uiText = e.localizedMessage?.let { UiText.DynamicString(it) }
                    ?: UiText.StringResource(R.string.error_google_sign_in_failed)
                _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
            }
        }
    }

    private fun onGoogleSignInResult(idToken: String) {
        viewModelScope.launch {
            _loginState.value = loginState.value.copy(isLoading = true)
            when (val result = authenticateWithGoogleUseCase(idToken)) {
                is Resource.Success -> {
                    _loginState.value = loginState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.login_successful)))

                    val name = nameUseCases.getUserName().firstOrNull()
                    val targetRoute = if (name.isNullOrBlank()) {
                        Screen.NameScreen.route
                    } else {
                        Screen.HomeScreen.route
                    }
                    _eventFlow.emit(UiEvent.Navigate(targetRoute))
                }
                is Resource.Error -> {
                    _loginState.value = loginState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(result.uiText ?: UiText.unknownError()))
                }
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

                    val name = nameUseCases.getUserName().firstOrNull()
                    val targetRoute = if (name.isNullOrBlank()) {
                        Screen.NameScreen.route
                    } else {
                        Screen.HomeScreen.route
                    }
                    _eventFlow.emit(UiEvent.Navigate(targetRoute))
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
