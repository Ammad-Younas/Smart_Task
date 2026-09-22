package com.madi.smarttask.core.presentation.navigation

sealed class Screen(val route: String) {
    object OnBoardingScreen : Screen("onboarding_screen")
    object NameScreen : Screen("name_screen")
    object HomeScreen : Screen("home_screen")
    object TaskScreen : Screen("task_screen?tab={tab}") {
        fun passTab(tab: String = "create_task") = "task_screen?tab=$tab"
    }
    object NotificationScreen : Screen("notification_screen")
    object SettingScreen : Screen("setting_screen")
    object TaskDetailScreen : Screen("task_detail_screen/{taskId}") {
        fun passTaskId(taskId: Long) = "task_detail_screen/$taskId"
    }
    object EditTaskScreen : Screen("edit_task_screen/{taskId}") {
        fun passTaskId(taskId: Long) = "edit_task_screen/$taskId"
    }
    object AppearanceScreen : Screen("appearance_screen")
    object CategoriesScreen : Screen("categories_screen")
    object AboutScreen : Screen("about_screen")
}
