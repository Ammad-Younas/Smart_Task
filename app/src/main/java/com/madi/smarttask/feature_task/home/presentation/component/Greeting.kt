package com.madi.smarttask.feature_task.home.presentation.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.ui.theme.ExtraSpaceSmall
import com.madi.smarttask.core.presentation.ui.theme.SpaceSmall
import com.madi.smarttask.core.util.toGreeting
import com.madi.smarttask.feature_task.home.presentation.HomeViewModel
import java.util.Date

@Composable
fun Greeting(
    viewModel: HomeViewModel
) {
    Text(
        text = stringResource(id = Date().toGreeting()),
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.Normal
    )
    Spacer(modifier = Modifier.height(ExtraSpaceSmall))
    Text(
        text = viewModel.userName.value,
        style = MaterialTheme.typography.titleLarge,
        fontWeight = FontWeight.ExtraBold
    )
    Spacer(modifier = Modifier.height(SpaceSmall))
    Text(
        text = stringResource(R.string.quote),
        style = MaterialTheme.typography.bodyLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
}
