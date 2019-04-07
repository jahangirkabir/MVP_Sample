package com.jahanbabu.deskerademo.ui.fruitView

import com.jahanbabu.deskerademo.BasePresenter
import com.jahanbabu.deskerademo.BaseView
import com.jahanbabu.deskerademo.data.Fruit

/**
 * This specifies the contract between the view and the presenter.
 */
interface FruitContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun setDataToView(fruits: MutableList<Fruit>)

        fun showNewItem()

        fun showUpdateItem(pos: Int)

        fun showCurrentItems()
    }

    interface Presenter: BasePresenter {

        fun getFruits()

        fun onAddClick(size: Int)
    }
}
