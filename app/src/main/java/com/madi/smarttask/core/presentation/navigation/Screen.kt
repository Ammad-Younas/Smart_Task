package com.madi.smarttask.core.presentation.navigation

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object OnBoardingScreen : Screen("onboarding_screen")
    object DashboardScreen : Screen("home_screen")
}
