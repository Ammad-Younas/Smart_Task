package com.madi.smarttask.feature_onboarding.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.feature_onboarding.domain.model.Boarding
import com.madi.smarttask.feature_onboarding.presentation.OnboardingEvent
import com.madi.smarttask.feature_onboarding.presentation.OnboardingViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NextOrGetStartedButton(
    pages: List<Boarding>,
    pagerState: PagerState,
    scope: CoroutineScope,
    viewModel: OnboardingViewModel
) {
    Button(
        onClick = {
            if (pagerState.currentPage < pages.size - 1) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            } else {
                viewModel.onEvent(OnboardingEvent.CompleteOnboarding)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = if (pagerState.currentPage == pages.size - 1) stringResource(R.string.get_started) else stringResource(R.string.next),
            style = MaterialTheme.typography.titleMedium
        )
    }
}