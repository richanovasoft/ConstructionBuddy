package com.consturctionbuddy.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Bean.StatusBean;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.Utility.ValidatorUtils;
import com.consturctionbuddy.custom.CustomEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;


public class ForgotPasswordActivity extends AppCompatActivity {

    private static final Object TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;
    private CustomEditText mEtEmail;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ForgotPasswordActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = this;
        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        mProgressBarLayout = findViewById(R.id.rl_progressBar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Forgot Password");
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        init();
    }

    private void init() {


        RelativeLayout mLLLogin = findViewById(R.id.ll_submit);
        mEtEmail = findViewById(R.id.email);

        mLLLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValidation();
            }
        });


    }


    private void setValidation() {
        UIUtils.hideKeyBoard(this);
        if (ValidatorUtils.NotEmptyValidator(mContext, mEtEmail, true, getString(R.string.LoginUserNameWithEmailTxt))
                && ValidatorUtils.EmailValidator(mContext, mEtEmail, true, getString(R.string.RegisterInvalidEmail))) {

            startHttpRequestForChangeEmail();

        }
    }


    private void startHttpRequestForChangeEmail() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_FORGOT_PASSWORD;

            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            try {
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                StatusBean forgetPasswordBean = gson.fromJson(jsonResp, StatusBean.class);
                                if (forgetPasswordBean != null && forgetPasswordBean.getStatus() == 200) {

                                    hideProgressBar();
                                    showForgotPasswordPopUp(forgetPasswordBean);
                                } else {
                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage(forgetPasswordBean.getMessage())
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();

                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage("Unable to reset password")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();

                            }

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof NetworkError) {

                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Constant.LOGIN_USERNAME_KEY, mEtEmail.getText().toString());
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
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

        } else {
            UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }
    }

    private void showForgotPasswordPopUp(StatusBean responseBean) {


        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                .setTitle("Alert")
                .setMessage("An email has been sent on this email address for password resetting.")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    }
                }).show();

    }

    private void hideProgressBar() {
        mIsRequestInProgress = false;
        if (mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.GONE);
            mProgressBarShowing = false;
        }
    }

    private void showProgressBar() {
        if (!mProgressBarShowing) {
            mProgressBarLayout.setVisibility(View.VISIBLE);
            mProgressBarShowing = true;
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
            window.setStatusBarColor(color);
        }
    }
}
