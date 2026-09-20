package com.madi.smarttask.core.presentation.navigation

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.madi.smarttask.feature_name.presentation.NameScreen
import com.madi.smarttask.feature_notification.presentation.NotificationScreen
import com.madi.smarttask.feature_onboarding.presentation.OnboardingScreen
import com.madi.smarttask.feature_setting.SettingScreen
import com.madi.smarttask.feature_setting.presentation.AboutScreen
import com.madi.smarttask.feature_setting.presentation.AppearanceScreen
import com.madi.smarttask.feature_setting.presentation.CategoriesScreen
import com.madi.smarttask.feature_task.edit_task.presentation.EditTaskScreen
import com.madi.smarttask.feature_task.home.presentation.HomeScreen
import com.madi.smarttask.feature_task.task.presentation.TaskScreen
import com.madi.smarttask.feature_task.task_detail.presentation.TaskDetailScreen

@Composable
fun Navigation(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
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
            HomeScreen(
                onNavigate = navController::navigate
            )
        }
        composable(
            route = Screen.TaskScreen.route
        ) {
            TaskScreen(
                onNavigate = navController::navigate,
                snackbarHostState = snackbarHostState
            )
        }
        composable(
            route = Screen.NotificationScreen.route
        ) {
            NotificationScreen()
        }
        composable(
            route = Screen.SettingScreen.route
        ) {
            SettingScreen(
                onNavigate = navController::navigate,
            )
        }
        composable(
            route = Screen.AppearanceScreen.route
        ) {
            AppearanceScreen(
                onNavigateUp = navController::navigateUp
            )
        }
        composable(
            route = Screen.CategoriesScreen.route
        ) {
            CategoriesScreen(
                onNavigateUp = navController::navigateUp
            )
        }
        composable(
            route = Screen.AboutScreen.route
        ) {
            AboutScreen(
                onNavigateUp = navController::navigateUp
            )
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
        composable(
            route = Screen.TaskDetailScreen.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            TaskDetailScreen(
                onNavigateUp = navController::navigateUp,
                onEditClick = navController::navigate
            )
        }
        composable(
            route = Screen.EditTaskScreen.route,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                    defaultValue = -1L
                }
            )
        ) {
            EditTaskScreen(
                onNavigateUp = navController::navigateUp
            )
        }
    }
}
