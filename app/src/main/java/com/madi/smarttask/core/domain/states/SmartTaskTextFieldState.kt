package com.madi.smarttask.core.domain.states

data class SmartTaskTextFieldState(
    val text: String = "",
    val error: Error? = null
)