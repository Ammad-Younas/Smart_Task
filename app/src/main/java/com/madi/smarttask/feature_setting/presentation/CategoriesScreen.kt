package com.madi.smarttask.feature_setting.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.presentation.component.SmartTaskToolBar

@Composable
fun CategoriesScreen(
    onNavigateUp: () -> Unit = {}
) {
    val defaultCategories = remember {
        Category.entries.map { category ->
            category.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() }
        }
    }
    
    val categories = remember { mutableStateListOf<String>().apply { addAll(defaultCategories) } }
    
    var showDialog by remember { mutableStateOf(false) }
    var newCategoryName by remember { mutableStateOf("") }

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
                onClick = { showDialog = true },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Category")
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(top = 8.dp)
        ) {
            items(categories) { categoryName ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = categoryName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.weight(1f))
                }
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }

        if (showDialog) {
            AlertDialog(
                onDismissRequest = { 
                    showDialog = false 
                    newCategoryName = ""
                },
                title = { Text(text = "Add Category") },
                text = {
                    OutlinedTextField(
                        value = newCategoryName,
                        onValueChange = { newCategoryName = it },
                        label = { Text("Category Name") },
                        singleLine = true
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            if (newCategoryName.isNotBlank()) {
                                categories.add(newCategoryName.trim())
                                showDialog = false
                                newCategoryName = ""
                            }
                        }
                    ) {
                        Text("Add")
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDialog = false
                            newCategoryName = ""
                        }
                    ) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
