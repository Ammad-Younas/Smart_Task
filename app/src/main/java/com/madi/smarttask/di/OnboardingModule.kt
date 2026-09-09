package com.madi.smarttask.di

import android.content.SharedPreferences
import com.madi.smarttask.feature_onboarding.data.repository.OnboardingRepositoryImpl
import com.madi.smarttask.feature_onboarding.domain.repository.OnboardingRepository
import com.madi.smarttask.feature_onboarding.domain.usecase.LoadNextOnboardingPage
import com.madi.smarttask.feature_onboarding.domain.usecase.OnboardingUseCases
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
    fun provideOnboardingRepository(sharedPreferences: SharedPreferences) : OnboardingRepository {
        return OnboardingRepositoryImpl(sharedPreferences = sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideOnboardingUseCases(repository: OnboardingRepository) : OnboardingUseCases {
        return OnboardingUseCases(
            loadNextOnboardingPage = LoadNextOnboardingPage(repository)
        )
    }
}