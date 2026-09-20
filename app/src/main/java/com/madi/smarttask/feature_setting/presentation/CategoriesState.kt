package com.madi.smarttask.feature_setting.presentation

import com.madi.smarttask.core.data.local.entity.CategoryEntity
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState

data class CategoriesState(
    val categories: List<CategoryEntity> = emptyList(),
    val showAddDialog: Boolean = false,
    val newCategoryName: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val categoryToEdit: CategoryEntity? = null,
    val editCategoryName: SmartTaskTextFieldState = SmartTaskTextFieldState(),
    val categoryToDelete: CategoryEntity? = null,
)
