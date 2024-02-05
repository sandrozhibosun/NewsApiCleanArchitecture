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

/**
 *
 *     @TypeConverter
 *     fun employeeListToString(value: List<EmployeeEntity>): String? {
 *         val gson = Gson()
 *         return gson.toJson(value)
 *     }
 *
 *     @TypeConverter
 *     fun stringToEmployeeList(value: String?): List<EmployeeEntity> {
 *         return  Gson().fromJson(value, Array<EmployeeEntity>::class.java).toList()
 *
 *     }
 *
 *         @TypeConverter
 *     fun employeeToString(value: EmployeeEntity): String? {
 *         val gson = Gson()
 *         return gson.toJson(value)
 *     }
 *
 *     @TypeConverter
 *     fun stringToEmployee(value: String?): EmployeeEntity {
 *         return  Gson().fromJson(value, EmployeeEntity::class.java)
 *     }
 */
