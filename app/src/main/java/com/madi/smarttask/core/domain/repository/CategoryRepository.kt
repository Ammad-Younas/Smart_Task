package com.madi.smarttask.core.domain.repository

import com.madi.smarttask.core.data.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategories(): Flow<List<CategoryEntity>>
    suspend fun addCategory(name: String)
    suspend fun updateCategory(id: Long, newName: String)
    suspend fun deleteCategory(id: Long)
}
