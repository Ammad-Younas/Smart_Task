package com.madi.smarttask.feature_name.presentation

sealed class NameEvent {
    data class OnNameChange(val name: String) : NameEvent()
    object SaveName : NameEvent()
}
