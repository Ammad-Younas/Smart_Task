package com.madi.smarttask.feature_notification.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.data.local.SmartTaskDatabase
import com.madi.smarttask.feature_notification.domain.model.Notification
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val db: SmartTaskDatabase
) : ViewModel() {

    val notifications: StateFlow<List<Notification>> = db.notificationDao.getNotifications()
        .map { entities -> entities.map { it.toNotification() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun deleteNotification(id: String) {
        val notificationId = id.toLongOrNull() ?: return
        viewModelScope.launch {
            db.notificationDao.deleteNotification(notificationId)
        }
    }
}
