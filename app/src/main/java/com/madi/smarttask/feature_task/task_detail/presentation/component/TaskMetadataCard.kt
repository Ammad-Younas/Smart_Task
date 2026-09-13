package com.madi.smarttask.feature_task.task_detail.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Flag
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.ui.theme.TaskCompleted
import com.madi.smarttask.core.domain.model.TaskDetail
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.core.presentation.ui.theme.getPriorityColor

@Composable
fun TaskMetadataCard(
    taskDetail: TaskDetail,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            val timeToFormat = if (taskDetail.task.dueDate > 0) taskDetail.task.dueDate else System.currentTimeMillis()

            MetadataRow(
                icon = Icons.Default.CalendarToday,
                label = stringResource(R.string.due_date),
                value = DateFormatUtil.timestampToFormatedString(timeToFormat, "MMM dd, yyyy")
            )
            DividerRow()
            MetadataRow(
                icon = Icons.Default.Schedule,
                label = stringResource(R.string.due_time),
                value = DateFormatUtil.timestampToFormatedString(timeToFormat, "hh:mm a")
            )
            DividerRow()
            MetadataRow(
                icon = Icons.Outlined.Flag,
                label = stringResource(R.string.priority),
                valueComponent = {
                    val priorityColor = getPriorityColor(taskDetail.task.priority)
                    DetailChip(
                        text = taskDetail.task.priority.name.lowercase().replaceFirstChar { it.uppercase() },
                        containerColor = priorityColor.copy(alpha = 0.15f),
                        contentColor = priorityColor
                    )
                }
            )
            DividerRow()
            MetadataRow(
                icon = Icons.Outlined.Folder,
                label = stringResource(R.string.category),
                value = taskDetail.task.category.name.lowercase().replaceFirstChar { it.uppercase() }
            )
            DividerRow()
            MetadataRow(
                icon = Icons.Outlined.CheckCircle,
                label = stringResource(R.string.status),
                valueComponent = {
                    DetailChip(
                        icon = Icons.Default.Check,
                        text = stringResource(R.string.completed),
                        containerColor = TaskCompleted.copy(alpha = 0.15f),
                        contentColor = TaskCompleted
                    )
                }
            )
        }
    }
}
