package com.jahanbabu.deskerademo.ui.fruitView.fruitDetail

import com.jahanbabu.deskerademo.data.Fruit
import com.jahanbabu.deskerademo.data.source.FruitRepository

/**
 * Listens to user actions from the UI ([FruitDetailsFragment]), retrieves the data and updates
 * the UI as required.
 */
class FruitDetailsPresenter(val fruitView: FruitDetailsContract.View, val fruitRepository: FruitRepository) : FruitDetailsContract.Presenter {
    override fun getFruit() {

    }

    override fun onSaveClick(id: Int, name: String) {
        fruitRepository.saveFruit(Fruit(id, name))
    }

    init {
        fruitView.presenter = this
    }

    override fun start() {
        getFruitData()
    }

    private fun getFruitData() {

    }
}
