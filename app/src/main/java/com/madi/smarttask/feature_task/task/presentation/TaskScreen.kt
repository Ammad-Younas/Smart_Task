package com.madi.smarttask.feature_task.task.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.ui.theme.NavActionIconSize
import com.madi.smarttask.core.presentation.ui.theme.SpaceMedium
import com.madi.smarttask.core.presentation.util.asString
import com.madi.smarttask.core.util.UiEvent
import com.madi.smarttask.feature_task.task.presentation.component.CreateTask
import com.madi.smarttask.feature_task.task.presentation.component.Dashboard
import com.madi.smarttask.feature_task.task.presentation.component.TaskTabRow
import com.madi.smarttask.feature_task.task.presentation.util.TaskTab
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun TaskScreen(
    onNavigate: (String) -> Unit = {},
    viewModel: TaskViewModel = hiltViewModel(),
    snackbarHostState: SnackbarHostState,
    scope: CoroutineScope,
    initialTab: String? = null
) {
    val tabs = remember { listOf(TaskTab.DASHBOARD, TaskTab.CREATE_TASK) }
    val initialPage = remember(initialTab) { if (initialTab == "create_task") 1 else 0 }
    val pagerState = rememberPagerState(initialPage = initialPage) { tabs.size }
    val localCoroutineScope = rememberCoroutineScope()

    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(initialTab) {
        if (initialTab == "create_task") {
            pagerState.scrollToPage(1)
        }
    }

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collect { event ->
            when (event) {
                is UiEvent.ShowSnackBar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            message = event.uiText.asString(context),
                            duration = SnackbarDuration.Short
                        )
                    }
                }
                is TaskEvent.TaskSaved -> {
                    localCoroutineScope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                }
                else -> Unit
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        SmartTaskToolBar(
            modifier = Modifier.fillMaxWidth(),
            showBackArrow = state.isSearchOpen,
            onNavigateUp = { viewModel.onEvent(TaskEvent.ToggleSearch) },
            title = {
                if (state.isSearchOpen) {
                    OutlinedTextField(
                        value = state.searchQuery,
                        onValueChange = { viewModel.onEvent(TaskEvent.EnteredSearchQuery(it)) },
                        placeholder = { Text(text = stringResource(R.string.search)) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = {
                            if (state.searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.onEvent(TaskEvent.EnteredSearchQuery("")) }) {
                                    Icon(
                                        imageVector = Icons.Default.Clear,
                                        contentDescription = stringResource(R.string.clear_title)
                                    )
                                }
                            }
                        }
                    )
                } else {
                    Text(
                        text = stringResource(R.string.my_tasks),
                    )
                }
            },
            navActions = {
                IconButton(
                    onClick = { viewModel.onEvent(TaskEvent.ToggleSearch) }
                ){
                    Icon(
                        imageVector = if (state.isSearchOpen) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = stringResource(R.string.search),
                        modifier = Modifier.size(NavActionIconSize)
                    )
                }
            }
        )
        TaskTabRow(
            selectedTabIndex = pagerState.currentPage,
            onTabSelected = { index ->
                localCoroutineScope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(SpaceMedium),
            verticalAlignment = Alignment.Top
        ) { page ->
            when (tabs[page]) {
                TaskTab.DASHBOARD -> {
                    Dashboard(
                        stats = state.stats,
                        viewModel = viewModel,
                        onNavigate = onNavigate
                    )
                }
                TaskTab.CREATE_TASK -> {
                    CreateTask(
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}
