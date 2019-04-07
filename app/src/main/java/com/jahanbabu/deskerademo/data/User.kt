package com.jahanbabu.deskerademo.data

import androidx.room.PrimaryKey

data class User(
    @PrimaryKey
    var name: String = "Kabir",
    var hobby: String = "",
    var email: String = "",
    var doj: Long = 0,
    var dop: Long = 0,
    var imageUrl: String = "",
    var tempUnit: String = "Farenheit",
    var sound: Boolean = true,
    var notification: Boolean = true
)