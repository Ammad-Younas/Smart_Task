package com.madi.smarttask.feature_auth.presentation.register

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskTextField
import com.madi.smarttask.core.presentation.navigation.Screen
import com.madi.smarttask.core.presentation.ui.theme.SpaceLarge
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.core.util.UiText
import com.madi.smarttask.feature_auth.presentation.util.AuthError
import com.madi.smarttask.feature_auth.presentation.util.asString
import kotlinx.coroutines.flow.collectLatest

@Composable
fun RegisterScreen(
    onShowSnackbar: (UiText) -> Unit = {},
    onNavigate: (String) -> Unit = {},
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state = viewModel.registerState.value

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    onShowSnackbar(event.uiText)
                }
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                else -> Unit
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(SpaceMedium)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.create_an_account),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.register_to_continue),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(SpaceLarge))

            SmartTaskTextField(
                text = state.usernameState.text,
                onValueChange = { viewModel.onEvent(RegisterEvent.EnteredUsername(it)) },
                hint = stringResource(R.string.username),
                error = (state.usernameState.error as? AuthError)?.asString() ?: "",
                leadingIcon = Icons.Outlined.Person,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SpaceMedium))

            SmartTaskTextField(
                text = state.emailState.text,
                onValueChange = { viewModel.onEvent(RegisterEvent.EnteredEmail(it)) },
                hint = stringResource(R.string.email),
                error = (state.emailState.error as? AuthError)?.asString() ?: "",
                leadingIcon = Icons.Outlined.Email,
                keyboardType = KeyboardType.Email,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SpaceMedium))

            SmartTaskTextField(
                text = state.passwordState.text,
                onValueChange = { viewModel.onEvent(RegisterEvent.EnteredPassword(it)) },
                hint = stringResource(R.string.password),
                error = (state.passwordState.error as? AuthError)?.asString() ?: "",
                leadingIcon = Icons.Outlined.Lock,
                keyboardType = KeyboardType.Password,
                showPasswordToggle = state.passwordState.isPasswordVisible,
                onPasswordToggleClick = { viewModel.onEvent(RegisterEvent.TogglePasswordVisibility) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(SpaceLarge))

            Button(
                onClick = { viewModel.onEvent(RegisterEvent.Register) },
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
                        text = stringResource(R.string.register),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }

            Spacer(modifier = Modifier.height(SpaceLarge))

            val loginText = buildAnnotatedString {
                append(stringResource(R.string.already_have_an_account))
                append(" ")
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append(stringResource(R.string.login))
                }
            }

            Text(
                text = loginText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.clickable {
                    onNavigate(Screen.LoginScreen.route)
                }
            )
        }
    }
}
