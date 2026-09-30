package com.madi.smarttask.feature_setting.presentation.edit_profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.madi.smarttask.R
import com.madi.smarttask.core.data.preferences.SettingsDataStore
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState
import com.madi.smarttask.core.domain.util.ValidationUtil
import com.madi.smarttask.core.util.Resource
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val settingsDataStore: SettingsDataStore,
    private val firebaseAuth: FirebaseAuth
) : ViewModel() {

    private val _state = mutableStateOf(EditProfileState())
    val state: State<EditProfileState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val user = firebaseAuth.currentUser
            val currentEmail = user?.email ?: ""
            val localName = settingsDataStore.userName.firstOrNull()
            val currentUsername = if (!localName.isNullOrBlank()) localName else (user?.displayName ?: "")

            _state.value = state.value.copy(
                emailState = SmartTaskTextFieldState(text = currentEmail),
                usernameState = SmartTaskTextFieldState(text = currentUsername)
            )
        }
    }

    fun onEvent(event: EditProfileEvent) {
        when (event) {
            is EditProfileEvent.EnteredUsername -> {
                _state.value = state.value.copy(
                    usernameState = state.value.usernameState.copy(
                        text = event.value,
                        error = null
                    )
                )
            }
            is EditProfileEvent.EnteredPassword -> {
                _state.value = state.value.copy(
                    passwordState = state.value.passwordState.copy(
                        text = event.value,
                        error = null
                    )
                )
            }
            is EditProfileEvent.TogglePasswordVisibility -> {
                _state.value = state.value.copy(
                    passwordState = state.value.passwordState.copy(
                        isPasswordVisible = !state.value.passwordState.isPasswordVisible
                    )
                )
            }
            is EditProfileEvent.SaveProfile -> {
                saveProfile()
            }
        }
    }

    private fun saveProfile() {
        viewModelScope.launch {
            val username = state.value.usernameState.text
            val password = state.value.passwordState.text

            val usernameError = ValidationUtil.validateUsername(username)
            val passwordError = if (password.isNotBlank()) ValidationUtil.validatePassword(password) else null

            if (usernameError != null || passwordError != null) {
                _state.value = state.value.copy(
                    usernameState = state.value.usernameState.copy(error = usernameError),
                    passwordState = state.value.passwordState.copy(error = passwordError)
                )
                return@launch
            }

            _state.value = state.value.copy(isLoading = true)

            when (val result = authRepository.updateProfile(username, password.ifBlank { null })) {
                is Resource.Success -> {
                    _state.value = state.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(UiText.StringResource(R.string.profile_updated_successfully)))
                    _eventFlow.emit(UiEvent.NavigateUp)
                }
                is Resource.Error -> {
                    _state.value = state.value.copy(isLoading = false)
                    _eventFlow.emit(UiEvent.ShowSnackBar(result.uiText ?: UiText.unknownError()))
                }
            }
        }
    }
}
