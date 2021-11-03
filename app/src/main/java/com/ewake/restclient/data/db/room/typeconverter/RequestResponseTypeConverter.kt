package com.ewake.restclient.data.db.room.typeconverter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * @author Nikolaevsky Dmitry (@d.nikolaevskiy)
 */
class RequestResponseTypeConverter {

    @TypeConverter
    fun keyValueListToString(list: MutableList<Pair<String, String>>): String {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToKeyValueList(json: String): MutableList<Pair<String, String>> {
        val type = object : TypeToken<MutableList<Pair<String, String>>>() {}.type
        return Gson().fromJson(json, type)
    }
}