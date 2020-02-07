package com.example.imageapi

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MyConverters {

    @TypeConverter
    fun listToString(list : List<String>) : String{
       return Gson().toJson(list)
    }

    @TypeConverter
    fun stringToList(value: String) : List<String> {
        return Gson().fromJson(value, object : TypeToken<List<String>> () {}.type)
    }
}