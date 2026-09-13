package com.madi.smarttask.feature_notification.domain.model

import com.madi.smarttask.feature_notification.domain.util.NotificationCategory

data class Notification(
    val id: String,
    val category: NotificationCategory,
    val title: String,
    val message: String,
    val detail: String? = null,
    val timeLabel: String
)
