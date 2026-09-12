package com.madi.smarttask.core.presentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.madi.smarttask.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartTaskToolBar(
    modifier: Modifier = Modifier,
    showToolBar: Boolean = false,
    onNavigateUp: () -> Unit = {},
    showBackArrow: Boolean = false,
    navActions: @Composable RowScope.() -> Unit = {},
    title: @Composable () -> Unit = {}
) {
    if (showToolBar){
        TopAppBar(
            modifier = modifier,
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                navigationIconContentColor = MaterialTheme.colorScheme.onPrimary,
                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                actionIconContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            title = title,
            navigationIcon = {
                if (showBackArrow) {
                    IconButton(
                        onClick = { onNavigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.Back)
                        )
                    }
                }
            },
            actions = navActions,
        )
    }
}