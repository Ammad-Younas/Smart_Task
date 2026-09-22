package com.madi.smarttask.feature_notification.presentation.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import com.madi.smarttask.R
import com.madi.smarttask.SmartTaskApplication
import com.madi.smarttask.core.data.local.dao.NotificationDao
import com.madi.smarttask.core.data.local.entity.NotificationEntity
import com.madi.smarttask.core.presentation.MainActivity
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.feature_notification.domain.util.NotificationCategory
import com.madi.smarttask.feature_notification.domain.util.TaskNotificationScheduler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class TaskNotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationDao: NotificationDao

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != TaskNotificationScheduler.ACTION_TASK_NOTIFICATION) {
            return
        }
        val taskId = intent.getLongExtra(TaskNotificationScheduler.EXTRA_TASK_ID, -1L)
        if (taskId == -1L) {
            return
        }
        val title = intent.getStringExtra(TaskNotificationScheduler.EXTRA_TASK_TITLE)
            ?: context.getString(R.string.tasks)
        val categoryName = intent.getStringExtra(TaskNotificationScheduler.EXTRA_CATEGORY)
            ?: NotificationCategory.REMINDER.name
        val dueDate = intent.getLongExtra(TaskNotificationScheduler.EXTRA_DUE_DATE, 0L)
        val category = try {
            NotificationCategory.valueOf(categoryName)
        } catch (_: IllegalArgumentException) {
            NotificationCategory.REMINDER
        }
        showNotification(
            context = context,
            taskId = taskId,
            title = title,
            category = category,
            dueDate = dueDate
        )
    }

    private fun showNotification(
        context: Context,
        taskId: Long,
        title: String,
        category: NotificationCategory,
        dueDate: Long
    ) {
        val activityIntent = Intent(
            context,
            MainActivity::class.java
        ).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId.toInt(),
            activityIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or
                    PendingIntent.FLAG_IMMUTABLE
        )
        val (notifTitle, notifMessage) = when (category) {
            NotificationCategory.REMINDER ->
                context.getString(R.string.notification_title_reminder) to
                        context.getString(
                            R.string.notification_msg_reminder,
                            title
                        )

            NotificationCategory.DEADLINE ->
                context.getString(R.string.notification_title_deadline) to
                        context.getString(
                            R.string.notification_msg_deadline,
                            title
                        )

            NotificationCategory.OVERDUE ->
                context.getString(R.string.notification_title_overdue) to
                        context.getString(
                            R.string.notification_msg_overdue,
                            title
                        )

            NotificationCategory.COMPLETED ->
                context.getString(R.string.notification_title_completed) to
                        context.getString(
                            R.string.notification_msg_completed,
                            title
                        )
        }
        val formattedTime =
            if (dueDate > 0) {
                DateFormatUtil.timestampToFormatedString(
                    dueDate,
                    "MMM dd, hh:mm a"
                )
            } else {
                context.getString(R.string.just_now)
            }
        val notificationBuilder =
            NotificationCompat.Builder(
                context,
                SmartTaskApplication.NOTIFICATION_CHANNEL_ID
            )
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(notifTitle)
                .setContentText(notifMessage)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager
        val notificationId =
            (taskId * 10 + category.ordinal).toInt()
        notificationManager.notify(
            notificationId,
            notificationBuilder.build()
        )
        CoroutineScope(Dispatchers.IO).launch {
            notificationDao.insertNotification(
                NotificationEntity(
                    category = category,
                    title = notifTitle,
                    message = notifMessage,
                    detail = formattedTime,
                    timeLabel = context.getString(R.string.just_now),
                    timestamp = System.currentTimeMillis()
                )
            )
        }
    }
}