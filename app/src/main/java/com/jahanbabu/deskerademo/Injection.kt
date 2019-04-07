package com.jahanbabu.deskerademo

import android.content.Context
import com.jahanbabu.deskerademo.data.source.FruitRepository
import com.jahanbabu.deskerademo.data.source.ItemRepository
import com.jahanbabu.deskerademo.data.source.local.FruitLocalDataSource
import com.jahanbabu.deskerademo.data.source.local.AppDatabase
import com.jahanbabu.deskerademo.data.source.local.ItemLocalDataSource
import com.jahanbabu.deskerademo.data.source.remote.FruitRemoteDataSource
import com.jahanbabu.deskerademo.data.source.remote.ItemRemoteDataSource
import com.jahanbabu.deskerademo.util.AppExecutors

/**
 * Enables injection of mock implementations for
 * [FruitLocalDataSource] at compile time. This is useful for testing, since it allows us to use
 * a fake instance of the class to isolate the dependencies and run a test hermetically.
 */
object Injection {
    fun provideItemRepository(context: Context): ItemRepository {
        val database = AppDatabase.getInstance(context)
        return ItemRepository.getInstance(
            ItemRemoteDataSource.getInstance(AppExecutors(), database.itemsDao()),
            ItemLocalDataSource.getInstance(AppExecutors(), database.itemsDao()))
    }

    fun provideFruitRepository(context: Context): FruitRepository {
        val database = AppDatabase.getInstance(context)
        return FruitRepository.getInstance(
            FruitRemoteDataSource.getInstance(AppExecutors(), database.fruitsDao()),
                FruitLocalDataSource.getInstance(AppExecutors(), database.fruitsDao()))
    }
}
