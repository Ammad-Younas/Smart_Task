package com.madi.smarttask.feature_notification.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.feature_notification.domain.model.Notification
import com.madi.smarttask.feature_notification.domain.util.NotificationCategory
import com.madi.smarttask.feature_notification.presentation.component.NotificationItem

@Composable
fun NotificationScreen(
    onNavigateUp: () -> Unit = {}
) {
    val mockNotifications = remember {
        listOf(
            Notification(
                id = "1",
                category = NotificationCategory.REMINDER,
                title = "Task Reminder",
                message = "Prepare project presentation",
                detail = "Today, 10:00 AM",
                timeLabel = "Now"
            ),
            Notification(
                id = "2",
                category = NotificationCategory.OVERDUE,
                title = "Task Overdue",
                message = "Submit assignment",
                detail = "Was due yesterday",
                timeLabel = "2h ago"
            ),
            Notification(
                id = "3",
                category = NotificationCategory.COMPLETED,
                title = "Task Completed",
                message = "You completed 5 tasks today!",
                detail = null,
                timeLabel = "5h ago"
            ),
            Notification(
                id = "4",
                category = NotificationCategory.UPDATE,
                title = "App Update",
                message = "New features are available",
                detail = null,
                timeLabel = "1d ago"
            ),
            Notification(
                id = "5",
                category = NotificationCategory.DEADLINE,
                title = "Upcoming Deadline",
                message = "Design review meeting",
                detail = "Tomorrow, 9:00 AM",
                timeLabel = "1d ago"
            )
        )
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = false,
            title = {
                Text(text = stringResource(id = R.string.notification))
            }
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(mockNotifications) { notification ->
                NotificationItem(notification = notification)
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}
