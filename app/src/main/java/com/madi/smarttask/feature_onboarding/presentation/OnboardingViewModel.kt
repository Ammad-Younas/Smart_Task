package com.madi.smarttask.feature_onboarding.presentation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.madi.smarttask.feature_onboarding.domain.usecase.OnboardingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val onboardingUseCases: OnboardingUseCases
) : ViewModel() {

}