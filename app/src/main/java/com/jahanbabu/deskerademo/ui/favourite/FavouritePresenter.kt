package com.jahanbabu.deskerademo.ui.favourite

import com.jahanbabu.deskerademo.data.Item
import com.jahanbabu.deskerademo.data.source.ItemDataSource
import com.jahanbabu.deskerademo.data.source.ItemRepository

/**
 * Listens to user actions from the UI ([FavouriteFragment]), retrieves the data and updates
 * the UI as required.
 */
class FavouritePresenter(val itemView: FavouriteContract.View, val itemsRepository: ItemRepository) : FavouriteContract.Presenter {

    override fun getItems() {
        getItemData()
    }

    override fun updateFavourite(position: Int) {

    }


    init {
        itemView.presenter = this
    }

    override fun start() {
        getItemData()
    }

    private fun getItemData() {
        itemsRepository.getItems(object : ItemDataSource.LoadItemsCallback{
            override fun onItemLoaded(items: List<Item>) {
                val itemsToShow: MutableList<Item> = mutableListOf()

                for (item in items){
                    if (item.favourite)
                        itemsToShow.add(item)
                }

                itemView.setDataToView(itemsToShow)
            }

            override fun onDataNotAvailable() {

            }
        })
    }

    private lateinit var items: MutableList<Item>
}
