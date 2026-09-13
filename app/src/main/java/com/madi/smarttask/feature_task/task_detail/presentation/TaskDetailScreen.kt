package com.madi.smarttask.feature_task.task_detail.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.ui.theme.NavActionIconSize
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.presentation.component.DeleteTaskButton
import com.madi.smarttask.feature_task.task_detail.presentation.component.TaskDetailHeader
import com.madi.smarttask.feature_task.task_detail.presentation.component.TaskMetadataCard

@Composable
fun TaskDetailScreen(
    onNavigateUp: () -> Unit = {},
    onEditClick: (String) -> Unit = {},
    viewModel: TaskDetailViewModel = hiltViewModel()
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = true,
            onNavigateUp = onNavigateUp,
            title = {
                Text(
                    text = stringResource(R.string.task_details),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            },
            navActions = {
                IconButton(onClick = { onEditClick(Screen.EditTaskScreen.route) }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = stringResource(R.string.edit),
                        modifier = Modifier.size(NavActionIconSize),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TaskDetailHeader(task = state.taskDetail.task)

            Text(
                text = state.taskDetail.task.description ?: "",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * 1.3
            )
            TaskMetadataCard(taskDetail = state.taskDetail)
            Spacer(modifier = Modifier.weight(1f))
            DeleteTaskButton(onClick = { viewModel.onEvent(TaskDetailEvent.DeleteTask) })
        }
    }
}