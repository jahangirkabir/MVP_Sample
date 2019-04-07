package com.jahanbabu.deskerademo.data.source.local

import androidx.annotation.VisibleForTesting
import com.jahanbabu.deskerademo.data.Item
import com.jahanbabu.deskerademo.data.source.ItemDataSource
import com.jahanbabu.deskerademo.util.AppExecutors

/**
 * Concrete implementation of a data source as a db.
 */
class ItemLocalDataSource private constructor(val appExecutors: AppExecutors, val itemsDao: ItemsDao) : ItemDataSource {

    override fun getRelatedItems(key: String, callback: ItemDataSource.LoadItemsCallback) {
        appExecutors.diskIO.execute {
            val items = itemsDao.getRelatedItems(key)
            appExecutors.mainThread.execute {
                if (items.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onItemLoaded(items)
                }
            }
        }
    }

    /**
     * Note: [ItemDataSource.LoadItemsCallback.onDataNotAvailable] is fired if the database doesn't exist
     * or the table is empty.
     */
    override fun getItems(callback: ItemDataSource.LoadItemsCallback) {
        appExecutors.diskIO.execute {
            val items = itemsDao.getItems()
            appExecutors.mainThread.execute {
                if (items.isEmpty()) {
                    // This will be called if the table is new or just empty.
                    callback.onDataNotAvailable()
                } else {
                    callback.onItemLoaded(items)
                }
            }
        }
    }

    /**
     * Note: [ItemDataSource.GetItemCallback.onDataNotAvailable] is fired if the [Item] isn't
     * found.
     */
    override fun getItem(itemId: Int, callback: ItemDataSource.GetItemCallback) {
        appExecutors.diskIO.execute {
            val item = itemsDao.getItemById(itemId)
            appExecutors.mainThread.execute {
                if (item != null) {
                    callback.onItemLoaded(item)
                } else {
                    callback.onDataNotAvailable()
                }
            }
        }
    }

    override fun saveItem(item: Item) {
        appExecutors.diskIO.execute { itemsDao.insertItem(item) }
    }

    override fun refreshItems() {
        // Not required because the {@link ItemRepository} handles the logic of refreshing the
        // tasks from all the available data sources.
    }

    override fun deleteAllItems() {
        appExecutors.diskIO.execute { itemsDao.deleteItems() }
    }

    companion object {
        private var INSTANCE: ItemLocalDataSource? = null

        @JvmStatic
        fun getInstance(appExecutors: AppExecutors, itemsDao: ItemsDao): ItemLocalDataSource {
            if (INSTANCE == null) {
                synchronized(ItemLocalDataSource::javaClass) {
                    INSTANCE = ItemLocalDataSource(appExecutors, itemsDao)
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
