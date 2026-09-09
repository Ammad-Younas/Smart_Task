package com.madi.smarttask.feature_onboarding.domain.repository

import com.madi.smarttask.feature_onboarding.domain.model.Boarding

interface OnboardingRepository {
    fun loadNextOnboardingPage(): Boarding
}