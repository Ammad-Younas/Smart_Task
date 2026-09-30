package com.madi.smarttask.feature_auth.presentation.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.madi.smarttask.R
import com.madi.smarttask.core.util.Constants
import com.madi.smarttask.core.util.Error

@Composable
fun AuthError.asString(): String {
    return when(this) {
        is AuthError.FieldEmpty -> stringResource(R.string.error_field_empty)
        is AuthError.InputTooShort -> stringResource(R.string.error_input_too_short, Constants.MIN_PASSWORD_LENGTH)
        is AuthError.InvalidEmail -> stringResource(R.string.error_invalid_email)
        is AuthError.InvalidPassword -> stringResource(R.string.error_invalid_password)
    }
}

sealed class AuthError : Error() {
    object FieldEmpty : AuthError()
    object InputTooShort : AuthError()
    object InvalidEmail : AuthError()
    object InvalidPassword : AuthError()
}
