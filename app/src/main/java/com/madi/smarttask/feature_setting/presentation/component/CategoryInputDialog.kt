package com.madi.smarttask.feature_setting.presentation.component

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState
import com.madi.smarttask.core.presentation.component.SmartTaskTextField

@Composable
fun CategoryInputDialog(
    title: String,
    textFieldState: SmartTaskTextFieldState,
    onValueChange: (String) -> Unit,
    confirmButtonText: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = title) },
        text = {
            SmartTaskTextField(
                text = textFieldState.text,
                onValueChange = onValueChange,
                hint = stringResource(R.string.category_name),
                singleLine = true
            )
        },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text(text = confirmButtonText)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(R.string.cancel))
            }
        }
    )
}
