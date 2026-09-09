package com.madi.smarttask.feature_onboarding.presentation

sealed class OnboardingEvent {
    object LoadNextOnboardingPage : OnboardingEvent()
}