package com.madi.smarttask.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.madi.smarttask.feature_notification.domain.model.Notification
import com.madi.smarttask.feature_notification.domain.util.NotificationCategory

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val category: NotificationCategory,
    val title: String,
    val message: String,
    val detail: String? = null,
    val timeLabel: String,
    val timestamp: Long = System.currentTimeMillis()
) {
    fun toNotification(): Notification {
        return Notification(
            id = id.toString(),
            category = category,
            title = title,
            message = message,
            detail = detail,
            timeLabel = timeLabel
        )
    }
}
