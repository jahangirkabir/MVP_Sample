package com.jahanbabu.deskerademo.data.source.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jahanbabu.deskerademo.data.Fruit
import com.jahanbabu.deskerademo.data.Item

/**
 * The Room Database that contains the Item & Fruit table.
 */
@Database(entities = arrayOf(Fruit::class, Item::class), version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun fruitsDao(): FruitsDao
    abstract fun itemsDao(): ItemsDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        private val lock = Any()

        fun getInstance(context: Context): AppDatabase {
            synchronized(lock) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            AppDatabase::class.java, "Deskera.db").fallbackToDestructiveMigration()
                            .build()
                }
                return INSTANCE!!
            }
        }
    }

}