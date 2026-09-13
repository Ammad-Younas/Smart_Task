package com.madi.smarttask.feature_task.task.presentation.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.madi.smarttask.R
import com.madi.smarttask.feature_task.task.presentation.util.TaskTab

@Composable
fun TaskTabRow(
    modifier: Modifier = Modifier,
    selectedTab: TaskTab,
    onTabSelected: (TaskTab) -> Unit,
) {
    PrimaryTabRow(
        selectedTabIndex = selectedTab.ordinal,
        modifier = modifier,
        containerColor = Color.Transparent,
        divider = {}
    ) {
        Tab(
            selected = selectedTab == TaskTab.DASHBOARD,
            onClick = { onTabSelected(TaskTab.DASHBOARD) },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            text = { Text(text = stringResource(R.string.dashboard)) },
        )
        Tab(
            selected = selectedTab == TaskTab.CREATE_TASK,
            onClick = { onTabSelected(TaskTab.CREATE_TASK) },
            selectedContentColor = MaterialTheme.colorScheme.primary,
            unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            text = { Text(text = stringResource(R.string.create_task)) },
        )
    }
}
