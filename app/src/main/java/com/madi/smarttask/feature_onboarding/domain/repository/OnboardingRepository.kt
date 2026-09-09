package com.madi.smarttask.feature_onboarding.domain.repository

import com.madi.smarttask.feature_onboarding.domain.model.Boarding
import kotlinx.coroutines.flow.Flow

interface OnboardingRepository {
    fun getOnboardingPages(): List<Boarding>
    suspend fun setOnboardingCompleted(completed: Boolean)
    fun isOnboardingCompleted(): Flow<Boolean>
}
