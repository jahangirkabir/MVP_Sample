package com.jahanbabu.deskerademo.data.source.local

import androidx.annotation.VisibleForTesting
import com.jahanbabu.deskerademo.data.Fruit
import com.jahanbabu.deskerademo.data.source.FruitDataSource
import com.jahanbabu.deskerademo.util.AppExecutors

/**
 * Concrete implementation of a data source as a db.
 */
class FruitLocalDataSource private constructor(val appExecutors: AppExecutors, val fruitsDao: FruitsDao) : FruitDataSource {

    override fun getRelatedFruits(key: String, callback: FruitDataSource.LoadFruitsCallback) {
        appExecutors.diskIO.execute {
            val fruits = fruitsDao.getRelatedFruits(key)
            appExecutors.mainThread.execute {
                if (fruits.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onFruitLoaded(fruits)
                }
            }
        }
    }

    /**
     * Note: [FruitDataSource.LoadFruitsCallback.onDataNotAvailable] is fired if the database doesn't exist
     * or the table is empty.
     */
    override fun getFruits(callback: FruitDataSource.LoadFruitsCallback) {
        appExecutors.diskIO.execute {
            val fruits = fruitsDao.getFruits()
            appExecutors.mainThread.execute {
                if (fruits.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onFruitLoaded(fruits)
                }
            }
        }
    }

    /**
     * Note: [FruitDataSource.GetFruitCallback.onDataNotAvailable] is fired if the [Fruit] isn't
     * found.
     */
    override fun getFruit(fruitId: Int, callback: FruitDataSource.GetFruitCallback) {
        appExecutors.diskIO.execute {
            val fruit = fruitsDao.getFruitById(fruitId)
            appExecutors.mainThread.execute {
                if (fruit != null) {
                    callback.onFruitLoaded(fruit)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveFruit(fruit: Fruit) {
        appExecutors.diskIO.execute { fruitsDao.insertFruit(fruit) }
    }

    override fun refreshFruits() {
        // Not required because the {@link FruitRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllFruits() {
        appExecutors.diskIO.execute { fruitsDao.deleteFruits() }
    }

    companion object {
        private var INSTANCE: FruitLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, fruitsDao: FruitsDao): FruitLocalDataSource {
            if (INSTANCE == null) {
                synchronized(FruitLocalDataSource::javaClass) {
                    INSTANCE = FruitLocalDataSource(appExecutors, fruitsDao)
                }
            }
            return INSTANCE!!
        }

        @VisibleForTesting
        fun clearInstance() {
            INSTANCE = null
        }
    }
}
