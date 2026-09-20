package com.madi.smarttask.feature_task.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.usecase.TaskUseCases
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.feature_name.domain.usecase.NameUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nameUseCases: NameUseCases,
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    private var targetTimeMillis: Long = 0L

    init {
        checkUserName()
        initProgressData()
        startClockTicker()
        observeTasks()
    }

    private fun checkUserName() {
        viewModelScope.launch {
            val name = nameUseCases.getUserName().first()
            if (!name.isNullOrBlank()) {
                _state.update { it.copy(userName = name) }
            }
        }
    }

    private fun initProgressData() {
        val currentDay = DateFormatUtil.timestampToFormatedString(System.currentTimeMillis(), "EEEE")
        _state.update { it.copy(currentDay = currentDay) }
        updateTimeRemaining()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            taskUseCases.getTasks().collect { tasks ->
                val total = tasks.size
                val completed = tasks.count { it.isCompleted }
                val percentage = if (total > 0) ((completed.toFloat() / total.toFloat()) * 100f).toInt().coerceIn(0, 100) else 0

                val sortedTasks = tasks.sortedWith(
                    compareByDescending<Task> { it.priority.ordinal }
                        .thenByDescending { it.dueDate }
                )
                val urgentTasks = sortedTasks.filter { it.priority == Priority.URGENT }
                val upcomingTasks = if (urgentTasks.size >= 3) {
                    urgentTasks.sortedByDescending { it.dueDate }
                } else {
                    sortedTasks.take(3)
                }

                val nearestUpcomingTask = tasks
                    .filter { !it.isCompleted && it.dueDate > System.currentTimeMillis() }
                    .minByOrNull { it.dueDate }

                targetTimeMillis = nearestUpcomingTask?.dueDate ?: 0L

                _state.update {
                    it.copy(
                        percentage = percentage,
                        upcomingTasks = upcomingTasks
                    )
                }
                updateTimeRemaining()
            }
        }
    }

    fun toggleTaskCompletion(task: Task, isCompleted: Boolean) {
        viewModelScope.launch {
            taskUseCases.updateTask(task.copy(isCompleted = isCompleted))
        }
    }

    private fun startClockTicker() {
        viewModelScope.launch {
            while (true) {
                delay(1.seconds)
                updateTimeRemaining()
            }
        }
    }

    private fun updateTimeRemaining() {
        val currentTime = System.currentTimeMillis()
        if (targetTimeMillis <= currentTime) {
            _state.update {
                it.copy(
                    days = 0,
                    hours = 0,
                    minutes = 0,
                    seconds = 0,
                    nextTaskHours = 0,
                    nextTaskMinutes = 0
                )
            }
            return
        }

        val diffMillis = targetTimeMillis - currentTime
        val totalSeconds = diffMillis / 1000

        val days = (totalSeconds / 86400).toInt()
        val remainder = totalSeconds % 86400
        val hours = (remainder / 3600).toInt()
        val minutes = ((remainder % 3600) / 60).toInt()
        val seconds = (remainder % 60).toInt()

        val nextTaskHours = (totalSeconds / 3600).toInt()
        val nextTaskMinutes = ((totalSeconds % 3600) / 60).toInt()

        _state.update {
            it.copy(
                days = days,
                hours = hours,
                minutes = minutes,
                seconds = seconds,
                nextTaskHours = nextTaskHours,
                nextTaskMinutes = nextTaskMinutes
            )
        }
    }
}
