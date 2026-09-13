package com.madi.smarttask.feature_task.task.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.TaskStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskFilter(
    selectedStatus: TaskStatus?,
    onStatusSelected: (TaskStatus?) -> Unit,
    selectedPriority: Priority?,
    onPrioritySelected: (Priority?) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showFilterMenu by remember { mutableStateOf(value = false) }

    Box(modifier = modifier) {
        FilterChip(
            selected = showFilterMenu,
            onClick = { showFilterMenu = true },
            label = { Icon(Icons.Default.FilterList, contentDescription = stringResource(R.string.filter)) },
            shape = CircleShape,
            modifier = Modifier.padding(start = 8.dp),
        )
        
        DropdownMenu(
            expanded = showFilterMenu,
            onDismissRequest = { showFilterMenu = false },
            shape = RoundedCornerShape(20.dp),
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp,
        ) {
            Text(
                text = stringResource(R.string.status),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            
            TaskStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(LocalLocale.current.platformLocale) else it.toString() }) },
                    onClick = { 
                        onStatusSelected(if (selectedStatus == status) null else status) 
                    },
                    trailingIcon = {
                        Checkbox(
                            checked = selectedStatus == status,
                            onCheckedChange = null,
                        )
                    },
                )
            }
            
            HorizontalDivider()
            
            Text(
                text = stringResource(R.string.priority),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
            )
            
            Priority.entries.forEach { priority ->
                DropdownMenuItem(
                    text = { Text(priority.name.lowercase().replaceFirstChar { if (it.isLowerCase()) it.titlecase(LocalLocale.current.platformLocale) else it.toString() }) },
                    onClick = { 
                        onPrioritySelected(if (selectedPriority == priority) null else priority) 
                    },
                    trailingIcon = {
                        Checkbox(
                            checked = selectedPriority == priority,
                            onCheckedChange = null,
                        )
                    },
                )
            }
        }
    }
}
