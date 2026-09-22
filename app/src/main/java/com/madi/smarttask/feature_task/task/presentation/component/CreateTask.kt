package com.madi.smarttask.feature_task.task.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.CategoryDropdown
import com.madi.smarttask.core.presentation.component.DueDateTimeSelectionRow
import com.madi.smarttask.core.presentation.component.PrioritySelectionRow
import com.madi.smarttask.core.presentation.component.TaskDateTimePickerDialogs
import com.madi.smarttask.core.presentation.component.TaskDescriptionInput
import com.madi.smarttask.core.presentation.component.TaskTitleInput
import com.madi.smarttask.feature_task.task.presentation.TaskError
import com.madi.smarttask.feature_task.task.presentation.TaskEvent
import com.madi.smarttask.feature_task.task.presentation.TaskViewModel
import com.madi.smarttask.feature_task.task.presentation.asString
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import com.madi.smarttask.core.util.permission.PermissionManager

@Composable
fun CreateTask(
    viewModel: TaskViewModel,
) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsState().value

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        viewModel.onEvent(TaskEvent.SaveTask)
    }

    TaskDateTimePickerDialogs(
        dueDate = state.dueDate,
        showDatePicker = showDatePicker,
        showTimePicker = showTimePicker,
        onDismissDatePicker = { showDatePicker = false },
        onDismissTimePicker = { showTimePicker = false },
        onDueDateSelected = { viewModel.onEvent(TaskEvent.SelectedDueDate(it)) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .imePadding(),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TaskTitleInput(
            text = state.title.text,
            error = (state.title.error as? TaskError)?.asString() ?: "",
            onValueChange = { viewModel.onEvent(TaskEvent.EnteredTitle(it)) }
        )

        TaskDescriptionInput(
            text = state.description.text,
            onValueChange = { viewModel.onEvent(TaskEvent.EnteredDescription(it)) }
        )

        PrioritySelectionRow(
            selectedPriority = state.priority,
            onPrioritySelected = { viewModel.onEvent(TaskEvent.SelectedPriority(it)) }
        )

        CategoryDropdown(
            selectedCategory = state.category,
            categories = state.categories,
            onCategorySelected = { viewModel.onEvent(TaskEvent.SelectedCategory(it)) }
        )

        DueDateTimeSelectionRow(
            dueDate = state.dueDate,
            onOpenDatePicker = { showDatePicker = true },
            onOpenTimePicker = { showTimePicker = true }
        )

        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = {
                if (!PermissionManager.hasNotificationPermission(context)) {
                    val permission = PermissionManager.getNotificationPermission()
                    if (permission != null) {
                        permissionLauncher.launch(permission)
                    } else {
                        viewModel.onEvent(TaskEvent.SaveTask)
                    }
                } else {
                    viewModel.onEvent(TaskEvent.SaveTask)
                }
            },
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
