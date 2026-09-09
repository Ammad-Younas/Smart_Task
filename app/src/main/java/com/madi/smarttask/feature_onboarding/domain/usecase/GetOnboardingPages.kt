package com.madi.smarttask.feature_onboarding.domain.usecase

import com.madi.smarttask.feature_onboarding.domain.model.Boarding
import com.madi.smarttask.feature_onboarding.domain.repository.OnboardingRepository

class GetOnboardingPages(
    private val repository: OnboardingRepository
) {
    operator fun invoke(): List<Boarding> {
        return repository.getOnboardingPages()
    }
}
