package com.madi.smarttask.feature_name.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.madi.smarttask.R
import com.madi.smarttask.core.util.Constants
import com.madi.smarttask.core.util.Error

@Composable
fun NameError.asString(): String {
    return when(this) {
        is NameError.FieldEmpty -> stringResource(R.string.error_name_empty)
        is NameError.InputTooShort -> stringResource(R.string.error_name_too_short, Constants.MIN_NAME_LENGTH)
        is NameError.InputTooLong -> stringResource(R.string.error_name_too_long, Constants.MAX_NAME_LENGTH)
    }
}


sealed class NameError : Error() {
    class FieldEmpty : NameError()
    class InputTooShort : NameError()
    class InputTooLong : NameError()
}