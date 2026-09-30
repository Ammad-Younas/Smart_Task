package com.madi.smarttask.core.domain.util

import android.util.Patterns
import com.madi.smarttask.core.util.Constants
import com.madi.smarttask.feature_auth.presentation.util.AuthError
import com.madi.smarttask.feature_name.presentation.NameError
import com.madi.smarttask.feature_task.task.presentation.TaskError

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

    fun validateEmail(email: String) : AuthError? {
        val trimmedEmail = email.trim()
        if (trimmedEmail.isBlank()){
            return AuthError.FieldEmpty
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return AuthError.InvalidEmail
        }
        return null
    }


    fun validatePassword(password: String) : AuthError? {
        if (password.isBlank()){
            return AuthError.FieldEmpty
        }
        if (password.length < Constants.MIN_PASSWORD_LENGTH){
            return AuthError.InputTooShort
        }
        val capitalLettersInPassword = password.any { it.isUpperCase() }
        val numbersInPassword = password.any { it.isDigit() }
        if (!capitalLettersInPassword || !numbersInPassword){
            return AuthError.InvalidPassword
        }
        return null
    }


    fun validateTitle(title: String) : TaskError? {
        val trimmedTitle = title.trim()
        if (trimmedTitle.isBlank()){
            return TaskError.TitleFieldEmpty()
        }
        return null
    }
}