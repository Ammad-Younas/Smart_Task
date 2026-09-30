package com.madi.smarttask.feature_setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.feature_setting.presentation.SettingViewModel
import com.madi.smarttask.feature_setting.presentation.component.SettingItem
import kotlinx.coroutines.flow.collectLatest

@Composable
fun SettingScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: SettingViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                else -> Unit
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(text = stringResource(R.string.logout_confirmation_title))
            },
            text = {
                Text(text = stringResource(R.string.logout_confirmation_message))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                    }
                ) {
                    Text(
                        text = stringResource(R.string.logout),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showLogoutDialog = false }
                ) {
                    Text(text = stringResource(R.string.cancel))
                }
            }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = false,
            title = {
                Text(text = stringResource(id = R.string.settings))
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            SettingItem(
                icon = Icons.Outlined.Person,
                title = stringResource(R.string.edit_profile),
                subtitle = stringResource(R.string.edit_profile_subtitle),
                onClick = { onNavigate(Screen.EditProfileScreen.route) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = Icons.Outlined.LightMode,
                title = stringResource(R.string.appearance),
                subtitle = stringResource(R.string.light_dark_system),
                onClick = { onNavigate(Screen.AppearanceScreen.route) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = Icons.Outlined.Folder,
                title = stringResource(R.string.categories),
                subtitle = stringResource(R.string.manage_your_categories),
                onClick = { onNavigate(Screen.CategoriesScreen.route) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = Icons.Outlined.Info,
                title = stringResource(R.string.about),
                subtitle = stringResource(R.string.app_version),
                onClick = { onNavigate(Screen.AboutScreen.route) }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            SettingItem(
                icon = Icons.AutoMirrored.Outlined.Logout,
                title = stringResource(R.string.logout),
                subtitle = stringResource(R.string.logout_subtitle),
                onClick = { showLogoutDialog = true }
            )
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
