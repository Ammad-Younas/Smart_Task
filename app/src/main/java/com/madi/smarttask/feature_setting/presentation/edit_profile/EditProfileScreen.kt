package com.madi.smarttask.feature_setting.presentation.edit_profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskTextField
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.ui.theme.SpaceLarge
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.core.presentation.ui.theme.SpaceSmall
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.presentation.util.AuthError
import com.madi.smarttask.feature_auth.presentation.util.asString

@Composable
fun EditProfileScreen(
    onNavigateUp: () -> Unit = {},
    onShowSnackbar: (UiText) -> Unit = {},
    viewModel: EditProfileViewModel = hiltViewModel()
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    onShowSnackbar(event.uiText)
                }
                is UiEvent.NavigateUp -> {
                    onNavigateUp()
                }
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = true,
            onNavigateUp = onNavigateUp,
            title = {
                Text(text = stringResource(R.string.edit_profile))
            }
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceMedium)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SmartTaskTextField(
                text = state.usernameState.text,
                onValueChange = { viewModel.onEvent(EditProfileEvent.EnteredUsername(it)) },
                hint = stringResource(R.string.username),
                error = (state.usernameState.error as? AuthError)?.asString() ?: "",
                leadingIcon = Icons.Outlined.Person,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SpaceMedium))

            SmartTaskTextField(
                text = state.emailState.text,
                onValueChange = {},
                hint = stringResource(R.string.email),
                leadingIcon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.email_cannot_be_changed_warning),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpaceSmall, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(SpaceMedium))

            SmartTaskTextField(
                text = state.passwordState.text,
                onValueChange = { viewModel.onEvent(EditProfileEvent.EnteredPassword(it)) },
                hint = stringResource(R.string.new_password),
                error = (state.passwordState.error as? AuthError)?.asString() ?: "",
                leadingIcon = Icons.Outlined.Lock,
                keyboardType = KeyboardType.Password,
                showPasswordToggle = state.passwordState.isPasswordVisible,
                onPasswordToggleClick = { viewModel.onEvent(EditProfileEvent.TogglePasswordVisibility) },
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = stringResource(R.string.leave_blank_to_keep_current),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = SpaceSmall, start = 4.dp)
            )

            Spacer(modifier = Modifier.height(SpaceLarge))

            Button(
                onClick = { viewModel.onEvent(EditProfileEvent.SaveProfile) },
                enabled = !state.isLoading,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = stringResource(R.string.save),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}
