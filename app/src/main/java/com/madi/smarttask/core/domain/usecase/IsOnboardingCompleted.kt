package com.madi.smarttask.core.domain.usecase

import com.madi.smarttask.feature_onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow

class IsOnboardingCompleted(
    private val repository: OnboardingRepository
) {
    operator fun invoke(): Flow<Boolean> {
        return repository.isOnboardingCompleted()
    }
}