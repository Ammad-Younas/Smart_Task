package com.madi.smarttask.core.presentation.ui.theme

import androidx.compose.ui.graphics.Color
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority

fun getPriorityColor(priority: Priority): Color {
    return when (priority) {
        Priority.LOW -> Color(0xFF3B82F6)
        Priority.MEDIUM -> Color(0xFFF59E0B)
        Priority.HIGH -> Color(0xFFEF4444)
        Priority.URGENT -> Color(0xFF9333EA)
    }
}

fun getCategoryColor(category: Category): Color {
    return when (category) {
        Category.WORK -> Color(0xFF6366F1)
        Category.PERSONAL -> Color(0xFF10B981)
        Category.STUDY -> Color(0xFF06B6D4)
        Category.HEALTH -> Color(0xFF84CC16)
        Category.SHOPPING -> Color(0xFF64748B)
    }
}
