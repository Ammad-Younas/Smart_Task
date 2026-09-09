package com.madi.smarttask.feature_onboarding.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.madi.smarttask.R
import com.madi.smarttask.core.util.Constants
import com.madi.smarttask.feature_onboarding.domain.model.Boarding
import com.madi.smarttask.feature_onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class OnboardingRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : OnboardingRepository {

    private object PreferencesKeys {
        val ONBOARDING_COMPLETED = booleanPreferencesKey(Constants.KEY_ONBOARDING_COMPLETED)
    }

    override fun getOnboardingPages(): List<Boarding> {
        return listOf(
            Boarding(
                image = R.drawable.first,
                title = R.string.onboarding_title_1,
                description = R.string.onboarding_desc_1
            ),
            Boarding(
                image = R.drawable.second,
                title = R.string.onboarding_title_2,
                description = R.string.onboarding_desc_2
            ),
            Boarding(
                image = R.drawable.three,
                title = R.string.onboarding_title_3,
                description = R.string.onboarding_desc_3
            )
        )
    }

    override suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] = completed
        }
    }

    override fun isOnboardingCompleted(): Flow<Boolean> {
        return dataStore.data.map { preferences ->
            preferences[PreferencesKeys.ONBOARDING_COMPLETED] ?: false
        }
    }
}
