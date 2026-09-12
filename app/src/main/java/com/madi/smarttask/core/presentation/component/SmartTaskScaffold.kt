package com.madi.smarttask.core.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.core.domain.model.BottomNavItem
import com.madi.smarttask.core.util.NavItems

@Composable
fun SmartTaskScaffold(
    modifier: Modifier = Modifier,
    currentRoute: String? = null,
    onNavigate: (String) -> Unit = {},
    snackbarHostState: SnackbarHostState,
    topBar: @Composable () -> Unit = {},
    showBottomBar: Boolean = true,
    bottomNavItemsList: List<BottomNavItem> = NavItems.NAV_ITEMS,
    content: @Composable () -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = topBar,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = MaterialTheme.colorScheme.background,
                    tonalElevation = 0.dp,
                    windowInsets = NavigationBarDefaults.windowInsets
                ) {
                    bottomNavItemsList.forEach { bottomNavItem ->
                        val isSelected = currentRoute == bottomNavItem.route
                        BottomNavItem(
                            icon = bottomNavItem.icon,
                            contentDescription = bottomNavItem.contentDescription?.let { stringResource(it) },
                            selected = isSelected,
                            alertCount = bottomNavItem.alertCount,
                            onClick = {
                                bottomNavItem.route?.let { route ->
                                    onNavigate(route)
                                }
                            },
                            enabled = bottomNavItem.icon != null,
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            content()
        }
    }
}
