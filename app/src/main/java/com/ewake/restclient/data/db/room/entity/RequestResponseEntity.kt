package com.ewake.restclient.data.db.room.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import com.ewake.restclient.presentation.model.RequestMethod

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
@Entity(foreignKeys = [ForeignKey(
    entity = ScriptEntity::class,
    parentColumns = ["id"],
    childColumns = ["scriptId"],
    onDelete = CASCADE
)])
data class RequestResponseEntity(
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    var method: RequestMethod = RequestMethod.GET,
    var url: String = "",
    var headers: MutableList<Pair<String, String>> = mutableListOf(),
    var query: MutableList<Pair<String, String>> = mutableListOf(),
    var body: String = "",
    var code: Int = 0,
    var message: String = "",
    var responseBody: String = "",
    var scriptId: Int? = null
)