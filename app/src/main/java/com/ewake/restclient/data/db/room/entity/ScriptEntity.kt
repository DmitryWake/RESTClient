package com.ewake.restclient.data.db.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScriptEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var name: String = ""
)
