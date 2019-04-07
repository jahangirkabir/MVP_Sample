package com.jahanbabu.deskerademo.util

import android.app.Application

class DApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }

    companion object {
        var appContext: DApplication? = null
            private set
    }
}
