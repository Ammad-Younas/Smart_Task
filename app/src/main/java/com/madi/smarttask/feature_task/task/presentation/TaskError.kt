package com.madi.smarttask.feature_task.task.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.madi.smarttask.R
import com.madi.smarttask.core.util.Error


@Composable
fun TaskError.asString(): String {
    return when(this) {
        is TaskError.TitleFieldEmpty -> stringResource(R.string.error_title_empty)
    }
}

sealed class TaskError : Error() {
    class TitleFieldEmpty : TaskError()
}