package com.consturctionbuddy.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.StorageUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    private Context mContext;
    public static final int RequestPermissionCode = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        changeStatusBarColor();
        findHashKey();
        mContext = this;
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                handleSplashTimeout();
            }
        }, Constant.SPLASH_TIME_OUT);
    }

    private void handleSplashTimeout() {


        if (CheckingPermissionIsEnabledOrNot()) {
            gotoMain();

        } else {
            RequestMultiplePermission();
        }
    }

    private void gotoMain() {

        boolean loggedIn = StorageUtils.getPrefForBool(mContext, Constant.PREF_USER_LOGGED_IN);
        Intent intent;
        if (loggedIn) {
            intent = new Intent(SplashActivity.this, NavigationActivity.class);
        } else {
            intent = new Intent(SplashActivity.this, LoginActivity.class);
        }
        startActivity(intent);
        finish();
    }

    public boolean CheckingPermissionIsEnabledOrNot() {

        int ForthPermissionResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
        int FifthPermissionResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int sixthPermissionResult = ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA);

        return ForthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                FifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                sixthPermissionResult == PackageManager.PERMISSION_GRANTED;
    }


    private void RequestMultiplePermission() {

        // Creating String Array with Permissions.
        ActivityCompat.requestPermissions(SplashActivity.this, new String[]{
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA,
        }, RequestPermissionCode);

    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorPrimary);
            window.setStatusBarColor(color);
        }
    }

    // Calling override method.
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case RequestPermissionCode:

                if (grantResults.length > 0) {

                    boolean GetAccountsPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean GetLocation = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean GetLocation1 = grantResults[2] == PackageManager.PERMISSION_GRANTED;

                    if (GetAccountsPermission && GetLocation && GetLocation1) {

                        gotoMain();
                    } else {
                        finish();
                    }
                }

                break;
        }
    }

    public void findHashKey() {

        try {
            @SuppressLint("PackageManagerGetSignatures") PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            System.out.println("e = " + e);
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            System.out.println("e = " + e);
            e.printStackTrace();
        }
    }
}
