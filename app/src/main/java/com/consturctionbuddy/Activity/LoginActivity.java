package com.consturctionbuddy.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
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
import com.consturctionbuddy.Bean.UserResponse.User;
import com.consturctionbuddy.Bean.UserResponse.UserInfo;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.custom.CustomBoldTextView;
import com.consturctionbuddy.custom.CustomEditText;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private CustomEditText mEmailView;
    private CustomEditText mPasswordView;
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

    private static final String EMAIL = "email";


    private CallbackManager mFbCallbackManager;
    private LoginButton mFbLoginButton;

    private GoogleApiClient mGoogleApiClient;
    private SignInButton mSignInButton;

    private LinearLayout mLLFbLogin;
    private LinearLayout mGPlusLoginLl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        LoginActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        changeStatusBarColor();
        mContext = this;

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);


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
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);

            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.btn_login);
        CustomBoldTextView btn_signup = findViewById(R.id.btn_signup);


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

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mLLFbLogin = findViewById(R.id.ll_fb_login);
        mFbLoginButton = findViewById(R.id.login_button);
        mGPlusLoginLl = findViewById(R.id.ll_gplus_login);
        mSignInButton = findViewById(R.id.sign_in_button);


        mLLFbLogin.setFocusable(true);
        mLLFbLogin.setFocusableInTouchMode(true);
        mLLFbLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean internetAvailable = Utils.isConnectingToInternet(mContext);

                if (internetAvailable) {


                    InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.showSoftInput(mLLFbLogin, InputMethodManager.SHOW_FORCED);

                    initFbLogin();
                    mFbLoginButton.performClick();

                } else {
                    UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));
                }
            }
        });


        mGPlusLoginLl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean internetAvailable = Utils.isConnectingToInternet(mContext);

                if (internetAvailable) {
                    initGPlusLogin();
                    mSignInButton.performClick();
                } else {
                    UIUtils.showToast(mContext, getString(R.string.InternetErrorMsg));

                }

            }
        });

    }


    private void initFbLogin() {

        mFbCallbackManager = CallbackManager.Factory.create();
        mFbLoginButton.setReadPermissions(Constant.FACEBOOK_PERMISSION_STR);
        mFbLoginButton.registerCallback(mFbCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                String accessToken = loginResult.getAccessToken().getToken();
                System.out.println("accessToken = " + accessToken);
                setFaceBookdata(loginResult, accessToken);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException exception) {

            }
        });
    }

    private void setFaceBookdata(LoginResult loginResult, final String accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                loginResult.getAccessToken(),
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {


                            // String profilePicUrl = object.getJSONObject("picture").getJSONObject("data").getString("url");
                            //  System.out.println("profilePicUrl = " + profilePicUrl);


                            String userID = object.getString("id");


                            String img_value = "https://graph.facebook.com/" + userID + "/picture?type=large";

                            //UserUtils.getInstance().setFbUserProfile(mContext, img_value);

                            getFacebookData(response, accessToken);

                        } catch (Exception e) {

                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", Constant.FB_FIELDS_STR);
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void getFacebookData(GraphResponse object, String accessToken) {
        try {


            String myPicture = object.getJSONObject().get("picture").toString();
            //SocialLoginResponseBean socialLoginResponseBean = new SocialLoginResponseBean(myPicture);

            // UserUtils.getInstance().saveUserInfo(mContext, socialLoginResponseBean);
            //httpRequestForSocialLogin(accessToken);
            loginSuccessfully1();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void initGPlusLogin() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, Constant.GP_SIGN_IN);
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

            // loginSuccessfully1();
            startHttpRequestForLoginAPI();

        }
    }


    private void startHttpRequestForLoginAPI() {

        boolean internetAvailable = Utils.isConnectingToInternet(LoginActivity.this);

        if (internetAvailable) {
            mIsRequestInProgress = true;

            android_id = Settings.System.getString(mContext.getContentResolver(), Settings.Secure.ANDROID_ID);
            showProgressBar();

            String baseUrl = Constant.API_LOGIN;
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                UserInfo loginResponseData = gson.fromJson(jsonResp, UserInfo.class);
                                if (loginResponseData != null && loginResponseData.getStatus() == 200) {
                                    hideProgressBar();

                                    loginSuccessfully(loginResponseData.getUser());

                                } else {

                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage(loginResponseData.getStrMsg())
                                            .setCancelable(false)
                                            .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Whatever...
                                                    dialog.dismiss();
                                                }
                                            }).show();
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
                    //params.put("deviceId", android_id);
                    //params.put("firebaseRegistrationId", refreshedToken);
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


    private void loginSuccessfully(User aLoginResponseObj) {


        UserUtils.getInstance().setUserLoggedIn(mContext, true);
        UserUtils.getInstance().saveUserInfo(mContext, aLoginResponseObj);
        UserUtils.getInstance().setUserId(mContext, aLoginResponseObj.get_id());
        UserUtils.getInstance().setUserProfileImage(mContext, aLoginResponseObj.getImg().getPath());
        Intent intent = new Intent(mContext, NavigationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void loginSuccessfully1() {


        UserUtils.getInstance().setUserLoggedIn(mContext, true);
        Intent intent = new Intent(mContext, NavigationActivity.class);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Facebook login
        if (requestCode == Constant.GP_SIGN_IN) {

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        } else {
            mFbCallbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {

        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            String idToken = account.getIdToken();
            // httpRequestForSocialLoginForGMail(idToken);

            loginSuccessfully1();


        } catch (ApiException e) {
            Log.w("", "handleSignInResult:error", e);

        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, Constant.REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }
    }
}

