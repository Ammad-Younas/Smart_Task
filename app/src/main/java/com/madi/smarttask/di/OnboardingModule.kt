package com.madi.smarttask.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.madi.smarttask.feature_onboarding.data.repository.OnboardingRepositoryImpl
import com.madi.smarttask.feature_onboarding.domain.repository.OnboardingRepository
import com.madi.smarttask.feature_onboarding.domain.usecase.GetOnboardingPages
import com.madi.smarttask.core.domain.usecase.IsOnboardingCompleted
import com.madi.smarttask.feature_onboarding.domain.usecase.OnboardingUseCases
import com.madi.smarttask.feature_onboarding.domain.usecase.SetOnboardingCompleted
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OnboardingModule {

    @Provides
    @Singleton
    fun provideOnboardingRepository(dataStore: DataStore<Preferences>): OnboardingRepository {
        return OnboardingRepositoryImpl(dataStore = dataStore)
    }

    @Provides
    @Singleton
    fun provideOnboardingUseCases(repository: OnboardingRepository): OnboardingUseCases {
        return OnboardingUseCases(
            getOnboardingPages = GetOnboardingPages(repository),
            setOnboardingCompleted = SetOnboardingCompleted(repository),
            isOnboardingCompleted = IsOnboardingCompleted(repository)
        )
    }
}
