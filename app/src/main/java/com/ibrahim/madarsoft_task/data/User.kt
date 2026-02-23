package com.ibrahim.madarsoft_task.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val age: Int,
    val jobTitle: String,
    val gender: Gender
)

