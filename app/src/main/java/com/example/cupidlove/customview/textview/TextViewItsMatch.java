package com.example.cupidlove.customview.textview;

/**
 * Created by Kaushal on 27-12-2017.
 */

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Bhumi Shah on 11/9/2017.
 */

@SuppressLint("AppCompatCustomView")
public class TextViewItsMatch extends TextView {

    public TextViewItsMatch(Context context) {
        super(context);
        init();
    }

    public TextViewItsMatch(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewItsMatch(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "font/GREATVIBES-REGULAR.TTF");
        setTypeface(tf, 1);
    }
}
