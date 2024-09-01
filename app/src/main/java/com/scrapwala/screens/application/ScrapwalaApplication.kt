package com.scrapwala.screens.application

import android.app.Application
import android.app.Dialog
import com.google.firebase.analytics.FirebaseAnalytics
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ScrapwalaApplication : Application() {
    public lateinit var firebaseAnalytics: FirebaseAnalytics

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    var globalDialog: Dialog? = null



    companion object {
        lateinit var mInstance: ScrapwalaApplication
            private set
    }

    override fun onCreate() {
        super.onCreate()

        mInstance = this

        firebaseAnalytics = FirebaseAnalytics.getInstance(this)



    }


}