package com.jahanbabu.deskerademo.ui.items

import com.jahanbabu.deskerademo.data.Item
import com.jahanbabu.deskerademo.data.User
import com.jahanbabu.deskerademo.data.source.ItemDataSource
import com.jahanbabu.deskerademo.data.source.ItemRepository
import com.orhanobut.hawk.Hawk

/**
 * Listens to user actions from the UI ([ItemFragment]), retrieves the data and updates
 * the UI as required.
 */
class ItemPresenter(val itemView: ItemContract.View, val itemsRepository: ItemRepository) : ItemContract.Presenter {

    override fun getItems(mPage: Int) {
        getItemData(mPage)
    }

    override fun updateFavourite(position: Int) {

    }


    init {
        itemView.presenter = this
    }

    override fun start() {

    }

    private fun getItemData(mPage: Int) {
        itemsRepository.getItems(object : ItemDataSource.LoadItemsCallback{
            override fun onItemLoaded(items: List<Item>) {
                val itemsToShow: MutableList<Item> = mutableListOf()

                if (mPage == 0){
                    itemsToShow.addAll(items)
                } else if (mPage == 1){
                    for (item in items){
                        if (item.category.equals("A"))
                            itemsToShow.add(item)
                    }
                } else if (mPage == 2){
                    for (item in items){
                        if (item.category.equals("B"))
                            itemsToShow.add(item)
                    }
                }

                itemView.setDataToView(itemsToShow)
            }

            override fun onDataNotAvailable() {

            }
        })
    }

    private lateinit var items: MutableList<Item>
}
