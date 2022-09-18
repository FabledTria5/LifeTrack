package com.example.lifetrack.data.db.utils

import androidx.room.TypeConverter
import java.time.LocalDate

object LocalDateConverter {

    @TypeConverter
    fun toLocalDate(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate): Long = localDate.toEpochDay()

}