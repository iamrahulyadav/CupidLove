package com.example.cupidlove.customview.edittext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by Bhumi Shah on 11/9/2017.
 */

@SuppressLint("AppCompatCustomView")
public class EditTextMedium extends EditText {


    public EditTextMedium(Context context) {
        super(context);
        init();
    }

    public EditTextMedium(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextMedium(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/Lato-Medium.ttf");
        setTypeface(tf, 1);

    }

}
