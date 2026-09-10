package com.madi.smarttask.feature_onboarding.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.core.presentation.ui.theme.SpaceLarge
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.feature_onboarding.presentation.component.BoardingItem
import com.madi.smarttask.feature_onboarding.presentation.component.NextOrGetStartedButton
import com.madi.smarttask.feature_onboarding.presentation.component.PageIndicator
import com.madi.smarttask.feature_onboarding.presentation.component.SkipButton
import kotlinx.coroutines.flow.collectLatest

@Composable
fun OnboardingScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()

    val pages = state.pages
    if (pages.isEmpty()) return

    val pagerState = rememberPagerState(pageCount = { pages.size })

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is UiEvent.Navigate -> {
                    onNavigate(event.route)
                }
                else -> Unit
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(vertical = SpaceLarge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        SkipButton(pages = pages, pagerState = pagerState, viewModel = viewModel)
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            BoardingItem(boarding = pages[page])
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = SpaceLarge),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PageIndicator(pages = pages, pagerState = pagerState)
            NextOrGetStartedButton(
                pages = pages,
                pagerState = pagerState,
                scope = scope,
                viewModel = viewModel
            )
        }
    }
}
