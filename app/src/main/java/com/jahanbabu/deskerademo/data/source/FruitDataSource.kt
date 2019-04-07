package com.jahanbabu.deskerademo.data.source

import com.jahanbabu.deskerademo.data.Fruit

interface FruitDataSource {

    interface LoadFruitsCallback {

        fun onFruitLoaded(fruits: List<Fruit>)

        fun onDataNotAvailable()
    }

    interface GetFruitCallback {

        fun onFruitLoaded(fruit: Fruit)

        fun onDataNotAvailable()
    }

    fun saveFruit(fruit: Fruit)

    fun getFruits(callback: LoadFruitsCallback)

    fun getRelatedFruits(key: String, callback: LoadFruitsCallback)

    fun getFruit(fruitId: Int, callback: GetFruitCallback)

    fun refreshFruits()

    fun deleteAllFruits()

}