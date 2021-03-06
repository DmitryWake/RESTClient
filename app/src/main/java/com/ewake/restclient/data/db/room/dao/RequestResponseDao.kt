package com.ewake.restclient.data.db.room.dao

import androidx.room.*
import com.ewake.restclient.data.db.room.entity.RequestResponseEntity

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@Dao
interface RequestResponseDao {
    @Query("SELECT * FROM RequestResponseEntity")
    suspend fun getAll(): List<RequestResponseEntity>

    @Query("SELECT * FROM RequestResponseEntity WHERE scriptId IS NULL")
    suspend fun getAllFromHistory(): List<RequestResponseEntity>

    @Query("SELECT * FROM RequestResponseEntity WHERE (url LIKE :searchQuery OR code LIKE :searchQuery OR method LIKE :searchQuery) AND scriptId is NULL ")
    suspend fun getFromHistory(searchQuery: String): List<RequestResponseEntity>

    @Query("SELECT * FROM RequestResponseEntity WHERE scriptId LIKE :scriptId")
    suspend fun getFromScript(scriptId: Int): List<RequestResponseEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: RequestResponseEntity)

    @Delete
    suspend fun delete(entity: RequestResponseEntity)

    @Update
    suspend fun update(entity: RequestResponseEntity)
}