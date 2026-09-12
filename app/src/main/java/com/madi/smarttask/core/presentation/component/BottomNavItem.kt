package com.madi.smarttask.core.presentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight

@Composable
fun RowScope.BottomNavItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    contentDescription: String? = null,
    selected: Boolean = false,
    alertCount: Int? = null,
    selectedColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    unselectedColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
    onClick: () -> Unit = {},
    enabled: Boolean = true,
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        icon = {
            if (icon != null) {
                if (alertCount != null && alertCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge {
                                Text(
                                    text = if (alertCount > 99) "99+" else alertCount.toString()
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = icon,
                            contentDescription = contentDescription
                        )
                    }
                } else {
                    Icon(
                        imageVector = icon,
                        contentDescription = contentDescription
                    )
                }
            }
        },
        label = contentDescription?.let {
            {
                Text(
                    text = it,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                )
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = selectedColor,
            selectedTextColor = selectedColor,
            unselectedIconColor = unselectedColor,
            unselectedTextColor = unselectedColor,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
        )
    )
}
