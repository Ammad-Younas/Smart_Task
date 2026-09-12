package com.madi.smarttask.core.presentation.navigation

sealed class Screen(val route: String) {
    object OnBoardingScreen : Screen("onboarding_screen")
    object NameScreen : Screen("name_screen")
    object HomeScreen : Screen("home_screen")
    object TaskScreen : Screen("task_screen")
    object NotificationScreen : Screen("notification_screen")
    object SettingScreen : Screen("setting_screen")
    
}
