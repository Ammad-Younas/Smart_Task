package com.madi.smarttask.feature_task.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.feature_onboarding.domain.usecase.OnboardingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private var hasCheckedOnboarding = false

    fun checkOnboardingStatus() {
        if (hasCheckedOnboarding) return
        hasCheckedOnboarding = true
        viewModelScope.launch {
            val isCompleted = onboardingUseCases.isOnboardingCompleted().first()
            if (!isCompleted) {
                _eventFlow.emit(UiEvent.NavigateToOnboarding)
            }
        }
    }

    sealed class UiEvent {
        data object NavigateToOnboarding : UiEvent()
    }
}
