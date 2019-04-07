package com.jahanbabu.deskerademo.ui.profile

import android.net.Uri
import com.jahanbabu.deskerademo.BasePresenter
import com.jahanbabu.deskerademo.BaseView
import com.jahanbabu.deskerademo.data.User

/**
 * This specifies the contract between the view and the presenter.
 */
interface ProfileContract {

    interface View : BaseView<Presenter> {

        val isActive: Boolean

        fun setUserDataToView(user: User)

        fun showEmailError(state: Boolean, msg: String)

        fun showDOJ(doj: Long)

        fun showDOJDialog(user: User)
    }

    interface Presenter: BasePresenter {

        fun saveEmail(email: String)

        fun saveHobby(hobby: String)

        fun onDOJClick()

        fun saveDOJ(doj: Long)

        fun saveImage(cameraImageUri: String)
    }
}
