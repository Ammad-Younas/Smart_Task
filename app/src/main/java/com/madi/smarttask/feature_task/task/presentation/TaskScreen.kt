package com.madi.smarttask.feature_task.task.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.ui.theme.NavActionIconSize
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.feature_task.task.presentation.component.CreateTask
import com.madi.smarttask.feature_task.task.presentation.component.Dashboard
import com.madi.smarttask.feature_task.task.presentation.component.TaskTabRow
import com.madi.smarttask.feature_task.task.presentation.util.TaskTab

@Composable
fun TaskScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: TaskViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
) {
    var selectedTab by remember { mutableStateOf(TaskTab.DASHBOARD) }
    val state by viewModel.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = false,
            title = {
                Text(
                    text = stringResource(R.string.my_tasks),
                )
            },
            navActions = {
                IconButton(
                    onClick = {}
                ){
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        modifier = Modifier.size(NavActionIconSize)
                    )
                }
            }
        )
        TaskTabRow(
            selectedTab = selectedTab,
        ) {
            selectedTab = it
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceMedium),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            when (selectedTab) {
                TaskTab.DASHBOARD -> {
                    Dashboard(
                        stats = state.stats,
                        viewModel = viewModel,
                        onNavigate = onNavigate
                    )
                }
                TaskTab.CREATE_TASK -> {
                    CreateTask(
                        viewModel = viewModel,
                        snackbarHostState = snackbarHostState,
                        onTaskCreated = {
                            selectedTab = TaskTab.DASHBOARD
                        }
                    )
                }
            }
        }
    }
}
