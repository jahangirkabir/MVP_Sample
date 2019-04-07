package com.jahanbabu.deskerademo.ui.items

import com.jahanbabu.deskerademo.BasePresenter
import com.jahanbabu.deskerademo.BaseView
import com.jahanbabu.deskerademo.data.Item

/**
 * This specifies the contract between the view and the presenter.
 */
interface ItemContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun setDataToView(items: MutableList<Item>)

//        fun showEmailError(state: Boolean, msg: String)
//
//        fun showItemsToRV(items: MutableList<Item>)
    }

    interface Presenter: BasePresenter {

        fun getItems(mPage: Int)

        fun updateFavourite(position: Int)

//        fun saveHobby(hobby: String)
//
//        fun saveDOJ(doj: String)
//
//        fun saveImage(cameraImageUri: String)
    }

    /**
     * Intractors are classes built for fetching data from your database, web services, or any other data source.
     **/
    interface GetItemIntractor {

        interface OnFinishedListener {
            fun onFinished(itemList: List<Item>)
            fun onFailure(t: Throwable)
        }

        fun getItemList(onFinishedListener: OnFinishedListener)
    }
}
