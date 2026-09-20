package com.madi.smarttask.feature_setting.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.madi.smarttask.core.domain.states.SmartTaskTextFieldState
import com.madi.smarttask.core.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoriesViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoriesState())
    val state: StateFlow<CategoriesState> = _state.asStateFlow()

    init {
        categoryRepository.getCategories().onEach { categories ->
            _state.update { it.copy(categories = categories) }
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: CategoriesEvent) {
        when (event) {
            CategoriesEvent.ShowAddDialog -> {
                _state.update { it.copy(showAddDialog = true, newCategoryName = SmartTaskTextFieldState()) }
            }
            CategoriesEvent.DismissAddDialog -> {
                _state.update { it.copy(showAddDialog = false, newCategoryName = SmartTaskTextFieldState()) }
            }
            is CategoriesEvent.NewCategoryNameChanged -> {
                _state.update { it.copy(newCategoryName = it.newCategoryName.copy(text = event.name, error = null)) }
            }
            CategoriesEvent.AddCategory -> {
                val name = state.value.newCategoryName.text.trim()
                if (name.isNotBlank()) {
                    viewModelScope.launch {
                        categoryRepository.addCategory(name)
                        _state.update { it.copy(showAddDialog = false, newCategoryName = SmartTaskTextFieldState()) }
                    }
                }
            }
            is CategoriesEvent.ShowEditDialog -> {
                _state.update {
                    it.copy(
                        categoryToEdit = event.category,
                        editCategoryName = SmartTaskTextFieldState(text = event.category.name)
                    )
                }
            }
            CategoriesEvent.DismissEditDialog -> {
                _state.update { it.copy(categoryToEdit = null, editCategoryName = SmartTaskTextFieldState()) }
            }
            is CategoriesEvent.EditCategoryNameChanged -> {
                _state.update { it.copy(editCategoryName = it.editCategoryName.copy(text = event.name, error = null)) }
            }
            CategoriesEvent.UpdateCategory -> {
                val category = state.value.categoryToEdit
                val newName = state.value.editCategoryName.text.trim()
                if (category != null && newName.isNotBlank()) {
                    viewModelScope.launch {
                        categoryRepository.updateCategory(category.id, newName)
                        _state.update { it.copy(categoryToEdit = null, editCategoryName = SmartTaskTextFieldState()) }
                    }
                }
            }
            is CategoriesEvent.ShowDeleteDialog -> {
                _state.update { it.copy(categoryToDelete = event.category) }
            }
            CategoriesEvent.DismissDeleteDialog -> {
                _state.update { it.copy(categoryToDelete = null) }
            }
            CategoriesEvent.DeleteCategory -> {
                val category = state.value.categoryToDelete
                if (category != null) {
                    viewModelScope.launch {
                        categoryRepository.deleteCategory(category.id)
                        _state.update { it.copy(categoryToDelete = null) }
                    }
                }
            }
        }
    }
}
