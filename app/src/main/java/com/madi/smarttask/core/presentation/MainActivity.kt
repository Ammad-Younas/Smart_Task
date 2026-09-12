package com.madi.smarttask.core.presentation

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.madi.smarttask.core.presentation.component.SmartTaskScaffold
import com.madi.smarttask.core.presentation.navigation.Navigation
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.presentation.ui.theme.SmartTaskTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition { viewModel.isLoading.value }
        }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )
        setContent {
            SmartTaskTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                val snackbarHostState = remember { SnackbarHostState() }

                val bottomNavRoutes = listOf(
                    Screen.HomeScreen.route,
                    Screen.TaskScreen.route,
                    Screen.NotificationScreen.route,
                    Screen.SettingScreen.route
                )
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmartTaskScaffold(
                        modifier = Modifier.fillMaxSize(),
                        currentRoute = currentRoute,
                        showBottomBar = currentRoute in bottomNavRoutes,
                        snackbarHostState = snackbarHostState,
                        onNavigate = { route ->
                            if (currentRoute != route) {
                                navController.navigate(route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    ) {
                        Navigation(
                            navController = navController,
                            startDestination = viewModel.startDestination.value
                        )
                    }
                }
            }
        }
    }
}