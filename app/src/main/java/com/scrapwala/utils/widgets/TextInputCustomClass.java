package com.scrapwala.utils.widgets;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.AttributeSet;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.scrapwala.R;


public class TextInputCustomClass extends TextInputEditText {
    public TextInputCustomClass(Context context) {
        super(context);
    }

    public TextInputCustomClass(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextInputCustomClass(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);

        removeError(this);

        this.setBackgroundResource(R.drawable.selectordrawable);


    }








    private void removeError(TextInputEditText editText) {
        try {

            View layout = (View) editText.getParent();
            while (!layout.getClass().getSimpleName().equals(TextInputLayout.class.getSimpleName()))
                layout = (View) layout.getParent();
            TextInputLayout realLayout = (TextInputLayout) layout;

            realLayout.setErrorEnabled(false);
            realLayout.setError(null);
            Integer colorInt = this.getContext().getColor(R.color.secondary_dark);
            ColorStateList color = ColorStateList.valueOf(colorInt);
            realLayout.setDefaultHintTextColor(color);




//            realLayout.setHintTextColor(color);
//            realLayout.settexti
        } catch (ClassCastException | NullPointerException e) {
        }
    }


}
