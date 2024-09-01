package com.scrapwala.utils

import android.content.Context

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









}