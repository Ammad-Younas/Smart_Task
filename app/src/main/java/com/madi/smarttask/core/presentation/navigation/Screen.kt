package com.madi.smarttask.core.presentation.navigation

sealed class Screen(val route: String) {
    object OnBoardingScreen : Screen("onboarding_screen")
}