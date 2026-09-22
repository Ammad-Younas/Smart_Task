package com.madi.smarttask.feature_notification.presentation.util

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.madi.smarttask.R
import com.madi.smarttask.core.data.local.dao.NotificationDao
import com.madi.smarttask.core.data.local.dao.TaskDao
import com.madi.smarttask.core.data.local.entity.NotificationEntity
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.feature_notification.domain.util.NotificationCategory
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class OverdueTaskWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val taskDao: TaskDao,
    private val notificationDao: NotificationDao
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            val tasks = taskDao.getAllTasksList()
            val currentTime = System.currentTimeMillis()

            val overdueTasks = tasks.filter {
                !it.isComplete && it.dueDate in 1..<currentTime
            }

            val overdueTitle = applicationContext.getString(R.string.notification_title_overdue)

            overdueTasks.forEach { task ->
                val formattedTime = DateFormatUtil.timestampToFormatedString(task.dueDate, "MMM dd, hh:mm a")
                val overdueMsg = applicationContext.getString(R.string.notification_msg_overdue, task.title)
                val wasDueDetail = applicationContext.getString(R.string.was_due, formattedTime)
                val overdueLabel = applicationContext.getString(R.string.overdue)

                notificationDao.insertNotification(
                    NotificationEntity(
                        category = NotificationCategory.OVERDUE,
                        title = overdueTitle,
                        message = overdueMsg,
                        detail = wasDueDetail,
                        timeLabel = overdueLabel,
                        timestamp = System.currentTimeMillis()
                    )
                )
            }
            return Result.success()
        } catch (_: Exception) {
            return Result.failure()
        }
    }
}
