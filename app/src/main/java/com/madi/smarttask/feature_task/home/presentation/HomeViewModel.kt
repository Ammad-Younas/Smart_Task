package com.madi.smarttask.feature_task.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.feature_name.domain.usecase.NameUseCases

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nameUseCases: NameUseCases
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading
    
    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    private val _completedTaskCount = mutableIntStateOf(14)
    val completedTaskCount: State<Int> = _completedTaskCount

    private val _totalTaskCount = mutableIntStateOf(20)
    val totalTaskCount: State<Int> = _totalTaskCount

    private val _percentage = mutableIntStateOf(70)
    val percentage: State<Int> = _percentage

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

    init {
        checkUserName()
        initProgressData()
        startClockTicker()
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
        val completed = _completedTaskCount.intValue
        val total = _totalTaskCount.intValue
        _percentage.intValue = ((completed.toFloat() / total.toFloat()) * 100f).toInt().coerceIn(0, 100)
        _currentDay.value = DateFormatUtil.timestampToFormatedString(System.currentTimeMillis(), "EEEE")
        updateTimeRemaining()
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
        val calendar = Calendar.getInstance()
        val nowHour = calendar.get(Calendar.HOUR_OF_DAY)
        val nowMinute = calendar.get(Calendar.MINUTE)
        val nowSecond = calendar.get(Calendar.SECOND)

        val totalSecondsNow = nowHour * 3600 + nowMinute * 60 + nowSecond
        val totalSecondsDay = 24 * 3600
        val secondsRemaining = (totalSecondsDay - totalSecondsNow).coerceAtLeast(0)

        _hours.intValue = secondsRemaining / 3600
        _minutes.intValue = (secondsRemaining % 3600) / 60
        _seconds.intValue = secondsRemaining % 60

        val nextTaskTotalSec = (secondsRemaining + 6300) % 86400
        _nextTaskHours.intValue = nextTaskTotalSec / 3600
        _nextTaskMinutes.intValue = (nextTaskTotalSec % 3600) / 60
    }
}
