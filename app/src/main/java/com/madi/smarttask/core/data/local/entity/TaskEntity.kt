package com.madi.smarttask.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.madi.smarttask.core.domain.model.Category
import com.madi.smarttask.core.domain.model.Priority
import com.madi.smarttask.core.domain.model.Task
import com.madi.smarttask.core.domain.model.TaskStatus

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String?,
    val isComplete: Boolean,
    val category: Category,
    val priority: Priority,
    val dueDate: Long,
    val status: TaskStatus = TaskStatus.PENDING
) {
    fun toTask(): Task {
        return Task(
            title = title,
            description = description,
            isCompleted = isComplete,
            category = category,
            priority = priority,
            dueDate = dueDate,
        )
    }

    companion object {
        fun fromTask(task: Task) : TaskEntity {
            return TaskEntity(
                title = task.title,
                description = task.description,
                isComplete = task.isCompleted,
                category = task.category,
                priority = task.priority,
                dueDate = task.dueDate,
            )
        }
    }
}

