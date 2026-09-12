package com.madi.smarttask.feature_onboarding.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.feature_onboarding.domain.usecase.OnboardingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(OnboardingState())
    val state: StateFlow<OnboardingState> = _state.asStateFlow()

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        _state.value = _state.value.copy(
            pages = onboardingUseCases.getOnboardingPages()
        )
    }

    fun onEvent(event: OnboardingEvent) {
        when (event) {
            is OnboardingEvent.CompleteOnboarding -> {
                viewModelScope.launch {
                    onboardingUseCases.setOnboardingCompleted(true)
                    _eventFlow.emit(UiEvent.Navigate(Screen.Home.route))
                }
            }
        }
    }
}
