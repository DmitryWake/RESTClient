package com.ewake.restclient.data.db.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ewake.restclient.data.db.room.dao.RequestResponseDao
import com.ewake.restclient.data.db.room.entity.RequestResponseEntity
import com.ewake.restclient.data.db.room.typeconverter.RequestResponseTypeConverter

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@Database(version = 1, entities = [RequestResponseEntity::class])
@TypeConverters(value = [RequestResponseTypeConverter::class])
abstract class AppDatabase : RoomDatabase() {
    abstract fun requestResponseDao(): RequestResponseDao
}