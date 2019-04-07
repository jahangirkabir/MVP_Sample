package com.jahanbabu.deskerademo.ui.fruitView.fruitDetail

import com.jahanbabu.deskerademo.BasePresenter
import com.jahanbabu.deskerademo.BaseView
import com.jahanbabu.deskerademo.data.Fruit

/**
 * This specifies the contract between the view and the presenter.
 */
interface FruitDetailsContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun setDataToView(fruits: Fruit)
    }

    interface Presenter: BasePresenter {

        fun getFruit()

        fun onSaveClick(id: Int, name: String)
    }
}
