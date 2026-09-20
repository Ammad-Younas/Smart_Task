package com.madi.smarttask.feature_setting.presentation

import com.madi.smarttask.core.data.local.entity.CategoryEntity

sealed class CategoriesEvent {
    object ShowAddDialog : CategoriesEvent()
    object DismissAddDialog : CategoriesEvent()
    data class NewCategoryNameChanged(val name: String) : CategoriesEvent()
    object AddCategory : CategoriesEvent()

    data class ShowEditDialog(val category: CategoryEntity) : CategoriesEvent()
    object DismissEditDialog : CategoriesEvent()
    data class EditCategoryNameChanged(val name: String) : CategoriesEvent()
    object UpdateCategory : CategoriesEvent()

    data class ShowDeleteDialog(val category: CategoryEntity) : CategoriesEvent()
    object DismissDeleteDialog : CategoriesEvent()
    object DeleteCategory : CategoriesEvent()
}
