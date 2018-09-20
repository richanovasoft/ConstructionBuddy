package com.consturctionbuddy.custom;


import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.consturctionbuddy.Utility.UserUtils;


public class CustomEditText extends android.support.v7.widget.AppCompatEditText {
    public CustomEditText(Context context) {
        super(context);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context aContext) {
        Typeface myTypeface = UserUtils.getInstance().getTypeFaceForType(aContext, UserUtils.FONT_OPEN_SANS_REGULAR);
        setTypeface(myTypeface);
    }
}
