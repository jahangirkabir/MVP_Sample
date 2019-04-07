package com.jahanbabu.deskerademo.ui.profile

import android.net.Uri
import android.util.Patterns
import com.jahanbabu.deskerademo.data.User
import com.jahanbabu.deskerademo.data.source.FruitRepository
import com.orhanobut.hawk.Hawk

/**
 * Listens to user actions from the UI ([ProfileFragment]), retrieves the data and updates
 * the UI as required.
 */
class ProfilePresenter(val profileView: ProfileContract.View) : ProfileContract.Presenter {

    override fun onDOJClick() {
        with(profileView){
            val u = Hawk.get("User", User())
            showDOJDialog(u)
        }
    }

    override fun saveEmail(email: String) {
        if (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email.toString().trim { it <= ' ' }).matches()){
            with(profileView){
                showEmailError(false, "")
                val u = Hawk.get("User", User())
                u.email = email
                Hawk.put("User", u)
            }
        } else {
            with(profileView){
                showEmailError(true, "Invalid email")
            }
        }
    }

    override fun saveHobby(hobby: String) {
        if (hobby.isNotBlank()){
            with(profileView){
                val u = Hawk.get("User", User())
                u.hobby = hobby
                Hawk.put("User", u)
            }
        }

    }

    override fun saveDOJ(doj: Long) {
        with(profileView){
            showDOJ(doj)
            val u = Hawk.get("User", User())
            u.doj = doj
            Hawk.put("User", u)
        }
    }

    override fun saveImage(cameraImageUri: String) {
        val u = Hawk.get("User", User())
        u.imageUrl = cameraImageUri
        Hawk.put("User", u)
    }

    init {
        profileView.presenter = this
    }

    override fun start() {
        getUserData()
    }

    private fun getUserData() {
        var user = Hawk.get("User", User())
        with(profileView) {
            setUserDataToView(user)
        }
    }

    private lateinit var user: User
}
