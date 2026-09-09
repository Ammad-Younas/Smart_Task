package com.madi.smarttask.feature_onboarding.domain.usecase

import com.madi.smarttask.core.domain.usecase.IsOnboardingCompleted

data class OnboardingUseCases(
    val getOnboardingPages: GetOnboardingPages,
    val setOnboardingCompleted: SetOnboardingCompleted,
    val isOnboardingCompleted: IsOnboardingCompleted
)
