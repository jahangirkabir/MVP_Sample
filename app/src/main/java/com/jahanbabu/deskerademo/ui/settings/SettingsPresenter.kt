package com.jahanbabu.deskerademo.ui.settings

import com.jahanbabu.deskerademo.data.User
import com.orhanobut.hawk.Hawk

/**
 * Listens to user actions from the UI ([SettingsFragment]), retrieves the data and updates
 * the UI as required.
 */
class SettingsPresenter(val settingsView: SettingsContract.View) : SettingsContract.Presenter {

    override fun saveTempUnit(unit: String) {
        val u = Hawk.get("User", User())
        u.tempUnit = unit
        Hawk.put("User", u)

        with(settingsView){
            showTempUnit(unit)
        }
    }

    override fun saveProbationDate(dop: Long) {
        val u = Hawk.get("User", User())
        u.dop = dop
        Hawk.put("User", u)

        with(settingsView){
            showDOP(dop)
        }
    }

    override fun saveSound(isChecked: Boolean) {
        val u = Hawk.get("User", User())
        u.sound = isChecked
        Hawk.put("User", u)
    }

    override fun saveNotification(isChecked: Boolean) {
        val u = Hawk.get("User", User())
        u.notification = isChecked
        Hawk.put("User", u)
    }

    override fun goToProfilesDetails() {

    }


    init {
        settingsView.presenter = this
    }

    override fun start() {
        getUserData()
    }

    private fun getUserData() {
        val u = Hawk.get("User", User())

        with(settingsView){
            setDataToView(u)
        }
    }
}
