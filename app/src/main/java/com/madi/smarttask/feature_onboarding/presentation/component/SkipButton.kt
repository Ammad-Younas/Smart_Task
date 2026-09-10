package com.madi.smarttask.feature_onboarding.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.feature_onboarding.domain.model.Boarding
import com.madi.smarttask.feature_onboarding.presentation.OnboardingEvent
import com.madi.smarttask.feature_onboarding.presentation.OnboardingViewModel

@Composable
fun SkipButton(
    modifier: Modifier = Modifier,
    pages: List<Boarding>,
    pagerState: PagerState,
    viewModel: OnboardingViewModel
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .padding(horizontal = SpaceMedium),
        horizontalArrangement = Arrangement.End,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AnimatedVisibility(
            visible = pagerState.currentPage < pages.size - 1,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            TextButton(
                onClick = {
                    viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
                }
            ) {
                Text(
                    text = stringResource(R.string.skip),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
