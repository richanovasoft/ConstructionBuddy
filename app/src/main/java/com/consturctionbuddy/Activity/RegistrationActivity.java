package com.consturctionbuddy.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Bean.UserResponse.UserInfo;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.Utility.ValidatorUtils;
import com.consturctionbuddy.custom.CustomEditText;
import com.consturctionbuddy.custom.CustomRegularTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;


public class RegistrationActivity extends AppCompatActivity {

    public static final Object TAG = RegistrationActivity.class.getSimpleName();
    private Context mContext;
    private CustomEditText mPasswordEt;
    private CustomEditText mConfirmPasswordEt;
    private CustomEditText mEmailEt;
    private CustomEditText mUserNameEt, mUserAddress;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;

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
        mProgressBarLayout = findViewById(R.id.rl_progressBar);

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

            startHttpRequestForSignUp();
        }
    }


    private void startHttpRequestForSignUp() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            mIsRequestInProgress = true;
            final String android_id = Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

            String baseUrl = Constant.API_SIGNUP_METHOD;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            System.out.println("***loginresponce = " + response);

                            try {

                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                UserInfo registerResponseObj = gson.fromJson(jsonResp, UserInfo.class);

                                if (registerResponseObj != null) {
                                    if (registerResponseObj.getUser().getLocal() != null) {
                                        registerSuccessfully();
                                    } else {
                                        hideProgressBar();
                                        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                                .setTitle(getString(R.string.app_name))
                                                .setMessage("Unable to Create Account")
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();

                                                    }
                                                }).show();
                                    }
                                } else {
                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage("Unable to Create Account")
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();

                                                }
                                            }).show();
                                    mIsRequestInProgress = false;
                                }

                            } catch (Exception e) {

                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage("Unable to Create Account")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        }).show();
                            }
                            hideProgressBar();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            hideProgressBar();
                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                            }

                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("email", mEmailEt.getText().toString().trim());
                    params.put("password", mPasswordEt.getText().toString().trim());
                    params.put("firstname", mUserNameEt.getText().toString());
                    params.put("cpassword", mConfirmPasswordEt.getText().toString());
                    params.put("address", mUserAddress.getText().toString());
                    System.out.println("registration_params = " + params);
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag(TAG);
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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

    private void hideProgressBar() {
        mIsRequestInProgress = false;
        mProgressBarLayout.setVisibility(View.GONE);
    }

    private void showProgressBar() {
        mProgressBarLayout.setVisibility(View.VISIBLE);
    }

}

