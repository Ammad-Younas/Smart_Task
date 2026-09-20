package com.madi.smarttask.core.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.core.util.FutureOrPresentSelectableDates
import java.util.Calendar

@Composable
fun TaskTitleInput(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    error: String
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.task_title),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SmartTaskTextField(
            text = text,
            error = error,
            onValueChange = onValueChange,
            hint = stringResource(R.string.task_title),
        )
    }
}

@Composable
fun TaskDescriptionInput(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.description),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        SmartTaskTextField(
            text = text,
            onValueChange = onValueChange,
            hint = stringResource(R.string.description),
            singleLine = false,
            minLines = 4,
            maxLines = 6
        )
    }
}

@Composable
fun DueDateTimeSelectionRow(
    dueDate: Long,
    onOpenDatePicker: () -> Unit,
    onOpenTimePicker: () -> Unit,
    modifier: Modifier = Modifier
) {
    val displayDate = DateFormatUtil.timestampToFormatedString(dueDate.takeIf { it > 0 } ?: System.currentTimeMillis(), "MMM dd, yyyy")
    val displayTime = DateFormatUtil.timestampToFormatedString(dueDate.takeIf { it > 0 } ?: System.currentTimeMillis(), "hh:mm a")

    Row(modifier = modifier.fillMaxWidth()) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.due_date),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box {
                SmartTaskTextField(
                    text = displayDate,
                    onValueChange = {},
                    leadingIcon = Icons.Outlined.CalendarToday
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { onOpenDatePicker() }
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = stringResource(R.string.due_time),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Box {
                SmartTaskTextField(
                    text = displayTime,
                    onValueChange = {},
                    leadingIcon = Icons.Outlined.Schedule
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { onOpenTimePicker() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDateTimePickerDialogs(
    dueDate: Long,
    showDatePicker: Boolean,
    showTimePicker: Boolean,
    onDismissDatePicker: () -> Unit,
    onDismissTimePicker: () -> Unit,
    onDueDateSelected: (Long) -> Unit
) {
    val initialDateMillis = dueDate.takeIf { it > 0 } ?: System.currentTimeMillis()
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initialDateMillis,
        selectableDates = FutureOrPresentSelectableDates
    )

    val cal = Calendar.getInstance().apply { timeInMillis = initialDateMillis }
    val timePickerState = rememberTimePickerState(
        initialHour = cal.get(Calendar.HOUR_OF_DAY),
        initialMinute = cal.get(Calendar.MINUTE),
        is24Hour = false
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = onDismissDatePicker,
            confirmButton = {
                TextButton(onClick = {
                    onDismissDatePicker()
                    datePickerState.selectedDateMillis?.let { selectedUtc ->
                        val newCal = Calendar.getInstance().apply { timeInMillis = selectedUtc }
                        val currentCal = Calendar.getInstance().apply { timeInMillis = dueDate.takeIf { d -> d > 0 } ?: System.currentTimeMillis() }
                        newCal.set(Calendar.HOUR_OF_DAY, currentCal.get(Calendar.HOUR_OF_DAY))
                        newCal.set(Calendar.MINUTE, currentCal.get(Calendar.MINUTE))
                        onDueDateSelected(newCal.timeInMillis)
                    }
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissDatePicker) {
                    Text(stringResource(R.string.skip))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = onDismissTimePicker,
            confirmButton = {
                TextButton(onClick = {
                    onDismissTimePicker()
                    val newCal = Calendar.getInstance().apply { timeInMillis = dueDate.takeIf { it > 0 } ?: System.currentTimeMillis() }
                    newCal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    newCal.set(Calendar.MINUTE, timePickerState.minute)
                    onDueDateSelected(newCal.timeInMillis)
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = onDismissTimePicker) {
                    Text(stringResource(R.string.skip))
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}
