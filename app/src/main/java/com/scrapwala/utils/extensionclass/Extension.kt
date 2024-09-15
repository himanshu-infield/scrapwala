package com.scrapwala.utils.extensionclass

import android.content.res.ColorStateList
import com.google.android.material.textfield.TextInputLayout
import com.scrapwala.R


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