package com.madi.smarttask.core.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.madi.smarttask.R
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.presentation.ui.theme.getPriorityColor

@Composable
fun PrioritySelectionRow(
    selectedPriority: Priority,
    onPrioritySelected: (Priority) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.priority),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Priority.entries.forEach { priority ->
                val color = getPriorityColor(priority)
                val isSelected = priority == selectedPriority
                
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = color.copy(alpha = 0.15f),
                    border = if (isSelected) BorderStroke(1.dp, color) else null,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onPrioritySelected(priority) }
                ) {
                    Text(
                        text = priority.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = color,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(vertical = 12.dp),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
