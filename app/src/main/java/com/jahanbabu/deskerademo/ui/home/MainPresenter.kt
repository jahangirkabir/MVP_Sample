package com.jahanbabu.deskerademo.ui.home

import android.content.Context
import com.jahanbabu.deskerademo.data.User
import com.jahanbabu.deskerademo.data.source.FruitRepository

/**
 * Listens to user actions from the UI ([MainActivity]), retrieves the data and updates
 * the UI as required.
 */
class MainPresenter(val mainView: MainContract.View) : MainContract.Presenter {

    override fun onDestroy() {

    }

    init {
        mainView.presenter = this
    }

    lateinit var mContext: Context

    override fun start() {
        mainView.presenter = this
    }

}
