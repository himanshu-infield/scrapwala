package com.scrapwala.screens.splash

import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONObject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {


    private var redirectUrl: String?=""
    private lateinit var binding: ActivitySplashBinding
    var jObj: JSONObject?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        setContentView(binding.root)

//        listenFcm()

        val video = Uri.parse("android.resource://" + packageName + "/" + R.raw.sqyrd);


        binding.videoView.setVideoURI(video)


        binding.videoView.setZOrderOnTop(true);//this line solve the problem


        binding.videoView.setOnCompletionListener(object : MediaPlayer.OnCompletionListener {
            override fun onCompletion(p0: MediaPlayer?) {

                var bundle: Bundle?=null

                if(redirectUrl.isNullOrEmpty().not()){
                    bundle= Bundle()
                    bundle.putString("redirecyUrl",redirectUrl)



                    navigateToHomeActivity(this@SplashActivity, bundle)
                }
                else{
                    navigateToHomeActivity(this@SplashActivity, null)

                }

                this@SplashActivity.finish()


            }
        })


        if (intent.action!=null &&intent.data!=null){

            if(intent.dataString.isNullOrEmpty().not()){
                intent.dataString?.let { redirectUrl=it }

            }

        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("onresumeSplash", "triggered")
        binding.videoView.start()
    }









    private var deeplinkBundle: Bundle? = null
    var deepLink: Uri? = null



}