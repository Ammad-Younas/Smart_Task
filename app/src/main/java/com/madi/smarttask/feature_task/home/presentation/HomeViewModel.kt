package com.madi.smarttask.feature_task.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.usecase.TaskUseCases
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.feature_name.domain.usecase.NameUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nameUseCases: NameUseCases,
    private val taskUseCases: TaskUseCases
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading
    
    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _completedTaskCount = mutableIntStateOf(0)
    val completedTaskCount: State<Int> = _completedTaskCount

    private val _totalTaskCount = mutableIntStateOf(0)
    val totalTaskCount: State<Int> = _totalTaskCount

    private val _percentage = mutableIntStateOf(0)
    val percentage: State<Int> = _percentage

    private val _upcomingTasks = mutableStateOf<List<Task>>(emptyList())
    val upcomingTasks: State<List<Task>> = _upcomingTasks

    private val _days = mutableIntStateOf(3)
    val days: State<Int> = _days

    private val _hours = mutableIntStateOf(4)
    val hours: State<Int> = _hours

    private val _minutes = mutableIntStateOf(52)
    val minutes: State<Int> = _minutes

    private val _seconds = mutableIntStateOf(54)
    val seconds: State<Int> = _seconds

    private val _nextTaskHours = mutableIntStateOf(1)
    val nextTaskHours: State<Int> = _nextTaskHours

    private val _nextTaskMinutes = mutableIntStateOf(45)
    val nextTaskMinutes: State<Int> = _nextTaskMinutes

    private val _currentDay = mutableStateOf("")
    val currentDay: State<String> = _currentDay

    private var targetTimeMillis: Long = System.currentTimeMillis() + (3L * 86400 * 1000) + (4L * 3600 * 1000) + (52L * 60 * 1000)

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
                _userName.value = name
            }
            _isLoading.value = false
        }
    }

    private fun initProgressData() {
        _currentDay.value = DateFormatUtil.timestampToFormatedString(System.currentTimeMillis(), "EEEE")
        updateTimeRemaining()
    }

    private fun observeTasks() {
        viewModelScope.launch {
            taskUseCases.getTasks().collect { tasks ->
                _upcomingTasks.value = tasks.take(3)
                val total = tasks.size
                val completed = tasks.count { it.isCompleted }
                _totalTaskCount.intValue = total
                _completedTaskCount.intValue = completed
                _percentage.intValue = if (total > 0) ((completed.toFloat() / total.toFloat()) * 100f).toInt().coerceIn(0, 100) else 0
            }
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
        val diffMillis = (targetTimeMillis - System.currentTimeMillis()).coerceAtLeast(0L)
        val totalSeconds = diffMillis / 1000

        _days.intValue = (totalSeconds / 86400).toInt()
        val remainder = totalSeconds % 86400
        _hours.intValue = (remainder / 3600).toInt()
        _minutes.intValue = ((remainder % 3600) / 60).toInt()
        _seconds.intValue = (remainder % 60).toInt()

        val nextTaskTotalSec = ((totalSeconds % 86400) + 6300) % 86400
        _nextTaskHours.intValue = (nextTaskTotalSec / 3600).toInt()
        _nextTaskMinutes.intValue = ((nextTaskTotalSec % 3600) / 60).toInt()
    }
}
