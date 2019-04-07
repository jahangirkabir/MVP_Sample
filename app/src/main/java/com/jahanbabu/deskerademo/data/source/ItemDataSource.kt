package com.jahanbabu.deskerademo.data.source

import com.jahanbabu.deskerademo.data.Item

interface ItemDataSource {

    interface LoadItemsCallback {

        fun onItemLoaded(items: List<Item>)

        fun onDataNotAvailable()
    }

    interface GetItemCallback {

        fun onItemLoaded(item: Item)

        fun onDataNotAvailable()
    }

    fun saveItem(item: Item)

    fun getItems(callback: LoadItemsCallback)

    fun getRelatedItems(key: String, callback: LoadItemsCallback)

    fun getItem(itemId: Int, callback: GetItemCallback)

    fun refreshItems()

    fun deleteAllItems()

}