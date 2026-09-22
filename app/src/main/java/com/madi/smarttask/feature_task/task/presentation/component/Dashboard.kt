package com.madi.smarttask.feature_task.task.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.TaskStatus
import com.madi.smarttask.core.presentation.component.TaskItem
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.presentation.ui.theme.SpaceSmall
import com.madi.smarttask.feature_task.task.domain.model.Stats
import com.madi.smarttask.feature_task.task.presentation.TaskEvent
import com.madi.smarttask.feature_task.task.presentation.TaskViewModel

@Composable
fun Dashboard(
    stats: Stats? = null,
    onNavigate: (String) -> Unit = {},
    viewModel: TaskViewModel,
) {
    val state by viewModel.state.collectAsState()

    var selectedCategoryName by remember { mutableStateOf<String?>(null) }
    var selectedStatus by remember { mutableStateOf<TaskStatus?>(TaskStatus.TOTAL) }
    var selectedPriority by remember { mutableStateOf<Priority?>(null) }

    val categoriesList = remember(state.categories) {
        if (state.categories.isNotEmpty()) {
            state.categories.map { it.name }
        } else {
            Category.entries.map {
                it.name.lowercase().replaceFirstChar { char -> char.uppercase() }
            }
        }
    }

    val filteredTasks =
        remember(state.tasks, selectedCategoryName, selectedStatus, selectedPriority, state.searchQuery) {
            state.tasks.filter { task ->
                val currentTime = System.currentTimeMillis()
                val matchesCategory = selectedCategoryName == null || task.category.name.equals(
                    selectedCategoryName,
                    ignoreCase = true
                )
                val matchesPriority = selectedPriority == null || task.priority == selectedPriority
                val matchesStatus = when (selectedStatus) {
                    null,
                    TaskStatus.TOTAL -> true
                    TaskStatus.COMPLETED -> task.isCompleted
                    TaskStatus.PENDING -> !task.isCompleted && (task.dueDate == 0L || task.dueDate >= currentTime)
                    TaskStatus.OVERDUE -> !task.isCompleted && task.dueDate in 1..<currentTime
                }
                val matchesSearch = state.searchQuery.isBlank() ||
                        task.title.contains(state.searchQuery, ignoreCase = true) ||
                        (task.description?.contains(state.searchQuery, ignoreCase = true) == true)

                matchesCategory && matchesPriority && matchesStatus && matchesSearch
            }
        }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            Column {
                StatsCard(stats = stats)
                Spacer(Modifier.height(SpaceSmall))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = SpaceSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(categoriesList) { categoryName ->
                            CategoryChip(
                                categoryName = categoryName,
                                isSelected = selectedCategoryName.equals(
                                    categoryName,
                                    ignoreCase = true
                                ),
                                onCategorySelected = { name ->
                                    selectedCategoryName = if (selectedCategoryName.equals(
                                            name,
                                            ignoreCase = true
                                        )
                                    ) null else name
                                },
                            )
                        }
                    }
                    TaskFilter(
                        selectedStatus = selectedStatus,
                        onStatusSelected = { selectedStatus = it ?: TaskStatus.TOTAL },
                        selectedPriority = selectedPriority,
                        onPrioritySelected = { selectedPriority = it },
                    )
                }
                Spacer(Modifier.height(SpaceSmall))
            }
        }
        if (filteredTasks.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_tasks_yet),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            items(filteredTasks) { task ->
                TaskItem(
                    task = task,
                    onCheckedChange = { isChecked ->
                        viewModel.onEvent(TaskEvent.ToggleTaskCompletion(task, isChecked))
                    },
                    onClick = {
                        onNavigate(Screen.TaskDetailScreen.passTaskId(task.id))
                    }
                )
            }
        }
    }
}
