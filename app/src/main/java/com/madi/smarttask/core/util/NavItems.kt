package com.madi.smarttask.core.util

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.model.BottomNavItem
import com.madi.smarttask.core.presentation.navigation.Screen

object NavItems {
    val NAV_ITEMS = listOf(
        BottomNavItem(
            route = Screen.Home.route,
            icon = Icons.Outlined.Home,
            contentDescription = R.string.home
        ),
        BottomNavItem(
            route = Screen.TaskScreen.route,
            icon = Icons.Outlined.Add,
            contentDescription = R.string.task
        ),
        BottomNavItem(
            route = Screen.NotificationScreen.route,
            icon = Icons.Outlined.Notifications,
            contentDescription = R.string.notifications
        ),
        BottomNavItem(
            route = Screen.SettingScreen.route,
            icon = Icons.Outlined.Settings,
            contentDescription = R.string.setting
        )
    )
}