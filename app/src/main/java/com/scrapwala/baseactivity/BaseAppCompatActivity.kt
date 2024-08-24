package com.scrapwala.baseactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.moengage.inapp.MoEInAppHelper


open class BaseAppCompatActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }






    override fun onStart() {
        super.onStart()
        MoEInAppHelper.getInstance().showInApp(this)
    }
}