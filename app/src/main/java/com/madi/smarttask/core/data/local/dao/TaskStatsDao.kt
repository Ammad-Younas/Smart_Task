package com.madi.smarttask.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.madi.smarttask.core.data.local.entity.TaskStatsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskStatsDao {

    @Query("SELECT * FROM task_stats WHERE id = 1")
    fun getStats(): Flow<TaskStatsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateStats(stats: TaskStatsEntity)

    @Query("DELETE FROM task_stats")
    suspend fun clearStats()
}
