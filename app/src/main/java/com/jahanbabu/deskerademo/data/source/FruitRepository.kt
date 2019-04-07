package com.jahanbabu.deskerademo.data.source

import com.jahanbabu.deskerademo.data.Fruit
import java.util.ArrayList
import java.util.LinkedHashMap

class FruitRepository(val fruitRemoteDataSource: FruitDataSource, val fruitLocalDataSource: FruitDataSource):
    FruitDataSource {

    override fun getRelatedFruits(key: String, callback: FruitDataSource.LoadFruitsCallback) {
//        // Respond immediately with cache if available and not dirty
        if (cachedFruits.isNotEmpty() && !cacheIsDirty) {
            val relatedFruits = mutableListOf<Fruit>()
            val titleWords = key.split(" ")
            for (m in cachedFruits.values){
                if (!key.equals(m.name))
                    relatedFruits.add(m)
            }
            callback.onFruitLoaded(relatedFruits)
            return
        }
    }

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedFruits: LinkedHashMap<Int, Fruit> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    /**
     * Gets fruits from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     *
     * Note: [FruitDataSource.LoadFruitsCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getFruits(callback: FruitDataSource.LoadFruitsCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedFruits.isNotEmpty() && !cacheIsDirty) {
            callback.onFruitLoaded(ArrayList(cachedFruits.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getFruitsFromRemoteDataSource(callback)
        } else {
            // Query the local storage if available. If not, query the network.
            fruitLocalDataSource.getFruits(object : FruitDataSource.LoadFruitsCallback {
                override fun onFruitLoaded(fruits: List<Fruit>) {
                    refreshCache(fruits)
                    callback.onFruitLoaded(ArrayList(cachedFruits.values))
                }

                override fun onDataNotAvailable() {
                    getFruitsFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun saveFruit(fruit: Fruit) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(fruit) {
            fruitRemoteDataSource.saveFruit(it)
            fruitLocalDataSource.saveFruit(it)
        }
    }

    /**
     * Gets fruits from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [FruitDataSource.GetFruitCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getFruit(fruitId: Int, callback: FruitDataSource.GetFruitCallback) {
        val fruitInCache = getFruitWithId(fruitId)

        // Respond immediately with cache if available
        if (fruitInCache != null) {
            callback.onFruitLoaded(fruitInCache)
            return
        }

        // Load from server/persisted if needed.

        // Is the fruit in the local data source? If not, query the network.
        fruitLocalDataSource.getFruit(fruitId, object : FruitDataSource.GetFruitCallback {
            override fun onFruitLoaded(fruit: Fruit) {
                // Do in memory cache update to keep the app UI up to date
                cacheAndPerform(fruit) {
                    callback.onFruitLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                fruitRemoteDataSource.getFruit(fruitId, object : FruitDataSource.GetFruitCallback {
                    override fun onFruitLoaded(fruit: Fruit) {
                        // Do in memory cache update to keep the app UI up to date
                        cacheAndPerform(fruit) {
                            callback.onFruitLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    private fun getFruitsFromRemoteDataSource(callback: FruitDataSource.LoadFruitsCallback) {
        fruitRemoteDataSource.getFruits(object : FruitDataSource.LoadFruitsCallback {
            override fun onFruitLoaded(fruits: List<Fruit>) {
                refreshCache(fruits)
                refreshLocalDataSource(fruits)
                callback.onFruitLoaded(ArrayList(cachedFruits.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun refreshFruits() {
        cacheIsDirty = true
    }

    override fun deleteAllFruits() {
        fruitRemoteDataSource.deleteAllFruits()
        fruitLocalDataSource.deleteAllFruits()
        cachedFruits.clear()
    }

    private fun refreshCache(fruits: List<Fruit>) {
        cachedFruits.clear()
        fruits.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(fruits: List<Fruit>) {
        fruitLocalDataSource.deleteAllFruits()
        for (fruit in fruits) {
            fruitLocalDataSource.saveFruit(fruit)
        }
    }

    private fun getFruitWithId(id: Int) = cachedFruits[id]

    private inline fun cacheAndPerform(fruit: Fruit, perform: (Fruit) -> Unit) {
        val cachedFruit = Fruit(fruit.id, fruit.name).apply {
            id = fruit.id
            name = fruit.name
        }
        cachedFruits.put(cachedFruit.id, cachedFruit)
        perform(cachedFruit)
    }

    companion object {

        private var INSTANCE: FruitRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param fruitRemoteDataSource the backend data source
         * *
         * @param fruitLocalDataSource  the device storage data source
         * *
         * @return the [FruitRepository] instance
         */
        @JvmStatic fun getInstance(fruitRemoteDataSource: FruitDataSource,
                                   fruitLocalDataSource: FruitDataSource
        ): FruitRepository {
            return INSTANCE ?: FruitRepository(fruitRemoteDataSource, fruitLocalDataSource)
                    .apply { INSTANCE = this }
        }

        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}