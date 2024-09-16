package com.scrapwala.redirectionhandler

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.lifecycle.LifecycleOwner
import com.scrapwala.MainActivity
import com.scrapwala.screens.login.ui.LoginActivity
import com.scrapwala.screens.onboarding.ui.OnboardingActivity
import com.scrapwala.screens.pickups.AddAddressActivity
import com.scrapwala.screens.pickups.PickupsActivity
import com.scrapwala.screens.pickups.category.ui.CategoryActivity
import com.scrapwala.screens.profile.EditProfileActivity

fun navigateToOnboardingActivity(context: Activity, bundle: Bundle?) {
    var intent = Intent(context, OnboardingActivity::class.java)
    bundle?.let {
        intent.putExtras(it)
    }

    context.startActivity(intent)

}




fun navigateToLoginActivity(context: Activity, bundle: Bundle?) {
    var intent = Intent(context, LoginActivity::class.java)
    bundle?.let {
        intent.putExtras(it)
    }

    context.startActivity(intent)

}




fun navigateToAddAdddress(context: Activity, bundle: Bundle?) {
    var intent = Intent(context, AddAddressActivity::class.java)
    bundle?.let {
        intent.putExtras(it)
    }

    context.startActivity(intent)

}


fun navigateToMainActivity(context: Activity, bundle: Bundle?, needFlag: Boolean = false,) {

    val intent = Intent(context, MainActivity::class.java)

    bundle?.let {
        intent.putExtras(it)
    }

    if (needFlag) {
        ActivityCompat.finishAffinity(context)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(intent)

    }

//    else if (bringActivityFront) {
//        val application = (context.application as? SquareyardsApplication)
//        application?.let {
//            // Remove observers only if application is an instance of SquareyardsApplication
//            val lifecycleOwner = context as? LifecycleOwner
//            lifecycleOwner?.let { owner ->
//                it.refreshMainActivity.removeObservers(owner)
//                it.refreshNotificationCount.removeObservers(owner)
//                it.refreshHome.removeObservers(owner)
//                it.changeToHome.removeObservers(owner)
//                it.refreshShortlistCount.removeObservers(owner)
//                it.changeBottomNav.removeObservers(owner)
//            }
//        }
//        ActivityCompat.finishAffinity(context)
//        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
//        context.startActivity(intent)
//
//    }
//    else{
//        context.startActivity(intent)
//
//    }



}



fun navigateToPickupsActivity(context: Activity, bundle: Bundle?, needFlag: Boolean? = false,) {
    val intent = Intent(context, PickupsActivity::class.java)
    if (needFlag!!) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    bundle?.let {
        intent.putExtras(it)
    }
    context.startActivity(intent)

}

fun navigateToEditProfileActivity(context: Activity, bundle: Bundle?, needFlag: Boolean? = false,) {
    val intent = Intent(context, EditProfileActivity::class.java)
    if (needFlag!!) {
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    }
    bundle?.let {
        intent.putExtras(it)
    }
    context.startActivity(intent)

}




fun navigateToCategoryActivity(context: Activity, bundle: Bundle?) {
    val intent = Intent(context, CategoryActivity::class.java)

    bundle?.let {
        intent.putExtras(it)
    }
    context.startActivity(intent)

}
