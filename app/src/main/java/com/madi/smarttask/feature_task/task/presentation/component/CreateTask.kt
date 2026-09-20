package com.madi.smarttask.feature_task.task.presentation.component

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.CategoryDropdown
import com.madi.smarttask.core.presentation.component.PrioritySelectionRow
import com.madi.smarttask.core.presentation.component.SmartTaskTextField
import com.madi.smarttask.core.presentation.util.asString
import com.madi.smarttask.core.util.DateFormatUtil
import com.madi.smarttask.core.util.FutureOrPresentSelectableDates
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.feature_task.task.presentation.TaskError
import com.madi.smarttask.feature_task.task.presentation.TaskEvent
import com.madi.smarttask.feature_task.task.presentation.TaskViewModel
import com.madi.smarttask.feature_task.task.presentation.asString
import kotlinx.coroutines.flow.collectLatest
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTask(
    viewModel: TaskViewModel,
    snackbarHostState: SnackbarHostState,
) {

    val context: Context = LocalContext.current

    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsState().value

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val initialDateMillis = state.dueDate.takeIf { it > 0 } ?: System.currentTimeMillis()
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
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    datePickerState.selectedDateMillis?.let {
                        val newCal = Calendar.getInstance().apply { timeInMillis = it }
                        val currentCal = Calendar.getInstance().apply { timeInMillis = state.dueDate.takeIf { d -> d > 0 } ?: System.currentTimeMillis() }
                        newCal.set(Calendar.HOUR_OF_DAY, currentCal.get(Calendar.HOUR_OF_DAY))
                        newCal.set(Calendar.MINUTE, currentCal.get(Calendar.MINUTE))
                        viewModel.onEvent(TaskEvent.SelectedDueDate(newCal.timeInMillis))
                    }
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text(stringResource(R.string.skip))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showTimePicker = false
                    val newCal = Calendar.getInstance().apply { timeInMillis = state.dueDate.takeIf { it > 0 } ?: System.currentTimeMillis() }
                    newCal.set(Calendar.HOUR_OF_DAY, timePickerState.hour)
                    newCal.set(Calendar.MINUTE, timePickerState.minute)
                    viewModel.onEvent(TaskEvent.SelectedDueDate(newCal.timeInMillis))
                }) {
                    Text(stringResource(R.string.save))
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text(stringResource(R.string.skip))
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }


    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when(event) {
                is UiEvent.ShowSnackBar -> {
                    snackbarHostState.showSnackbar(
                        message = event.uiText.asString(context),
                        duration = SnackbarDuration.Short
                    )
                }
                else -> Unit
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Column(
        ) {
            Text(
                text = stringResource(R.string.task_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SmartTaskTextField(
                text = state.title.text,
                error = (state.title.error as? TaskError)?.asString() ?: "",
                onValueChange = { viewModel.onEvent(TaskEvent.EnteredTitle(it)) },
                hint = stringResource(R.string.task_title),
                trailingIcon = {
                    if (state.title.text.isNotEmpty()) {
                        IconButton(onClick = { viewModel.onEvent(TaskEvent.EnteredTitle("")) }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = stringResource(R.string.clear_title)
                            )
                        }
                    }
                }
            )
        }

        Column {
            Text(
                text = stringResource(R.string.description),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            SmartTaskTextField(
                text = state.description.text,
                onValueChange = { viewModel.onEvent(TaskEvent.EnteredDescription(it)) },
                hint = stringResource(R.string.description),
                singleLine = false,
                minLines = 4,
                maxLines = 6
            )
        }

        PrioritySelectionRow(
            selectedPriority = state.priority,
            onPrioritySelected = { viewModel.onEvent(TaskEvent.SelectedPriority(it)) }
        )

        CategoryDropdown(
            selectedCategory = state.category,
            onCategorySelected = { viewModel.onEvent(TaskEvent.SelectedCategory(it)) }
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = stringResource(R.string.due_date),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box {
                    SmartTaskTextField(
                        text = DateFormatUtil.timestampToFormatedString(state.dueDate.takeIf { it > 0 } ?: System.currentTimeMillis(), "MMM dd, yyyy"),
                        onValueChange = {},
                        leadingIcon = Icons.Outlined.CalendarToday
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showDatePicker = true }
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
                        text = DateFormatUtil.timestampToFormatedString(state.dueDate.takeIf { it > 0 } ?: System.currentTimeMillis(), "hh:mm a"),
                        onValueChange = {},
                        leadingIcon = Icons.Outlined.Schedule
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { showTimePicker = true }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { viewModel.onEvent(TaskEvent.SaveTask) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = stringResource(R.string.save),
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
