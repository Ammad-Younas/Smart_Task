package com.madi.smarttask.feature_task.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.ui.theme.SpaceLarge
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.core.presentation.ui.theme.SpaceSmall
import com.madi.smarttask.feature_task.home.domain.model.Category
import com.madi.smarttask.feature_task.home.domain.model.Priority
import com.madi.smarttask.feature_task.home.domain.model.Task
import com.madi.smarttask.feature_task.home.presentation.component.Greeting
import com.madi.smarttask.feature_task.home.presentation.component.Progress
import com.madi.smarttask.feature_task.home.presentation.component.TaskItem

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceMedium)
            .windowInsetsPadding(
                WindowInsets.safeDrawing.only(WindowInsetsSides.Horizontal)
            )
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
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
            TaskItem(
                task = Task(
                    title = "Task 1",
                    description = "Description 1",
                    category = Category.WORK,
                    priority = Priority.HIGH,
                    dueDate = System.currentTimeMillis()
                )
            )
            TaskItem(task = Task(
                title = "Task 2",
                description = "Description 2",
                category = Category.PERSONAL,
                priority = Priority.LOW,
                dueDate = System.currentTimeMillis() + 86400000
            )
            )
            TaskItem(task = Task(
                title = "Task 3",
                description = "Description 3",
                category = Category.SHOPPING,
                priority = Priority.MEDIUM,
                dueDate = System.currentTimeMillis() + 172800000
            )
            )
            TaskItem(task = Task(
                title = "Task 4",
                description = "Description 4",
                category = Category.WORK,
                priority = Priority.HIGH,
                dueDate = System.currentTimeMillis() + 259200000
            )
            )
        }
    }
}
