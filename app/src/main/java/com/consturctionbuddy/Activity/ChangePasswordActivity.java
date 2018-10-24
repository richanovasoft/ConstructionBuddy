package com.consturctionbuddy.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Bean.StatusBean;
import com.consturctionbuddy.Bean.UserResponse.User;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.Utility.ValidatorUtils;
import com.consturctionbuddy.custom.CustomEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class ChangePasswordActivity extends AppCompatActivity {


    private static final Object TAG = LoginActivity.class.getSimpleName();
    private Context mContext;
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;

    private CustomEditText mEtLoginNewPassword;
    private CustomEditText mEtLoginPassword;
    private RelativeLayout mProgressBarLayout;
    private User userUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ChangePasswordActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = this;
        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Change Password");
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


        mProgressBarLayout = findViewById(R.id.rl_progressBar);
        RelativeLayout mLLLogin = findViewById(R.id.ll_submit);
        mEtLoginNewPassword = findViewById(R.id.et_change_pass);
        mEtLoginPassword = findViewById(R.id.et_current_pass);

        userUtils = UserUtils.getInstance().getUserInfo(mContext);
      /*   if (userUtils != null) {
            mEtLoginEmail.setText(userUtils.getEmail());
            System.out.println("userUtils.getUserid() = " + userUtils.getUserId());
        }*/

        mLLLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setValidation();
            }
        });


    }

    private void setValidation() {
        UIUtils.hideKeyBoard(this);
        if (ValidatorUtils.NotEmptyValidator(mContext, mEtLoginPassword, true, getString(R.string.PasswordOld))
                && ValidatorUtils.NotEmptyValidator(mContext, mEtLoginNewPassword, true, getString(R.string.PasswordNew))
                && ValidatorUtils.MinimumLengthValidator(mContext, mEtLoginNewPassword, Constant.MIN_PASSWORD_LENGTH, true,
                getString(R.string.RegisterPasswordMinimumLength))) {

            startHttpRequestForResetPassword();

        }
    }


    private void startHttpRequestForResetPassword() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_CHANGE_PASSWORD;

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
                                    showChangePasswordPopUp(forgetPasswordBean);
                                } else {
                                    hideProgressBar();
                                    /*new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle("Alert")
                                            .setMessage(forgetPasswordBean.getMessage())
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.dismiss();
                                                }
                                            }).show();
*/
                                }
                            } catch (Exception e) {
                                hideProgressBar();
                                /*new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle("Alert")
                                        .setMessage("Unable to change password")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();*/

                            }

                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error instanceof TimeoutError) {
                                /*new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle("Alert")
                                        .setMessage("Unable to change password")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();*/

                            } else if (error instanceof ServerError) {
                                /*new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle("Alert")
                                        .setMessage("Unable to change password")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
*/
                            } else if (error instanceof AuthFailureError) {

                                /*new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle("Alert")
                                        .setMessage("Unable to change password")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();*/

                            } else if (error instanceof NetworkError) {
                                /*new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle("Alert")
                                        .setMessage("Unable to change password")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        }).show();
*/
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", UserUtils.getInstance().getUserID(mContext));
                    params.put("npassword", mEtLoginPassword.getText().toString());
                    params.put("cpassword", mEtLoginNewPassword.getText().toString());
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
            //UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
        }


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


    private void showChangePasswordPopUp(StatusBean aLoginResponseObj) {
        if (aLoginResponseObj != null) {

            new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                    .setTitle(getString(R.string.RegisterSuccessfulTitle))
                    .setMessage(aLoginResponseObj.getMessage())
                    .setCancelable(false)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(mContext, NavigationActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    })
                    .setIcon(R.mipmap.ic_launcher)
                    .show();

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
