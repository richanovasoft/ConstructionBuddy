package com.consturctionbuddy.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Bean.LoginResponce;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {


    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView mPasswordView;
    private Context mContext;
    private LinearLayout ll_forgot;


    private static final Object TAG = LoginActivity.class.getSimpleName();
    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;

    private int BackCount = Constant.APPLICATION_BACK_COUNT;

    private String android_id, refreshedToken;

    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public FirebaseAuth firebaseAuth;

    DatabaseReference databaseReference;
    String Database_Path = "All_UserName_Database";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        LoginActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        changeStatusBarColor();
        mContext = this;


        mEmailView = findViewById(R.id.email);
        ll_forgot = findViewById(R.id.ll_forgot);

        mPasswordView = findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path);

        firebaseAuth = FirebaseAuth.getInstance();


        mProgressBarLayout = findViewById(R.id.rl_progressBar);
        ll_forgot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                //startActivity(intent);

            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_login);
        Button btn_signup = (Button) findViewById(R.id.btn_signup);


        btn_signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(mContext, RegistrationActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Constant.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    displayFirebaseRegId();

                } else if (intent.getAction().equals(Constant.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra(Constant.INTENT_NOTIFICATION_MSG);
                    String id = intent.getStringExtra(Constant.INTENT_NOTIFICATION_ID);
                    String imageUrl = intent.getStringExtra(Constant.INTENT_NOTIFICATION_IMAGE_URL);
                    String title = intent.getStringExtra(Constant.INTENT_NOTIFICATION_TITLE);
                    //AppData.getInstance().showDialog(SplashActivity.this, title, message, imageUrl, type, id);
                }
            }
        };

        displayFirebaseRegId();

    }


    private void displayFirebaseRegId() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constant.SHARED_PREF_FIREBASE_KEY, 0);
        String refreshedToken1 = pref.getString("regId", null);

        if (!TextUtils.isEmpty(refreshedToken1) || refreshedToken1 != null) {

            refreshedToken = refreshedToken1;
            System.out.println("????regId = " + refreshedToken);


        } else {
            refreshedToken = FirebaseInstanceId.getInstance().getToken();
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("regId", refreshedToken);
            editor.commit();
        }
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            window.setStatusBarColor(color);
        }
    }

    private void attemptLogin() {


        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            //Call API for Login

            loginSuccessfully1();
            //startHttpRequestForLoginAPI();

        }
    }


    private void startHttpRequestForLoginAPI() {

        boolean internetAvailable = Utils.isConnectingToInternet(LoginActivity.this);

        if (internetAvailable) {
            mIsRequestInProgress = true;

            android_id = Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);

            String baseUrl = Constant.API_LOGIN;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject root = new JSONObject(response);
                                String strStatus = root.optString("status");
                                String strMsg = root.optString("message");
                                if (strStatus.equals("Failed")) {

                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage(strMsg)
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Whatever...
                                                    dialog.dismiss();
                                                }
                                            }).show();


                                } else {
                                    Gson gson = new GsonBuilder().create();
                                    JsonParser jsonParser = new JsonParser();
                                    JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                    LoginResponce loginResponseData = gson.fromJson(jsonResp, LoginResponce.class);
                                    if (loginResponseData != null) {
                                        hideProgressBar();

                                        loginSuccessfully(loginResponseData);

                                    } else {

                                        hideProgressBar();
                                        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                                .setTitle(getString(R.string.app_name))
                                                .setMessage(getString(R.string.LoginFailedMsg))
                                                .setCancelable(false)
                                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Whatever...
                                                        dialog.dismiss();
                                                    }
                                                }).show();
                                    }
                                }

                            } catch (Exception e) {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage(getString(R.string.LoginFailedMsg))
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
                            if (error instanceof NoConnectionError) {
                                UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                            } else {
                                hideProgressBar();
                                new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                        .setTitle(getString(R.string.app_name))
                                        .setMessage("Account is not activated, Please verify your email and Sign In. ")
                                        .setCancelable(false)
                                        .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Whatever...
                                                dialog.dismiss();
                                            }
                                        }).show();
                            }

                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put(Constant.LOGIN_USERNAME_KEY, mEmailView.getText().toString());
                    params.put(Constant.LOGIN_PASSWORD_KEY, mPasswordView.getText().toString().trim());
                    params.put("deviceId", android_id);
                    params.put("firebaseRegistrationId", refreshedToken);
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


    private void loginSuccessfully(LoginResponce aLoginResponseObj) {


        UserUtils.getInstance().setUserLoggedIn(mContext, true);
        UserUtils.getInstance().saveUserInfo(mContext, aLoginResponseObj);
        UserUtils.getInstance().setUserId(mContext, aLoginResponseObj.getUserId());
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loginSuccessfully1() {


        UserUtils.getInstance().setUserLoggedIn(mContext, true);
        Intent intent = new Intent(mContext, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 2;
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

    @Override
    public void onBackPressed() {
        showExitAlertDialog();
    }

    //Userd for Exit application.
    private void showExitAlertDialog() {
        if (BackCount == Constant.APPLICATION_BACK_EXIT_COUNT) {
            BackCount = Constant.APPLICATION_BACK_COUNT;
            finish();
        } else {
            Toast.makeText(getApplicationContext(), getString(R.string.backCountMsg), Toast.LENGTH_SHORT).show();
            BackCount++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayFirebaseRegId();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.REGISTRATION_COMPLETE));
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Constant.PUSH_NOTIFICATION));

    }


    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }

}

