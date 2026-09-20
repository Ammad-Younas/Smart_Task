package com.madi.smarttask.feature_setting.presentation

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.madi.smarttask.R
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar
import com.madi.smarttask.core.presentation.ui.theme.ExtraSpaceSmall
import com.madi.smarttask.feature_setting.presentation.component.CategoryInputDialog

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoriesScreen(
    onNavigateUp: () -> Unit = {},
    viewModel: CategoriesViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val state by viewModel.state.collectAsState()

    val defaultEditedToastMsg = stringResource(R.string.default_category_cannot_be_edited)
    val defaultDeletedToastMsg = stringResource(R.string.default_category_cannot_be_deleted)

    Scaffold(
        topBar = {
            SmartTaskToolBar(
                modifier = Modifier.fillMaxWidth(),
                showBackArrow = true,
                onNavigateUp = onNavigateUp,
                title = {
                    Text(text = stringResource(R.string.categories))
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.onEvent(CategoriesEvent.ShowAddDialog) },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_category)
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 8.dp)
        ) {
            items(state.categories, key = { it.id }) { category ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .combinedClickable(
                            onClick = {
                                if (category.isDefault) {
                                    Toast.makeText(context, defaultEditedToastMsg, Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.onEvent(CategoriesEvent.ShowEditDialog(category))
                                }
                            },
                            onLongClick = {
                                if (category.isDefault) {
                                    Toast.makeText(context, defaultDeletedToastMsg, Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.onEvent(CategoriesEvent.ShowDeleteDialog(category))
                                }
                            }
                        )
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = category.name,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        if (category.isDefault) {
                            Spacer(Modifier.height(ExtraSpaceSmall))
                            Text(
                                text = stringResource(R.string.default_tag),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(1f))
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (state.showAddDialog) {
            CategoryInputDialog(
                title = stringResource(R.string.add_category),
                textFieldState = state.newCategoryName,
                onValueChange = { viewModel.onEvent(CategoriesEvent.NewCategoryNameChanged(it)) },
                confirmButtonText = stringResource(R.string.add),
                onConfirm = { viewModel.onEvent(CategoriesEvent.AddCategory) },
                onDismiss = { viewModel.onEvent(CategoriesEvent.DismissAddDialog) }
            )
        }

        state.categoryToEdit?.let { _ ->
            CategoryInputDialog(
                title = stringResource(R.string.edit_category),
                textFieldState = state.editCategoryName,
                onValueChange = { viewModel.onEvent(CategoriesEvent.EditCategoryNameChanged(it)) },
                confirmButtonText = stringResource(R.string.save),
                onConfirm = { viewModel.onEvent(CategoriesEvent.UpdateCategory) },
                onDismiss = { viewModel.onEvent(CategoriesEvent.DismissEditDialog) }
            )
        }

        state.categoryToDelete?.let { category ->
            AlertDialog(
                onDismissRequest = { viewModel.onEvent(CategoriesEvent.DismissDeleteDialog) },
                title = { Text(text = stringResource(R.string.delete_category_title)) },
                text = {
                    Text(text = stringResource(R.string.delete_category_confirmation, category.name))
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.onEvent(CategoriesEvent.DeleteCategory) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text(text = stringResource(R.string.delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.onEvent(CategoriesEvent.DismissDeleteDialog) }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}
