package com.jahanbabu.deskerademo.ui.fruitView

import com.jahanbabu.deskerademo.data.Fruit
import com.jahanbabu.deskerademo.data.source.FruitDataSource
import com.jahanbabu.deskerademo.data.source.FruitRepository
import com.jahanbabu.deskerademo.util.AppUtils

/**
 * Listens to user actions from the UI ([FruitFragment]), retrieves the data and updates
 * the UI as required.
 */
class FruitPresenter(val fruitView: FruitContract.View, val fruitRepository: FruitRepository) : FruitContract.Presenter {
    override fun onAddClick(size: Int) {
        val randomFruit = AppUtils.random()
        val f = Fruit()
        f.id = size+1
        f.name = randomFruit
        fruitRepository.saveFruit(f)

        fruitView.showNewItem()

        getFruitData()
    }

    override fun getFruits() {
        getFruitData()
    }

    init {
        fruitView.presenter = this
    }

    override fun start() {
        getFruitData()
    }

    private fun getFruitData() {
        fruitRepository.getFruits(object : FruitDataSource.LoadFruitsCallback{
            override fun onFruitLoaded(fruits: List<Fruit>) {
                val sortedF = fruits.sortedBy {
                    it.name
                }
                fruitView.setDataToView(sortedF as MutableList<Fruit>)
            }

            override fun onDataNotAvailable() {

            }
        })
    }

    private lateinit var fruits: MutableList<Fruit>
}
