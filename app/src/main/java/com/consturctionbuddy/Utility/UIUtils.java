package com.consturctionbuddy.Utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;



public class UIUtils {

    public static void showToast(Context aContext, String aMag) {
        Toast.makeText(aContext, aMag, Toast.LENGTH_SHORT).show();
    }


    public static void hideKeyBoard(Activity aActivity) {
        View view = aActivity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) aActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
