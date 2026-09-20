package com.madi.smarttask.core.data.repository

import com.madi.smarttask.core.data.local.dao.CategoryDao
import com.madi.smarttask.core.data.local.entity.CategoryEntity
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart

class CategoryRepositoryImpl(
    private val dao: CategoryDao
) : CategoryRepository {

    override fun getCategories(): Flow<List<CategoryEntity>> {
        return dao.getCategories().onStart {
            ensureDefaultCategories()
        }
    }

    override suspend fun addCategory(name: String) {
        val trimmed = name.trim()
        if (trimmed.isNotBlank()) {
            dao.insertCategory(
                CategoryEntity(
                    name = trimmed,
                    isDefault = false
                )
            )
        }
    }

    override suspend fun updateCategory(id: Long, newName: String) {
        val trimmed = newName.trim()
        if (trimmed.isNotBlank()) {
            dao.updateCategory(
                CategoryEntity(
                    id = id,
                    name = trimmed,
                    isDefault = false
                )
            )
        }
    }

    override suspend fun deleteCategory(id: Long) {
        dao.deleteCategory(id)
    }

    private suspend fun ensureDefaultCategories() {
        if (dao.getCategoryCount() == 0) {
            val defaults = Category.entries.map { category ->
                val formattedName = category.name.lowercase().replaceFirstChar { it.uppercase() }
                CategoryEntity(
                    name = formattedName,
                    isDefault = true
                )
            }
            dao.insertCategories(defaults)
        }
    }
}
