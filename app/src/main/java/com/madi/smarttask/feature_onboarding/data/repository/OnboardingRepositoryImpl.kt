package com.madi.smarttask.feature_onboarding.data.repository

import android.content.SharedPreferences
import com.madi.smarttask.feature_onboarding.domain.model.Boarding
import com.madi.smarttask.feature_onboarding.domain.repository.OnboardingRepository

class OnboardingRepositoryImpl(
    private val sharedPreferences: SharedPreferences,
) : OnboardingRepository {
    override fun loadNextOnboardingPage(): Boarding {
        TODO("Not yet implemented")
    }
}