package com.madi.smarttask.core.domain.util

import com.madi.smarttask.core.util.Constants
import com.madi.smarttask.feature_name.presentation.NameError

object ValidationUtil {
    fun validateUsername(username: String) : NameError? {
        val trimmedUsername = username.trim()
        if (trimmedUsername.isBlank()){
            return NameError.FieldEmpty()
        }
        if (trimmedUsername.length < Constants.MIN_NAME_LENGTH){
            return NameError.InputTooShort()
        }
        if (trimmedUsername.length > Constants.MAX_NAME_LENGTH){
            return NameError.InputTooLong()
        }
        return null
    }
}