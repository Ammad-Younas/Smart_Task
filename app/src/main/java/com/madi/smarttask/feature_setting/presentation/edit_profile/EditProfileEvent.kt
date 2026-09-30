package com.madi.smarttask.feature_setting.presentation.edit_profile

sealed class EditProfileEvent {
    data class EnteredUsername(val value: String) : EditProfileEvent()
    data class EnteredPassword(val value: String) : EditProfileEvent()
    object TogglePasswordVisibility : EditProfileEvent()
    object SaveProfile : EditProfileEvent()
}
