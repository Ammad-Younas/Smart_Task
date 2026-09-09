package com.madi.smarttask.feature_onboarding.presentation

import com.madi.smarttask.feature_onboarding.domain.model.Boarding

data class OnboardingState(
    val pages: List<Boarding> = emptyList(),
    val isCompleted: Boolean = false
)
