package com.madi.smarttask.feature_name.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState
import com.madi.smarttask.feature_name.domain.usecase.NameUseCases
import com.madi.smarttask.core.domain.util.ValidationUtil
import com.madi.smarttask.core.util.UiEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.madi.smarttask.core.presentation.navigation.Screen

@HiltViewModel
class NameViewModel @Inject constructor(
    private val nameUseCases: NameUseCases
) : ViewModel() {

    private val _nameState = mutableStateOf(SmartTaskTextFieldState())
    val nameState: State<SmartTaskTextFieldState> = _nameState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun onEvent(event: NameEvent) {
        when (event) {
            is NameEvent.OnNameChange -> {
                _nameState.value = _nameState.value.copy(
                    text = event.name,
                    error = null
                )
            }
            is NameEvent.SaveName -> {
                val name = _nameState.value.text
                val error = ValidationUtil.validateUsername(name)
                
                if (error != null) {
                    _nameState.value = _nameState.value.copy(error = error)
                    return
                }
                
                viewModelScope.launch {
                    nameUseCases.saveUserName(name)
                    _eventFlow.emit(UiEvent.Navigate(Screen.HomeScreen.route))
                }
            }
        }
    }
}
