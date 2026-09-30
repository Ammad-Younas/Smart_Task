package com.madi.smarttask.feature_auth.presentation.register

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
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
import com.madi.smarttask.feature_auth.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase,
    private val authenticateWithGoogleUseCase: AuthenticateWithGoogleUseCase
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

    fun onGoogleSignUpClick(context: Context, webClientId: String) {
        viewModelScope.launch {
            _registerState.value = registerState.value.copy(isLoading = true)
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
                        _registerState.value = registerState.value.copy(isLoading = false)
                    }
                } else {
                    _registerState.value = registerState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_configure_web_client_id)))
                }
            } catch (_: NoCredentialException) {
                _registerState.value = registerState.value.copy(isLoading = false)
                _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.error_no_google_account)))
            } catch (e: GetCredentialException) {
                _registerState.value = registerState.value.copy(isLoading = false)
                val uiText = e.message?.let { UiText.DynamicString(it) }
                    ?: UiText.StringResource(R.string.error_google_sign_up_canceled)
                _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
            } catch (e: Exception) {
                _registerState.value = registerState.value.copy(isLoading = false)
                val uiText = e.localizedMessage?.let { UiText.DynamicString(it) }
                    ?: UiText.StringResource(R.string.error_google_sign_up_failed)
                _eventFlow.emit(UiEvent.ShowSnackBar(uiText))
            }
        }
    }

    private fun onGoogleSignInResult(idToken: String) {
        viewModelScope.launch {
            _registerState.value = registerState.value.copy(isLoading = true)
            when (val result = authenticateWithGoogleUseCase(idToken)) {
                is Resource.Success -> {
                    _registerState.value = registerState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.registration_successful)))
                    _eventFlow.emit(UiEvent.Navigate(Screen.HomeScreen.route))
                }
                is Resource.Error -> {
                    _registerState.value = registerState.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(result.uiText ?: UiText.unknownError()))
                }
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
                    _eventFlow.emit(UiEvent.Navigate(Screen.HomeScreen.route))
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
