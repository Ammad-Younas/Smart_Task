package com.madi.smarttask.feature_setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.feature_setting.presentation.component.SettingItem

@Composable
fun SettingScreen(
    onNavigate: (String) -> Unit = {},
) {
    val scrollState = rememberScrollState()

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
        }
    }
}
