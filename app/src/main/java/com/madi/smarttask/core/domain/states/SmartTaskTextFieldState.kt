package com.madi.smarttask.core.domain.states

import com.madi.smarttask.core.util.Error

data class SmartTaskTextFieldState(
    val text: String = "",
    val error: Error? = null
)
