package com.example.storagedatabaseapp.data.db.typeconverters

import androidx.room.TypeConverter
import com.example.storagedatabaseapp.data.db.entities.Style
import com.google.gson.Gson

class StyleTypeConverter {

    @TypeConverter
    fun toStyle(styleString: String) =
        Gson().fromJson(styleString, Style::class.java)

    @TypeConverter
    fun fromStyle(style: Style) =
        Gson().toJson(style)
}