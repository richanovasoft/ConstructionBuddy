package com.consturctionbuddy.Utility;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ValidatorUtils {
    public static boolean NotEmptyValidator(Context aContext,
                                            EditText aEditText,
                                            boolean aShowToast,
                                            String aToastMsg) {
        boolean haveText = false;

        String text = aEditText.getText().toString().trim();
        boolean empty = TextUtils.isEmpty(text);
        if (empty) {
            if (aShowToast) {

                aEditText.setError(aToastMsg);
                aEditText.requestFocus();
            }
            haveText = false;
        } else {
            haveText = true;
        }

        return haveText;
    }



    public static boolean NumericValidator(Context aContext,
                                           EditText aEditText,
                                           boolean aShowToast,
                                           String aToastMsg) {
        boolean isNumber = false;
        String text = aEditText.getText().toString().trim();
        if (text.length() > 0) {
            isNumber = text.matches("[0-9]+");
            if (!isNumber && aShowToast) {
                Toast.makeText(aContext, aToastMsg, Toast.LENGTH_SHORT).show();
            }
        } else {
            if (aShowToast) {
                Toast.makeText(aContext, aToastMsg, Toast.LENGTH_SHORT).show();
            }
        }
        return isNumber;
    }

    public static boolean MinLengthValidator(Context aContext,
                                             EditText aEditText,
                                             int aMinLength,
                                             boolean aShowToast,
                                             String aToastMsg) {
        boolean validMinLength = false;
        if (aEditText.getText().toString().trim().length() >= aMinLength) {
            validMinLength = true;
        } else {
            if (aShowToast) {
                aEditText.setError(aToastMsg);
                aEditText.requestFocus();
            }
        }
        return validMinLength;
    }

    public static boolean MaxLengthValidator(Context aContext,
                                             EditText aEditText,
                                             int aMaxLength,
                                             boolean aShowToast,
                                             String aToastMsg) {
        boolean validManLength = false;
        if (aEditText.getText().toString().trim().length() <= aMaxLength) {
            validManLength = true;
        } else {
            if (aShowToast) {
                aEditText.setError(aToastMsg);
                aEditText.requestFocus();
            }
        }
        return validManLength;
    }


    public static boolean EmailValidator(Context aContext,
                                         EditText aEditText,
                                         boolean aShowToast,
                                         String aToastMsg) {
        boolean valid = false;
        String emailStr = aEditText.getText().toString().trim();
        valid = android.util.Patterns.EMAIL_ADDRESS.matcher(emailStr).matches();
        if (!valid) {
            if (aShowToast) {

                aEditText.setError(aToastMsg);
                aEditText.requestFocus();
            }
        }
        return valid;
    }


    public static boolean SpecialCharacterValidator(Context aContext,
                                                    EditText aEditText,
                                                    boolean aShowToast,
                                                    String aToastMsg) {
        boolean valid = false;
        String str = aEditText.getText().toString().trim();
        Pattern p = Pattern.compile("[^A-Za-z0-9]");
        Matcher m = p.matcher(str);
        valid = m.find();
        if (!valid) {
            if (aShowToast) {

                aEditText.setError(aToastMsg);
                aEditText.requestFocus();
            }
        }
        return valid;
    }

    public static boolean MinimumLengthValidator(Context aContext,
                                                 EditText aEditText,
                                                 int aMinLength,
                                                 boolean aShowToast,
                                                 String aToastMsg) {
        boolean valid = false;
        String str = aEditText.getText().toString().trim();
        if (str.length() >= aMinLength) {
            valid = true;
        }
        if (!valid) {
            if (aShowToast) {
                aEditText.setError(aToastMsg);
                aEditText.requestFocus();
            }
        }
        return valid;
    }


    public static boolean UrlPhoneValidator(Context aContext,
                                            EditText aEditText,
                                            int aMinLength,
                                            int aMaxLength,
                                            boolean aShowToast,
                                            String aToastMsg) {
        if (aMinLength == 0) {
            aMinLength = 6;
        }
        if (aMaxLength == 0) {
            aMaxLength = 12;
        }
        boolean valid = false;
        String str = aEditText.getText().toString().trim();

        if (!Pattern.matches("[a-zA-Z]+", str)) {
            if (str.length() >= aMinLength && str.length() <= aMaxLength) {
                valid = true;
            } else {
                valid = false;
            }
        } else {
            valid = false;
        }
        if (!valid) {
            if (aShowToast) {

                aEditText.setError(aToastMsg);
                aEditText.requestFocus();
            }
        }
        return valid;
    }

    public static boolean EqualStrValidator(Context aContext,
                                            EditText aFirstEditText,
                                            EditText aSecondEditText,
                                            boolean aShowToast,
                                            String aToastMsg) {
        boolean equal = false;
        String firstText = aFirstEditText.getText().toString().trim();
        String secondText = aSecondEditText.getText().toString().trim();
        if (firstText.equals(secondText)) {
            equal = true;
        } else {
            if (aShowToast) {
                aSecondEditText.setError(aToastMsg);
                aSecondEditText.requestFocus();
            }
        }
        return equal;
    }

}
