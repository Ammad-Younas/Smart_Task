package com.madi.smarttask.feature_task.home.presentation

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

import com.madi.smarttask.feature_name.domain.usecase.NameUseCases

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val nameUseCases: NameUseCases
) : ViewModel() {

    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading
    
    private val _userName = mutableStateOf("")
    val userName: State<String> = _userName

    init {
        checkUserName()
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
}
