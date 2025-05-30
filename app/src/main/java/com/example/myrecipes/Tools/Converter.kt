package com.example.myrecipes.Tools

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converter {
    @TypeConverter
    fun fromStringList(list: List<String>?): String? {
        return Gson().toJson(list)
    }

    @TypeConverter
    fun toStringList(json: String?): List<String>? {
        val type = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(json, type)
    }

}