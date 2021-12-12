package com.ewake.restclient.data.db.room.dao

import androidx.room.*
import com.ewake.restclient.data.db.room.entity.ScriptEntity

@Dao
interface ScriptDao {

    @Query("SELECT * FROM ScriptEntity")
    suspend fun getAll(): List<ScriptEntity>

    @Query("SELECT * FROM ScriptEntity WHERE id LIKE :id LIMIT 1")
    suspend fun getById(id: Int): ScriptEntity?

    @Update
    suspend fun update(scriptEntity: ScriptEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(scriptEntity: ScriptEntity)

    @Delete
    suspend fun delete(scriptEntity: ScriptEntity)
}