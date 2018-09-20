package com.consturctionbuddy.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.consturctionbuddy.Utility.UserUtils;


public class CustomBoldTextView extends android.support.v7.widget.AppCompatTextView {

    public CustomBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public CustomBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CustomBoldTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context aContext) {
        Typeface myTypeface = UserUtils.getInstance().getTypeFaceForType(aContext, UserUtils.FONT_OPEN_SANS_BOLD);
        setTypeface(myTypeface);
    }
}
