package com.scrapwala.screens.splash.ui

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.scrapwala.databinding.ActiivtySplashBinding
import com.scrapwala.redirectionhandler.navigateToMainActivity
import com.scrapwala.redirectionhandler.navigateToOnboardingActivity
import com.scrapwala.utils.Preferences
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {


    private var redirectUrl: String?=""
    private lateinit var binding: ActiivtySplashBinding
    var jObj: JSONObject?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActiivtySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        listenFcm()

      //  val video = Uri.parse("android.resource://" + packageName + "/" + R.raw.sqyrd);


      //  binding.videoView.setVideoURI(video)




  Handler().postDelayed(Runnable {



      if(Preferences.getUserData(this).isNullOrEmpty()){
          navigateToOnboardingActivity(this,null)

      }
      else{
          navigateToMainActivity(this,null)

      }

//      navigateToMainActivity(this, null, true)

},1000)


        if (intent.action!=null &&intent.data!=null){

            if(intent.dataString.isNullOrEmpty().not()){
                intent.dataString?.let { redirectUrl=it }

            }

        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("onresumeSplash", "triggered")

    }









    private var deeplinkBundle: Bundle? = null
    var deepLink: Uri? = null



}