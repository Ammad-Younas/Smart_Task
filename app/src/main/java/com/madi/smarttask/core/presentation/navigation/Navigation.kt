package com.madi.smarttask.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madi.smarttask.feature_onboarding.presentation.OnboardingScreen
import com.madi.smarttask.feature_setting.SettingScreen
import com.madi.smarttask.feature_task.home.Home
import com.madi.smarttask.feature_task.notification.NotificationScreen
import com.madi.smarttask.feature_task.task.TaskScreen

@Composable
fun Navigation(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            route = Screen.OnBoardingScreen.route
        ) {
            OnboardingScreen(
                onNavigate = { route ->
                    navController.popBackStack()
                    navController.navigate(route)
                }
            )
        }
        composable(
            route = Screen.Home.route
        ) {
            Home(
                onNavigateToOnboarding = {
                    navController.navigate(Screen.OnBoardingScreen.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        composable(
            route = Screen.TaskScreen.route
        ) {
            TaskScreen()
        }
        composable(
            route = Screen.NotificationScreen.route
        ) {
            NotificationScreen()
        }
        composable(
            route = Screen.SettingScreen.route
        ) {
            SettingScreen()
        }
    }
}
