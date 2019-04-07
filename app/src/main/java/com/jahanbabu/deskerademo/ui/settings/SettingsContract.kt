package com.jahanbabu.deskerademo.ui.settings

import com.jahanbabu.deskerademo.BasePresenter
import com.jahanbabu.deskerademo.BaseView
import com.jahanbabu.deskerademo.data.User

/**
 * This specifies the contract between the view and the presenter.
 */
interface SettingsContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun setDataToView(user: User)

        fun showDOP(date: Long)

        fun showTempUnit(unit: String)
    }

    interface Presenter: BasePresenter {

        fun saveTempUnit(unit: String)

        fun saveProbationDate(dop: Long)

        fun saveSound(hobby: Boolean)

        fun saveNotification(doj: Boolean)

        fun goToProfilesDetails()
    }
}
