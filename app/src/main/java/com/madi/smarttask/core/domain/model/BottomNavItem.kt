package com.madi.smarttask.core.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val route: String?,
    val icon : ImageVector?,
    val contentDescription: Int? = null,
    val alertCount: Int? = null
)
