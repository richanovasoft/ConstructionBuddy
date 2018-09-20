package com.consturctionbuddy.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.ValidatorUtils;
import com.consturctionbuddy.custom.CustomEditText;
import com.consturctionbuddy.custom.CustomRegularTextView;


public class RegistrationActivity extends AppCompatActivity {

    public static final Object TAG = RegistrationActivity.class.getSimpleName();
    private Context mContext;
    private CustomEditText mPasswordEt;
    private CustomEditText mConfirmPasswordEt;
    private CustomEditText mEmailEt;
    private CustomEditText mUserNameEt, mUserAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_registration);

        changeStatusBarColor();
        mContext = this;

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        init();
    }

    private void init() {


        mPasswordEt = findViewById(R.id.et_password);

        mEmailEt = findViewById(R.id.et_email);
        mUserNameEt = findViewById(R.id.et_name);
        CustomRegularTextView tvAlreadyAcccount = findViewById(R.id.ll_already_acc);

        mConfirmPasswordEt = findViewById(R.id.et_con_password);
        mUserAddress = findViewById(R.id.et_address);

        Button joinButton = findViewById(R.id.btn_signup);

        tvAlreadyAcccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToLogin();
            }
        });


        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleJoinClick();

            }
        });

    }

    private void goToLogin() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void handleJoinClick() {
        if (ValidatorUtils.NotEmptyValidator(mContext, mUserNameEt, true, getString(R.string.RegisterUserNameTxt))
                && ValidatorUtils.NotEmptyValidator(mContext, mEmailEt, true, getString(R.string.RegisterEmptyEmail))
                && ValidatorUtils.EmailValidator(mContext, mEmailEt, true, getString(R.string.RegisterInvalidEmail)) &&
                ValidatorUtils.NotEmptyValidator(mContext, mPasswordEt, true, getString(R.string.RegisterEmptyPassword))
                && ValidatorUtils.MinimumLengthValidator(mContext, mPasswordEt, Constant.MIN_PASSWORD_LENGTH, true,
                getString(R.string.RegisterPasswordMinimumLength))
                && ValidatorUtils.NotEmptyValidator(mContext, mConfirmPasswordEt, true, getString(R.string.RegisterEmptyConfirmPassword))
                && ValidatorUtils.EqualStrValidator(mContext, mPasswordEt, mConfirmPasswordEt, true,
                getString(R.string.RegisterPasswordMisMatch))
                && ValidatorUtils.NotEmptyValidator(mContext, mUserAddress, true, "Please enter address.")) {

            registerSuccessfully();
        }
    }


    private void registerSuccessfully() {


        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                .setTitle(getString(R.string.RegisterSuccessfulTitle))
                .setMessage(getString(R.string.RegistrationSuccessfulMsg))
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        gotoHome();
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }

    private void gotoHome() {

        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(mContext, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            window.setStatusBarColor(color);
        }
    }
}

