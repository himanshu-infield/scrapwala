package com.scrapwala.utils.extensionclass

import android.app.Activity
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.scrapwala.R
import com.scrapwala.databinding.LoaderLayoutBinding
import com.scrapwala.utils.ApiError
import com.scrapwala.utils.ApiResult
import com.scrapwala.utils.ApiSuccess
import com.scrapwala.utils.ErrorResponse
import com.scrapwala.utils.FailureException


fun TextInputLayout.setErrorMessage(
    message: String?,
    removepadding: Boolean = true,
    paddingvalueleft: Int = 2,
    paddingvalueTop: Int = 2
) {


    if (message.isNullOrEmpty().not()) {
        this.setError(message)

        val colorInt: Int = context.getColor(R.color.red)
        val csl = ColorStateList.valueOf(colorInt)
        this.defaultHintTextColor = csl


        if (removepadding) {
            try {
                for (i in 0 until this.childCount) {
                    this.editText.let {
//                  it?.sette(resources.getColor(R.color.red))
                        it?.setBackgroundResource(R.drawable.rectangle_red)

                    }



                    if (i == 1) {
                        this.getChildAt(i).setPadding(paddingvalueleft, paddingvalueTop, 0, 0)
                    }

                }
            } catch (excep: Exception) {
                excep.printStackTrace()
            }
        }

    }

    fun hideSoftKeyBoard(context: Context) {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(
            (context as Activity).getWindow().getDecorView().getWindowToken(),
            0
        )
    }


}



fun checkDigit(number: Long): String? {
    return if (number <= 9) "0$number" else number.toString()
}


fun TextInputLayout.removePadding(paddingvalueleft: Int = 2, paddingvalueTop: Int = 2) {

    try {
        for (i in 0 until this.childCount) {
            this.getChildAt(i).setPadding(0, 0, 0, 0)

        }
    } catch (excep: Exception) {
        excep.printStackTrace()
    }


}


suspend fun <T : Any> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> = apply {
    if (this is ApiSuccess<T>) {
        executable(data)
    }
}

suspend fun <T : Any> ApiResult<T>.onError(
    executable: suspend (error: ErrorResponse) -> Unit
): ApiResult<T> = apply {
    if (this is ApiError<T>) {
        executable(error)
    }
}

suspend fun <T : Any> ApiResult<T>.onFailureCallback(
    executable: suspend (e: String) -> Unit
): ApiResult<T> = apply {
    if (this is FailureException<T>) {
        executable(e)
    }
}

fun hideKeyboard(context: Context) {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(
        (context as Activity).getWindow().getDecorView().getWindowToken(),
        0
    )
}

/*fun showProgressDialog(context: Context): AlertDialog {
    val builder = AlertDialog.Builder(context)
    builder.setCancelable(false)

    val inflater = LayoutInflater.from(context)
    val binding = inflater.inflate(R.layout.loader_layout, null)
    builder.setView(binding)

    val dialog = builder.create()

    dialog.window?.apply {
        setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
        )
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    dialog.show()

    return dialog
}*/


fun showProgressDialog(context: Context): AlertDialog {
    val builder = AlertDialog.Builder(context)
    builder.setCancelable(false)

    // Inflate your loader layout
    val inflater = LayoutInflater.from(context)
    val binding = inflater.inflate(R.layout.loader_layout, null)
    builder.setView(binding)

    val dialog = builder.create()

    // Make the dialog fullscreen and non-interactable with outside elements
    dialog.window?.apply {
        // Set a fully transparent background
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // Make the dialog cover the full screen
        setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )

        // Disable touch interaction for the underlying activity
        setFlags(
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
            WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        )
        setFlags(
            WindowManager.LayoutParams.FLAG_DIM_BEHIND,
            WindowManager.LayoutParams.FLAG_DIM_BEHIND
        )
    }

    // Show the dialog
    dialog.show()

    // Block touch interaction on the dialog itself (except cancelable clicks)
    dialog.setOnKeyListener { _, keyCode, _ -> keyCode == KeyEvent.KEYCODE_BACK }

    return dialog
}







fun showCustomToast(
    view: View,
    context: Activity,
    message: String,
    showLong: Boolean = true,
    icon: Int = 0,
    color: String? = ""
) {

    // create an instance of the snackbar
    val snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG)

    // inflate the custom_snackbar_view created previously
    val customSnackView: View =
        context.layoutInflater.inflate(R.layout.custom_snackbar, null)


    if (message.contains("Internet", true)) {

        customSnackView.findViewById<ImageView>(R.id.imageError)
            .setBackgroundResource(R.drawable.ic_no_internet)

        customSnackView.findViewById<CardView>(R.id.cardViewContainer)
            .setCardBackgroundColor(Color.parseColor("#e85f59"))

    } else {
        customSnackView.findViewById<ImageView>(R.id.imageError)
            .setBackgroundResource(R.drawable.ic_error)

        customSnackView.findViewById<CardView>(R.id.cardViewContainer)
            .setCardBackgroundColor(Color.parseColor("#e85f59"))

    }

    if (icon != 0) {
        customSnackView.findViewById<ImageView>(R.id.imageError)
            .setBackgroundResource(icon)
    }


    if (color.isNullOrEmpty().not()) {
        customSnackView.findViewById<CardView>(R.id.cardViewContainer)
            .setCardBackgroundColor(Color.parseColor(color))
    }


    customSnackView.findViewById<TextView>(R.id.toastMessage).setText(message)

    // set the background of the default snackbar as transparent
    snackbar.view.setBackgroundColor(Color.TRANSPARENT)

    // now change the layout of the snackbar
    val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    // set padding of the all corners as 0
    snackbarLayout.setPadding(0, 0, 0, 0)

    snackbarLayout.addView(customSnackView, 0)
    snackbar.show()

}