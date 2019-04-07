package com.jahanbabu.deskerademo.data.source

import com.jahanbabu.deskerademo.data.Item
import java.util.ArrayList
import java.util.LinkedHashMap

class ItemRepository(val itemRemoteDataSource: ItemDataSource, val itemLocalDataSource: ItemDataSource):
    ItemDataSource {

    override fun getRelatedItems(key: String, callback: ItemDataSource.LoadItemsCallback) {
//        // Respond immediately with cache if available and not dirty
        if (cachedItems.isNotEmpty() && !cacheIsDirty) {
            val relatedItems = mutableListOf<Item>()
            val titleWords = key.split(" ")
            for (m in cachedItems.values){
                if (!key.equals(m.title))
                    relatedItems.add(m)
            }
            callback.onItemLoaded(relatedItems)
            return
        }
    }

    /**
     * This variable has public visibility so it can be accessed from tests.
     */
    var cachedItems: LinkedHashMap<Int, Item> = LinkedHashMap()

    /**
     * Marks the cache as invalid, to force an update the next time data is requested. This variable
     * has package local visibility so it can be accessed from tests.
     */
    var cacheIsDirty = false

    /**
     * Gets items from cache, local data source (SQLite) or remote data source, whichever is
     * available first.
     *
     *
     * Note: [ItemDataSource.LoadItemsCallback.onDataNotAvailable] is fired if all data sources fail to
     * get the data.
     */
    override fun getItems(callback: ItemDataSource.LoadItemsCallback) {
        // Respond immediately with cache if available and not dirty
        if (cachedItems.isNotEmpty() && !cacheIsDirty) {
            callback.onItemLoaded(ArrayList(cachedItems.values))
            return
        }

        if (cacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getItemsFromRemoteDataSource(callback)
        } else {
            // Query the local storage if available. If not, query the network.
            itemLocalDataSource.getItems(object : ItemDataSource.LoadItemsCallback {
                override fun onItemLoaded(items: List<Item>) {
                    refreshCache(items)
                    callback.onItemLoaded(ArrayList(cachedItems.values))
                }

                override fun onDataNotAvailable() {
                    getItemsFromRemoteDataSource(callback)
                }
            })
        }
    }

    override fun saveItem(item: Item) {
        // Do in memory cache update to keep the app UI up to date
        cacheAndPerform(item) {
            itemRemoteDataSource.saveItem(it)
            itemLocalDataSource.saveItem(it)
        }
    }

    /**
     * Gets items from local data source (sqlite) unless the table is new or empty. In that case it
     * uses the network data source. This is done to simplify the sample.
     *
     *
     * Note: [ItemDataSource.GetItemCallback.onDataNotAvailable] is fired if both data sources fail to
     * get the data.
     */
    override fun getItem(itemId: Int, callback: ItemDataSource.GetItemCallback) {
        val itemInCache = getItemWithId(itemId)

        // Respond immediately with cache if available
        if (itemInCache != null) {
            callback.onItemLoaded(itemInCache)
            return
        }

        // Load from server/persisted if needed.

        // Is the item in the local data source? If not, query the network.
        itemLocalDataSource.getItem(itemId, object : ItemDataSource.GetItemCallback {
            override fun onItemLoaded(item: Item) {
                // Do in memory cache update to keep the app UI up to date
                cacheAndPerform(item) {
                    callback.onItemLoaded(it)
                }
            }

            override fun onDataNotAvailable() {
                itemRemoteDataSource.getItem(itemId, object : ItemDataSource.GetItemCallback {
                    override fun onItemLoaded(item: Item) {
                        // Do in memory cache update to keep the app UI up to date
                        cacheAndPerform(item) {
                            callback.onItemLoaded(it)
                        }
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }
                })
            }
        })
    }

    private fun getItemsFromRemoteDataSource(callback: ItemDataSource.LoadItemsCallback) {
        itemRemoteDataSource.getItems(object : ItemDataSource.LoadItemsCallback {
            override fun onItemLoaded(items: List<Item>) {
                refreshCache(items)
                refreshLocalDataSource(items)
                callback.onItemLoaded(ArrayList(cachedItems.values))
            }

            override fun onDataNotAvailable() {
                callback.onDataNotAvailable()
            }
        })
    }

    override fun refreshItems() {
        cacheIsDirty = true
    }

    override fun deleteAllItems() {
        itemRemoteDataSource.deleteAllItems()
        itemLocalDataSource.deleteAllItems()
        cachedItems.clear()
    }

    private fun refreshCache(items: List<Item>) {
        cachedItems.clear()
        items.forEach {
            cacheAndPerform(it) {}
        }
        cacheIsDirty = false
    }

    private fun refreshLocalDataSource(items: List<Item>) {
        itemLocalDataSource.deleteAllItems()
        for (item in items) {
            itemLocalDataSource.saveItem(item)
        }
    }

    private fun getItemWithId(id: Int) = cachedItems[id]

    private inline fun cacheAndPerform(item: Item, perform: (Item) -> Unit) {
        val cachedItem = Item(item.id, item.title, item.description, item.favourite, item.url, item.category).apply {
            id = item.id
            title = item.title
            description = item.description
            favourite = item.favourite
            url = item.url
            category = item.category
        }
        cachedItems.put(cachedItem.id, cachedItem)
        perform(cachedItem)
    }

    companion object {

        private var INSTANCE: ItemRepository? = null

        /**
         * Returns the single instance of this class, creating it if necessary.

         * @param itemRemoteDataSource the backend data source
         * *
         * @param itemLocalDataSource  the device storage data source
         * *
         * @return the [ItemRepository] instance
         */
        @JvmStatic fun getInstance(itemRemoteDataSource: ItemDataSource,
                                   itemLocalDataSource: ItemDataSource
        ): ItemRepository {
            return INSTANCE ?: ItemRepository(itemRemoteDataSource, itemLocalDataSource)
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