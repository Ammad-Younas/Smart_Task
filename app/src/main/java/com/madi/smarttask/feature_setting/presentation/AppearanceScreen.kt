package com.madi.smarttask.feature_setting.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar

import com.madi.smarttask.feature_setting.domain.util.ThemeMode

@Composable
fun AppearanceScreen(
    onNavigateUp: () -> Unit = {}
) {
    var selectedTheme by remember { mutableStateOf(ThemeMode.SYSTEM_DEFAULT) }
    val themes = ThemeMode.entries

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = true,
            onNavigateUp = onNavigateUp,
            title = {
                Text(text = stringResource(id = R.string.appearance))
            }
        )

        Column(modifier = Modifier.fillMaxSize()) {
            themes.forEach { theme ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedTheme = theme }
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = theme.toDisplayString(),
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(
                        selected = selectedTheme == theme,
                        onClick = { selectedTheme = theme }
                    )
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
