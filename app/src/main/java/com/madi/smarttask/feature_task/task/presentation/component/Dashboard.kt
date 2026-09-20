package com.madi.smarttask.feature_task.task.presentation.component

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
    var selectedCategory by remember { mutableStateOf<Category?>(null) }
    var selectedStatus by remember { mutableStateOf<TaskStatus?>(TaskStatus.TOTAL) }
    var selectedPriority by remember { mutableStateOf<Priority?>(null) }

    val state by viewModel.state.collectAsState()

    val filteredTasks = remember(state.tasks, selectedCategory, selectedStatus, selectedPriority) {
        state.tasks.filter { task ->
            val currentTime = System.currentTimeMillis()
            val matchesCategory = selectedCategory == null || task.category == selectedCategory
            val matchesPriority = selectedPriority == null || task.priority == selectedPriority
            val matchesStatus = when (selectedStatus) {
                null, TaskStatus.TOTAL -> true
                TaskStatus.COMPLETED -> task.isCompleted
                TaskStatus.PENDING -> !task.isCompleted && (task.dueDate == 0L || task.dueDate >= currentTime)
                TaskStatus.OVERDUE -> !task.isCompleted && task.dueDate in 1..<currentTime
            }
            matchesCategory && matchesPriority && matchesStatus
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
                    modifier = Modifier.fillMaxWidth().padding(top = SpaceSmall),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(Category.entries.toTypedArray()) { category ->
                            CategoryChip(
                                category = category,
                                isSelected = selectedCategory == category,
                                onCategorySelected = {
                                    selectedCategory = if (selectedCategory == it) null else it
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
        items(filteredTasks) { task ->
            TaskItem(
                task = task,
                onCheckedChange = { isChecked ->
                    viewModel.onEvent(TaskEvent.ToggleTaskCompletion(task, isChecked))
                },
                onClick = {
                    onNavigate(Screen.TaskDetailScreen.route)
                }
            )
        }
    }
}
