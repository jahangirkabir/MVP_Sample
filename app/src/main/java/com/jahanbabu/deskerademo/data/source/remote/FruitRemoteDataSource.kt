package com.jahanbabu.deskerademo.data.source.remote

import android.os.Handler
import androidx.annotation.VisibleForTesting
import com.jahanbabu.deskerademo.data.source.FruitDataSource
import com.google.common.collect.Lists
import com.google.gson.Gson
import com.jahanbabu.deskerademo.data.Fruit
import com.jahanbabu.deskerademo.data.json.FruitsData
import com.jahanbabu.deskerademo.data.json.SampleItemData
import com.jahanbabu.deskerademo.data.source.local.FruitsDao
import com.jahanbabu.deskerademo.util.AppExecutors
import com.jahanbabu.deskerademo.util.AppUtils

/**
 * Implementation of the data source that adds a latency simulating network.
 */
class FruitRemoteDataSource private constructor(val appExecutors: AppExecutors, val fruitsDao: FruitsDao
) : FruitDataSource {
    override fun getRelatedFruits(key: String, callback: FruitDataSource.LoadFruitsCallback) {
        // Simulate network by delaying the execution.
        val fruits = Lists.newArrayList(MOVIES_SERVICE_DATA.values)
        Handler().postDelayed({
            callback.onFruitLoaded(fruits)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    private val SERVICE_LATENCY_IN_MILLIS = 1L

    private var MOVIES_SERVICE_DATA = LinkedHashMap<Int, Fruit>()

    private fun addFruit(fruit: Fruit) {
        MOVIES_SERVICE_DATA.put(fruit.id, fruit)
    }

    override fun saveFruit(fruit: Fruit) {
        MOVIES_SERVICE_DATA.put(fruit.id, fruit)
    }

    /**
     * Note: [FruitDataSource.LoadFruitsCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getFruits(callback: FruitDataSource.LoadFruitsCallback) {
        // Simulate network by delaying the execution.

        val json = AppUtils.loadJSONFromAsset("fruits.json")

        val fruitData = Gson().fromJson(json, FruitsData::class.java)
        if (!fruitData.fruits.isNullOrEmpty()){
            for (item in fruitData.fruits.indices){
                fruitData.fruits[item].id = item
                addFruit(fruitData.fruits[item])
            }
        }

        val fruits = Lists.newArrayList(MOVIES_SERVICE_DATA.values)
        Handler().postDelayed({
            callback.onFruitLoaded(fruits)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    /**
     * Note: [FruitDataSource.GetFruitCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getFruit(fruitId: Int, callback: FruitDataSource.GetFruitCallback) {
        val task = MOVIES_SERVICE_DATA[fruitId]

        // Simulate network by delaying the execution.
        with(Handler()) {
            if (task != null) {
                postDelayed({ callback.onFruitLoaded(task) }, SERVICE_LATENCY_IN_MILLIS)
            } else {
                postDelayed({ callback.onDataNotAvailable() }, SERVICE_LATENCY_IN_MILLIS)
            }
        }
    }

    override fun refreshFruits() {
        // Not required because the {@link FruitRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllFruits() {
        MOVIES_SERVICE_DATA.clear()
    }

    companion object {
        private var INSTANCE: FruitRemoteDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, fruitsDao: FruitsDao): FruitRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(FruitRemoteDataSource::javaClass) {
                    INSTANCE = FruitRemoteDataSource(appExecutors, fruitsDao)
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