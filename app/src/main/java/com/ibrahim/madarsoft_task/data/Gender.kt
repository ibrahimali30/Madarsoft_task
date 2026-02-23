package com.ibrahim.madarsoft_task.data

import androidx.room.TypeConverter

enum class Gender {
    MALE,
    FEMALE
}

class Converters {
    @TypeConverter
    fun fromGender(gender: Gender): String = gender.name

    @TypeConverter
    fun toGender(value: String): Gender = try {
        Gender.valueOf(value)
    } catch (e: Exception) {
        Gender.MALE
    }
}
