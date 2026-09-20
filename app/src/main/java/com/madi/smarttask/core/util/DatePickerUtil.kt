package com.madi.smarttask.core.util

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SelectableDates
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
object FutureOrPresentSelectableDates : SelectableDates {
    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC")).apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return utcTimeMillis >= calendar.timeInMillis
    }

    override fun isSelectableYear(year: Int): Boolean {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        return year >= calendar.get(Calendar.YEAR)
    }
}
