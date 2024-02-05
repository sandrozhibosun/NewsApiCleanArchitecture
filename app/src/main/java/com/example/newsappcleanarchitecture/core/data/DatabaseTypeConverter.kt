package com.example.newsappcleanarchitecture.core.data

import androidx.room.TypeConverter
import com.google.gson.Gson

class DatabaseTypeConverter {

    @TypeConverter
    fun stringListToString(value: List<String>): String? {
        val gson = Gson()
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToStringList(value: String?): List<String> {
        return Gson().fromJson(value, Array<String>::class.java).toList()
    }
}