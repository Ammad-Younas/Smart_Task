package com.madi.smarttask.feature_setting.domain.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.madi.smarttask.R

enum class ThemeMode {
    SYSTEM_DEFAULT,
    LIGHT,
    DARK;

    @Composable
    fun toDisplayString(): String {
        return when (this) {
            SYSTEM_DEFAULT -> stringResource(R.string.system_default)
            LIGHT -> stringResource(R.string.light)
            DARK -> stringResource(R.string.dark)
        }
    }
}