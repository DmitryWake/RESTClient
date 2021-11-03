package com.ewake.restclient.data.db.room.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ewake.restclient.data.db.room.entity.RequestResponseEntity

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@Dao
interface RequestResponseDao {
    @Query("SELECT * FROM RequestResponseEntity")
    suspend fun getAll(): List<RequestResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RequestResponseEntity)
}