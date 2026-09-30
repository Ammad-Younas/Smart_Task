package com.madi.smarttask.feature_auth.domain.model

import com.madi.smarttask.core.util.SimpleResource
import com.madi.smarttask.feature_auth.presentation.util.AuthError

data class LoginResult(
    val emailError: AuthError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null
)