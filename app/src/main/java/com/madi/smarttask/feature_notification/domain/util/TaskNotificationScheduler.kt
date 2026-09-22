package com.madi.smarttask.feature_notification.domain.util

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.feature_notification.presentation.util.TaskNotificationReceiver
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TaskNotificationScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager

    fun scheduleTaskNotifications(task: Task) {
        try {
            if (task.isCompleted || task.dueDate <= 0L) return
            val currentTime = System.currentTimeMillis()

            val reminderTime = task.dueDate - (15 * 60 * 1000L)
            if (reminderTime > currentTime) {
                scheduleAlarm(
                    task = task,
                    triggerAtMillis = reminderTime,
                    category = NotificationCategory.REMINDER
                )
            }

            if (task.dueDate > currentTime) {
                scheduleAlarm(
                    task = task,
                    triggerAtMillis = task.dueDate,
                    category = NotificationCategory.DEADLINE
                )
            }

            val overdueTime = task.dueDate + (15 * 60 * 1000L)
            if (overdueTime > currentTime) {
                scheduleAlarm(
                    task = task,
                    triggerAtMillis = overdueTime,
                    category = NotificationCategory.OVERDUE
                )
            }
        } catch (_: Exception) { }
    }

    fun postImmediateNotification(task: Task, category: NotificationCategory) {
        try {
            val intent = Intent(context, TaskNotificationReceiver::class.java).apply {
                action = ACTION_TASK_NOTIFICATION
                putExtra(EXTRA_TASK_ID, task.id)
                putExtra(EXTRA_TASK_TITLE, task.title)
                putExtra(EXTRA_CATEGORY, category.name)
                putExtra(EXTRA_DUE_DATE, task.dueDate)
            }
            context.sendBroadcast(intent)
        } catch (_: Exception) { }
    }

    fun cancelTaskNotifications(taskId: Long) {
        try {
            cancelAlarm(taskId, NotificationCategory.REMINDER)
            cancelAlarm(taskId, NotificationCategory.DEADLINE)
            cancelAlarm(taskId, NotificationCategory.OVERDUE)
        } catch (_: Exception) { }
    }

    private fun scheduleAlarm(
        task: Task,
        triggerAtMillis: Long,
        category: NotificationCategory
    ) {
        val intent = Intent(
            context,
            TaskNotificationReceiver::class.java
        ).apply {
            action = ACTION_TASK_NOTIFICATION
            putExtra(EXTRA_TASK_ID, task.id)
            putExtra(EXTRA_TASK_TITLE, task.title)
            putExtra(EXTRA_CATEGORY, category.name)
            putExtra(EXTRA_DUE_DATE, task.dueDate)
        }
        val requestCode = getRequestCode(task.id, category)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        alarmManager?.let { alarm ->
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S && !alarm.canScheduleExactAlarms()) {
                    alarm.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                } else {
                    alarm.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                }
            } catch (_: Exception) {
                try {
                    alarm.set(
                        AlarmManager.RTC_WAKEUP,
                        triggerAtMillis,
                        pendingIntent
                    )
                } catch (_: Exception) { }
            }
        }
    }

    private fun cancelAlarm(
        taskId: Long,
        category: NotificationCategory
    ) {
        val intent = Intent(
            context,
            TaskNotificationReceiver::class.java
        ).apply {
            action = ACTION_TASK_NOTIFICATION
        }
        val requestCode = getRequestCode(taskId, category)
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
        )
        pendingIntent?.let {
            alarmManager?.cancel(it)
            it.cancel()
        }
    }

    private fun getRequestCode(
        taskId: Long,
        category: NotificationCategory
    ): Int {
        return (taskId * 10 + category.ordinal).toInt()
    }

    companion object {
        const val ACTION_TASK_NOTIFICATION = "com.madi.smarttask.action.TASK_NOTIFICATION"
        const val EXTRA_TASK_ID = "com.madi.smarttask.extra.TASK_ID"
        const val EXTRA_TASK_TITLE = "com.madi.smarttask.extra.TASK_TITLE"
        const val EXTRA_CATEGORY = "com.madi.smarttask.extra.CATEGORY"
        const val EXTRA_DUE_DATE = "com.madi.smarttask.extra.DUE_DATE"
    }
}
