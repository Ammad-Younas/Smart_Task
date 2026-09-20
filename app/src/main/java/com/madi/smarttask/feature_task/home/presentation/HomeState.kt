package com.madi.smarttask.feature_task.home.presentation

import com.madi.smarttask.core.domain.model.Task

data class HomeState(
    val userName: String = "",
    val percentage: Int = 0,
    val upcomingTasks: List<Task> = emptyList(),
    val days: Int = 0,
    val hours: Int = 0,
    val minutes: Int = 0,
    val seconds: Int = 0,
    val nextTaskHours: Int = 0,
    val nextTaskMinutes: Int = 0,
    val currentDay: String = "",
)
