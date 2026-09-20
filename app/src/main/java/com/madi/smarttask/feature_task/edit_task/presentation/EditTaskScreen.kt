package com.madi.smarttask.feature_task.edit_task.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.CategoryDropdown
import com.madi.smarttask.core.presentation.component.DueDateTimeSelectionRow
import com.madi.smarttask.core.presentation.component.PrioritySelectionRow
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.component.TaskDateTimePickerDialogs
import com.madi.smarttask.core.presentation.component.TaskDescriptionInput
import com.madi.smarttask.core.presentation.component.TaskTitleInput
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_task.edit_task.presentation.component.ActionButtons
import com.madi.smarttask.feature_task.edit_task.presentation.component.StatusToggle
import com.madi.smarttask.feature_task.task.presentation.TaskError
import com.madi.smarttask.feature_task.task.presentation.TaskEvent
import com.madi.smarttask.feature_task.task.presentation.asString

@Composable
fun EditTaskScreen(
    onNavigateUp: () -> Unit = {},
    viewModel: EditTaskViewModel = hiltViewModel(),
    onShowSnackbar: (UiText) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    val state = viewModel.state.collectAsState().value

    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    onShowSnackbar(event.uiText)
                }
                TaskEvent.TaskUpdated, TaskEvent.TaskDeleted, UiEvent.NavigateUp -> {
                    onNavigateUp()
                }
                else -> Unit
            }
        }
    }

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDeleteConfirmationDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmationDialog = false },
            title = {
                Text(
                    text = stringResource(R.string.delete_task_confirmation_title),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.delete_task_confirmation_message),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmationDialog = false
                        viewModel.onEvent(EditTaskEvent.DeleteTask)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmationDialog = false }) {
                    Text(stringResource(R.string.cancel))
                }
            }
        )
    }

    TaskDateTimePickerDialogs(
        dueDate = state.dueDate,
        showDatePicker = showDatePicker,
        showTimePicker = showTimePicker,
        onDismissDatePicker = { showDatePicker = false },
        onDismissTimePicker = { showTimePicker = false },
        onDueDateSelected = { viewModel.onEvent(EditTaskEvent.SelectedDueDate(it)) }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = true,
            onNavigateUp = onNavigateUp,
            title = {
                Text(
                    text = stringResource(R.string.edit_task),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(24.dp)
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            TaskTitleInput(
                text = state.title.text,
                error = (state.title.error as? TaskError)?.asString() ?: "",
                onValueChange = { viewModel.onEvent(EditTaskEvent.EnteredTitle(it)) }
            )

            TaskDescriptionInput(
                text = state.description.text,
                onValueChange = { viewModel.onEvent(EditTaskEvent.EnteredDescription(it)) }
            )

            PrioritySelectionRow(
                selectedPriority = state.priority,
                onPrioritySelected = { viewModel.onEvent(EditTaskEvent.SelectedPriority(it)) }
            )

            CategoryDropdown(
                selectedCategory = state.category,
                categories = state.categories,
                onCategorySelected = { viewModel.onEvent(EditTaskEvent.SelectedCategory(it)) }
            )

            DueDateTimeSelectionRow(
                dueDate = state.dueDate,
                onOpenDatePicker = { showDatePicker = true },
                onOpenTimePicker = { showTimePicker = true }
            )

            StatusToggle(
                isCompleted = state.isCompleted,
                onCompletedChange = { viewModel.onEvent(EditTaskEvent.ToggledCompletion(it)) }
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            ActionButtons(
                onUpdateClick = { viewModel.onEvent(EditTaskEvent.UpdateTask) },
                onDeleteClick = { showDeleteConfirmationDialog = true }
            )
        }
    }
}
