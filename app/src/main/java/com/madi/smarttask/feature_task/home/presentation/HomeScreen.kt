package com.madi.smarttask.feature_task.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.presentation.component.TaskItem
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.presentation.ui.theme.SpaceLarge
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.core.presentation.ui.theme.SpaceSmall
import com.madi.smarttask.feature_task.home.presentation.component.Greeting
import com.madi.smarttask.feature_task.home.presentation.component.Progress

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigate: (String) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceMedium)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            Greeting(viewModel = viewModel)
            Spacer(Modifier.height(SpaceLarge))
            Progress(viewModel = viewModel)
            Spacer(Modifier.height(SpaceLarge))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.upcoming_tasks),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                TextButton(
                    onClick = {

                    }
                ) {
                    Text(
                        text = stringResource(R.string.see_all),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(Modifier.height(SpaceSmall))
        }
        items(3) {
            TaskItem(
                task = Task(
                    title = "Task 1",
                    category = Category.WORK,
                    priority = Priority.HIGH,
                    dueDate = System.currentTimeMillis()
                ),
                onClick = { onNavigate(Screen.TaskDetailScreen.route) }
            )
        }
    }
}
