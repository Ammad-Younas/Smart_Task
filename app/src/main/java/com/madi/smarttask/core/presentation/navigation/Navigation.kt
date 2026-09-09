package com.madi.smarttask.core.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.madi.smarttask.feature_onboarding.presentation.OnboardingScreen
import com.madi.smarttask.feature_task.dashboard.DashboardScreen

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
                onOnboardingFinished = {
                    navController.popBackStack()
                    navController.navigate(Screen.DashboardScreen.route)
                }
            )
        }
        composable(
            route = Screen.DashboardScreen.route
        ) {
            DashboardScreen()
        }
    }
}
