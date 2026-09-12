package com.madi.smarttask.core.util

import androidx.annotation.StringRes
import com.madi.smarttask.R

sealed class UiText {
    data class StringResource(@StringRes val id: Int, val args: List<Any> = emptyList()): UiText()

    companion object {
        fun unknownError(): UiText {
            return StringResource(R.string.unknown_error)
        }
    }
}