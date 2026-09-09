package com.madi.smarttask.feature_onboarding.domain.usecase

import com.madi.smarttask.feature_onboarding.domain.repository.OnboardingRepository

class SetOnboardingCompleted(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(completed: Boolean = true) {
        repository.setOnboardingCompleted(completed)
    }
}
