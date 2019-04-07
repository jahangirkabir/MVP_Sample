package com.jahanbabu.deskerademo.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Item")
data class Item(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var favourite: Boolean = true,
    var url: String = "",
    var category: String = ""
)