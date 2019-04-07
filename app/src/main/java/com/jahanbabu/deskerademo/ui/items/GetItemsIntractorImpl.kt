package com.jahanbabu.deskerademo.ui.items

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.jahanbabu.deskerademo.data.Item
import com.jahanbabu.deskerademo.util.AppUtils

class GetItemsIntractorImpl: ItemContract.GetItemIntractor {
    override fun getItemList(onFinishedListener: ItemContract.GetItemIntractor.OnFinishedListener) {

        try {
            val json = AppUtils.loadJSONFromAsset("items.josn")

            val itemType = object : TypeToken<List<Item>>() {}.type
            val items = Gson().fromJson<List<Item>>(json, itemType)

            onFinishedListener.onFinished(items)
        } catch (e: Exception){
            onFinishedListener.onFailure(e)
        }
    }
}