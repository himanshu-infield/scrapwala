package com.scrapwala.utils

import android.content.Context
import com.google.gson.Gson
import com.scrapwala.screens.login.model.VerifyOtpResponse

object Preferences {



    fun setUserData(context: Context, userData: String) {
        var  settings = context.getSharedPreferences(Constant.PREFERENCE_NAME, 0)
        var  editor = settings?.edit()
        editor?.putString("userData", userData)
        editor?.apply()
    }



    fun getUserData(context: Context): String {
        var settings = context.getSharedPreferences(Constant.PREFERENCE_NAME, 0)
        return settings?.getString("userData", "") ?: ""
    }



    fun getUserDataObj(context: Context): VerifyOtpResponse.Data? {
        var settings = context.getSharedPreferences(Constant.PREFERENCE_NAME, 0)
       var data= settings?.getString("userData", "") ?: ""


        if(data.isNullOrEmpty().not()){
            var userDaataObj= Gson().fromJson(data,VerifyOtpResponse.Data::class.java)


            if(userDaataObj!=null){
                return userDaataObj
            }
        }

        return null
    }




    fun setUserToken(context: Context, token: String) {
        var  settings = context.getSharedPreferences(Constant.PREFERENCE_NAME, 0)
        var  editor = settings?.edit()
        editor?.putString("userToken", token)
        editor?.apply()
    }



    fun getUserToken(context: Context): String {
        var settings = context.getSharedPreferences(Constant.PREFERENCE_NAME, 0)
        return settings?.getString("userToken", "") ?: ""
    }







}