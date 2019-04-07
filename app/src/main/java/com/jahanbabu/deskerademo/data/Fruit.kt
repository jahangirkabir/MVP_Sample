package com.jahanbabu.deskerademo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fruit")
data class Fruit(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = ""
)