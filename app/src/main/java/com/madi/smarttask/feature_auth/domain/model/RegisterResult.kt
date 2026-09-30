package com.madi.smarttask.feature_auth.domain.model

import com.madi.smarttask.core.util.SimpleResource
import com.madi.smarttask.feature_auth.presentation.util.AuthError
import com.madi.smarttask.feature_name.presentation.NameError

data class RegisterResult(
    val emailError: AuthError? = null,
    val usernameError: NameError? = null,
    val passwordError: AuthError? = null,
    val result: SimpleResource? = null
)
