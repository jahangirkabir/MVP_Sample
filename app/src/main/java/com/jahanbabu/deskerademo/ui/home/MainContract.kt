package com.jahanbabu.deskerademo.ui.home

import com.jahanbabu.deskerademo.data.User
import com.jahanbabu.deskerademo.BasePresenter
import com.jahanbabu.deskerademo.BaseView

interface MainContract {

    interface View : BaseView<Presenter> {

        fun showProgress()

        fun hideProgress()

        fun setView()

        fun navigateToDetailScreen(complete: Boolean)
    }

    interface Presenter : BasePresenter {

        fun onDestroy()

    }
}
