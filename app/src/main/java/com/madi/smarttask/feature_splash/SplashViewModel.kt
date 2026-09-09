package com.madi.smarttask.feature_splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.feature_onboarding.domain.usecase.OnboardingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

    private val _isCheckComplete = MutableStateFlow(false)
    val isCheckComplete: StateFlow<Boolean> = _isCheckComplete.asStateFlow()

    private val _destination = MutableStateFlow<String?>(null)
    val destination: StateFlow<String?> = _destination.asStateFlow()

    fun checkOnboardingStatus() {
        viewModelScope.launch {
            val isCompleted = onboardingUseCases.isOnboardingCompleted().first()
            _destination.value = if (isCompleted) {
                Screen.DashboardScreen.route
            } else {
                Screen.OnBoardingScreen.route
            }
            delay(1000)
            _isCheckComplete.value = true
        }
    }
}
