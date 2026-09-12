package com.madi.smarttask.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madi.smarttask.feature_name.presentation.NameScreen
import com.madi.smarttask.feature_onboarding.presentation.OnboardingScreen
import com.madi.smarttask.feature_setting.SettingScreen
import com.madi.smarttask.feature_task.home.presentation.HomeScreen
import com.madi.smarttask.feature_notification.presentation.NotificationScreen
import com.madi.smarttask.feature_task.task.presentation.TaskScreen

@Composable
fun Navigation(
    navController: NavHostController,
    startDestination: String
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
            route = Screen.HomeScreen.route
        ) {
            HomeScreen()
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
        composable(
            route = Screen.NameScreen.route
        ) {
            NameScreen(
                onNavigate = { route ->
                    navController.popBackStack()
                    navController.navigate(route)
                }
            )
        }
    }
}
