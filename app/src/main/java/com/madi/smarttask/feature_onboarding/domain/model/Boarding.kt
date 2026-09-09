package com.madi.smarttask.feature_onboarding.domain.model

import androidx.annotation.DrawableRes

data class Boarding (
    @DrawableRes val image : Int,
    val title: String,
    val description: String
)