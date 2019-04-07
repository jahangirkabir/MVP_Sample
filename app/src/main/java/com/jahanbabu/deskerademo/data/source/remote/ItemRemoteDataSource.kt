package com.jahanbabu.deskerademo.data.source.remote

import android.os.Handler
import androidx.annotation.VisibleForTesting
import com.jahanbabu.deskerademo.data.source.ItemDataSource
import com.google.common.collect.Lists
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jahanbabu.deskerademo.data.Item
import com.jahanbabu.deskerademo.data.json.SampleItemData
import com.jahanbabu.deskerademo.data.source.local.ItemsDao
import com.jahanbabu.deskerademo.util.AppExecutors
import com.jahanbabu.deskerademo.util.AppUtils

/**
 * Implementation of the data source that adds a latency simulating network.
 */
class ItemRemoteDataSource private constructor(val appExecutors: AppExecutors, val itemsDao: ItemsDao
) : ItemDataSource {
    override fun getRelatedItems(key: String, callback: ItemDataSource.LoadItemsCallback) {
        // Simulate network by delaying the execution.
        val items = Lists.newArrayList(ITEMS_SERVICE_DATA.values)
        Handler().postDelayed({
            callback.onItemLoaded(items)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    private val SERVICE_LATENCY_IN_MILLIS = 1L

    private var ITEMS_SERVICE_DATA = LinkedHashMap<Int, Item>()


    private fun addItem(item: Item) {
//        val item = Item(title, description, favorite, url, category)
        ITEMS_SERVICE_DATA.put(item.id, item)
    }

    override fun saveItem(item: Item) {
        ITEMS_SERVICE_DATA.put(item.id, item)
    }

    /**
     * Note: [ItemDataSource.LoadItemsCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getItems(callback: ItemDataSource.LoadItemsCallback) {
        // Simulate network by delaying the execution.

        val json = AppUtils.loadJSONFromAsset("items.json")

        val sampleItemData = Gson().fromJson(json, SampleItemData::class.java)
        if (!sampleItemData.items.isNullOrEmpty()){
            for (item in sampleItemData.items.indices){
                sampleItemData.items[item].id = item
                addItem(sampleItemData.items[item])
            }
        }

        val items = Lists.newArrayList(ITEMS_SERVICE_DATA.values)

        Handler().postDelayed({
            callback.onItemLoaded(items)
        }, SERVICE_LATENCY_IN_MILLIS)
    }

    /**
     * Note: [ItemDataSource.GetItemCallback.onDataNotAvailable] is never fired. In a real remote data
     * source implementation, this would be fired if the server can't be contacted or the server
     * returns an error.
     */
    override fun getItem(itemId: Int, callback: ItemDataSource.GetItemCallback) {
        val task = ITEMS_SERVICE_DATA[itemId]

        // Simulate network by delaying the execution.
        with(Handler()) {
            if (task != null) {
                postDelayed({ callback.onItemLoaded(task) }, SERVICE_LATENCY_IN_MILLIS)
            } else {
                postDelayed({ callback.onDataNotAvailable() }, SERVICE_LATENCY_IN_MILLIS)
            }
        }
    }

    override fun refreshItems() {
        // Not required because the {@link ItemRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllItems() {
        ITEMS_SERVICE_DATA.clear()
    }

    companion object {
        private var INSTANCE: ItemRemoteDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, itemsDao: ItemsDao): ItemRemoteDataSource {
            if (INSTANCE == null) {
                synchronized(ItemRemoteDataSource::javaClass) {
                    INSTANCE = ItemRemoteDataSource(appExecutors, itemsDao)
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