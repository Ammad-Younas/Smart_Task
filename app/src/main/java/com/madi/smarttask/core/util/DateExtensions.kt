package com.madi.smarttask.core.util

import androidx.annotation.StringRes
import com.madi.smarttask.R
import java.util.Calendar
import java.util.Date

@StringRes
fun Date.toGreeting(): Int {
    val hour = Calendar.getInstance().apply {
        time = this@toGreeting
    }.get(Calendar.HOUR_OF_DAY)

    return when (hour) {
        in 5..11 -> R.string.good_morning
        in 12..13 -> R.string.good_noon
        in 14..16 -> R.string.good_afternoon
        in 17..20 -> R.string.good_evening
        else -> R.string.good_night
    }
}
