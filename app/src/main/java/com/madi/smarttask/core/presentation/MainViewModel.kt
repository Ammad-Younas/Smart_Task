package com.madi.smarttask.core.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.feature_onboarding.domain.usecase.OnboardingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

import com.madi.smarttask.feature_name.domain.usecase.NameUseCases
import kotlinx.coroutines.flow.combine

@HiltViewModel
class MainViewModel @Inject constructor(
    onboardingUseCases: OnboardingUseCases,
    nameUseCases: NameUseCases
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _startDestination = mutableStateOf(Screen.OnBoardingScreen.route)
    val startDestination: State<String> = _startDestination

    init {
        combine(
            onboardingUseCases.isOnboardingCompleted(),
            nameUseCases.getUserName()
        ) { isCompleted, name ->
            if (!isCompleted) {
                _startDestination.value = Screen.OnBoardingScreen.route
            } else if (name.isNullOrBlank()) {
                _startDestination.value = Screen.NameScreen.route
            } else {
                _startDestination.value = Screen.HomeScreen.route
            }
            delay(300.milliseconds)
            _isLoading.value = false
        }.launchIn(viewModelScope)
    }
}
