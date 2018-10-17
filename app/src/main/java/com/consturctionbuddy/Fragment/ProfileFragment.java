package com.consturctionbuddy.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Activity.LoginActivity;
import com.consturctionbuddy.Bean.LoginResponce;
import com.consturctionbuddy.Bean.StatusBean;
import com.consturctionbuddy.Bean.UserResponse.User;
import com.consturctionbuddy.Bean.UserResponse.UserInfo;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.AppHelper;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.FilePath;
import com.consturctionbuddy.Utility.ImageUtils;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.Utility.ValidatorUtils;
import com.consturctionbuddy.Utility.VolleyMultipartRequest;
import com.consturctionbuddy.custom.CustomBoldTextView;
import com.consturctionbuddy.custom.CustomEditText;
import com.consturctionbuddy.custom.CustomRegularTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private Context mContext;
    private View mMainView;
    private ImageView img_edit_profile;
    private CustomEditText et_name, et_phone, et_email, et_address;
    private Uri mStrUpdateProfileUrl;
    private String mSelectedFilePath;
    private RelativeLayout mProgressBarLayout;

    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private User loginResponce;
    private Button btn_update;
    private CircleImageView civ_profile;
    private CustomBoldTextView tv_pan;
    private ImageView civ_profile_pan;
    private FloatingActionButton fab_company_name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.activity_edit_profile, container, false);
        init();
        return mMainView;
    }

    private void init() {

        img_edit_profile = mMainView.findViewById(R.id.img_edit_profile);
        et_name = mMainView.findViewById(R.id.et_name);
        et_phone = mMainView.findViewById(R.id.et_phone);
        et_email = mMainView.findViewById(R.id.et_email);
        et_address = mMainView.findViewById(R.id.et_address);
        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);
        btn_update = mMainView.findViewById(R.id.btn_update);
        civ_profile = mMainView.findViewById(R.id.civ_profile);
        tv_pan = mMainView.findViewById(R.id.tv_pan);
        civ_profile_pan = mMainView.findViewById(R.id.civ_profile_pan);
        fab_company_name = mMainView.findViewById(R.id.fab_company_name);
        tv_pan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                requestForUploadPanCardImage();
            }
        });

        fab_company_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setvalidation();
            }
        });


        img_edit_profile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                requestPermissionForGallery();
            }
        });


        loginResponce = UserUtils.getInstance().getUserInfo(mContext);
        if (loginResponce != null) {

            if (loginResponce.getmStrName() != null || loginResponce.getLocal().getEmail() != null || loginResponce.getAddress() != null) {
                et_name.setText(loginResponce.getmStrName());
                et_phone.setText("");
                et_email.setText(loginResponce.getLocal().getEmail());
                et_address.setText(loginResponce.getAddress());
                et_email.setEnabled(false);
            } else {
                et_name.setText("");
                et_phone.setText("");
                et_email.setText("");
                et_address.setText("");
            }

        }
    }

    private void setvalidation() {

        if (ValidatorUtils.NotEmptyValidator(mContext, et_name, true, getString(R.string.RegisterUserNameTxt))
                && ValidatorUtils.NotEmptyValidator(mContext, et_email, true, getString(R.string.RegisterEmptyEmail))
                && ValidatorUtils.EmailValidator(mContext, et_email, true, getString(R.string.RegisterInvalidEmail)) &&
                ValidatorUtils.NotEmptyValidator(mContext, et_phone, true, "Please enter mobile number.")
                && ValidatorUtils.MinimumLengthValidator(mContext, et_phone, 10, true,
                "Please enter 10 digits mobile number.")
                && ValidatorUtils.NotEmptyValidator(mContext, et_address, true, "Please enter address.")) {

            startHttpRequestForUpdateProfile();
        }
    }

    private void startHttpRequestForUpdateProfile() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            mIsRequestInProgress = true;
            String baseUrl = Constant.API_UPDATE_PROFILE;
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
                                                .setMessage(registerResponseObj.getStrMsg())
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
                                            .setMessage("Unable to update Account")
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
                                        .setMessage("Unable to update Account")
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
                    params.put("phone", et_phone.getText().toString());
                    params.put("firstName", et_name.getText().toString());
                    params.put("address", et_address.getText().toString());
                    params.put("userid", UserUtils.getInstance().getUserID(mContext));
                    System.out.println("registration_params = " + params);
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
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
                .setMessage("Your profile is updated successfully.")
                .setCancelable(false)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(R.mipmap.ic_launcher)
                .show();
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestPermissionForGallery() {
        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, Constant.MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Constant.MY_PERMISSIONS_REQUEST_FOR_EXTERNAL_STORAGE) {
            boolean permissionDenied = false;

            for (int i = 0, len = permissions.length; i < len; i++) {
                String permission = permissions[i];
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    permissionDenied = true;
                    boolean showRationale = false;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        showRationale = shouldShowRequestPermissionRationale(permission);
                    }
                    if (!showRationale) {
                        // user also CHECKED "never ask again"
                        // you can either enable some fall back,
                        // disable features of your app
                        // or open another dialog explaining
                        // again the permission and directing to
                        // the app setting
                        showDialogForPermissionSetting();
                    } else if (Manifest.permission.READ_EXTERNAL_STORAGE.equals(permission)) {
                        showDialogForPermission();
//                        showRationale(permission, R.string.permission_denied_contacts);
                        // user did NOT check "never ask again"
                        // this is a good place to explain the user
                        // why you need the permission and ask if he wants
                        // to accept it (the rationale)
                    }
                }
            }
            if (!permissionDenied) {

                selectImagePopUp();
            }
        }

        if (requestCode == Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (grantResults.length == 1 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initCameraPermission();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }


    private void showDialogForPermissionSetting() {
        new android.app.AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.PermissionGoSettingTitle))
                .setMessage(getString(R.string.PermissionGoSettingMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionGoSettingTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        final Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + mContext.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        mContext.startActivity(i);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void showDialogForPermission() {
        new android.app.AlertDialog.Builder(mContext)
                .setTitle(getString(R.string.PermissionDeniedTitle))
                .setMessage(getString(R.string.PermissionDeniedMsg))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.PermissionDeniedSureTxt), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(getString(R.string.PermissionDeniedRetryTxt), new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestPermissionForGallery();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    private void selectImagePopUp() {


        View view = getLayoutInflater().inflate(R.layout.custom_profile_dialog, null);
        final BottomSheetDialog sheetDialog = new BottomSheetDialog(mContext);
        sheetDialog.setContentView(view);

        CustomRegularTextView tv_remove = view.findViewById(R.id.tv_remove);
        CustomRegularTextView tv_camera = view.findViewById(R.id.tv_camera);
        CustomRegularTextView tv_gallery = view.findViewById(R.id.tv_gallery);
        RelativeLayout rl_list_bootom = view.findViewById(R.id.rl_list_bootom);

        rl_list_bootom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();
            }
        });

        tv_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sheetDialog.dismiss();

            }
        });

        tv_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initCameraPermission();
                sheetDialog.dismiss();

            }
        });


        tv_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initGalleryIntent();
                sheetDialog.dismiss();

            }
        });

        sheetDialog.show();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void initCameraPermission() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                Toast.makeText(mContext, "Permission to use Camera", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.CAMERA}, Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } else {
            captureImage();
        }
    }

    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);

    }

    private void initGalleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), Constant.GALLERY_CAPTURE_IMAGE_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    Uri tempUri = getImageUri(mContext, imageBitmap);
                    setImagePath(tempUri);

                } else {
                    System.out.println("error = " + requestCode);
                }

            }
        }


        if (requestCode == Constant.GALLERY_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                mStrUpdateProfileUrl = data.getData();
                if (mStrUpdateProfileUrl != null) {
                    CropImage.activity(data.getData())
                            .start(getActivity());
                }
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri selectedImage = result.getUri();
                if (selectedImage != null) {
                    setImagePath(selectedImage);

                } else {
                    //UIUtils.showToast(mContext, "Something issue for uploading.");
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                //UIUtils.showToast(mContext, "" + error);
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public void setImagePath(Uri tempUri) {
        mStrUpdateProfileUrl = tempUri;
        mSelectedFilePath = FilePath.getPath(mContext, mStrUpdateProfileUrl);
        if (mSelectedFilePath != null && mStrUpdateProfileUrl.getPath() != null && !mStrUpdateProfileUrl.getPath().equals("")) {
            try {
                Bitmap mProfileBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mStrUpdateProfileUrl);
                mProfileBitmap = ImageUtils.getScaledImage(mStrUpdateProfileUrl, mContext);
                if (mProfileBitmap != null) {
                    requestForUploadImage();

                }


            } catch (IOException e) {
                e.printStackTrace();
                // UIUtils.showToast(mContext, getString(R.string.LoadFailedMsg));
            }
        }
    }

    private void requestForUploadImage() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_UPDATE_IMAGE;
            showProgressBar();
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                    baseUrl, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    try {
                        String resultResponse = new String(response.data);
                        Gson gson = new GsonBuilder().create();
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonResp = jsonParser.parse(resultResponse).getAsJsonObject();
                        StatusBean responseBean = gson.fromJson(jsonResp, StatusBean.class);
                        if (responseBean != null && responseBean.getStatus() == 200) {
                            hideProgressBar();

                            setImageBitmap(responseBean.getmImagePath());


                        } else {
                            if (responseBean != null) {
                                hideProgressBar();

                            }
                            civ_profile.setImageResource(R.drawable.ic_user_account);

                        }

                    } catch (Exception e) {
                        hideProgressBar();
                        civ_profile.setImageResource(R.drawable.ic_user_account);

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mIsRequestInProgress) {
                        hideProgressBar();
                        if (error.getClass().equals(NoConnectionError.class)) {
                            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
                        } else {
                            UIUtils.showToast(mContext, getResources().getString(R.string.ErrorMsg));
                        }
                    }
                }
            }) {

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("avatar", new DataPart(mSelectedFilePath.substring(mSelectedFilePath.lastIndexOf("/") + 1),
                            AppHelper.readBytesFromFile(mSelectedFilePath)));
                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_id", UserUtils.getInstance().getUserID(mContext));
                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", "" + UserUtils.getInstance().getUserID(mContext));
                    return params;
                }
            };

            multipartRequest.setShouldCache(false);
            multipartRequest.setTag("");
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(multipartRequest);
        }
    }

    private void setImageBitmap(String aUserProfile) {
        if (aUserProfile != null) {

            Picasso.with(mContext).load(aUserProfile).placeholder(R.drawable.ic_user_account).into(civ_profile);
        } else {

            civ_profile.setImageResource(R.drawable.ic_user_account);
        }

    }

    private void setImageBitmapPanCard(String aUserProfile) {
        if (aUserProfile != null) {

            Picasso.with(mContext).load(aUserProfile).placeholder(R.drawable.placeholder).into(civ_profile_pan);
        } else {

            civ_profile_pan.setImageResource(R.drawable.placeholder);
        }

    }


    private void requestForUploadPanCardImage() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_PAN_CARD_IMAGE;
            showProgressBar();
            VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(Request.Method.POST,
                    baseUrl, new Response.Listener<NetworkResponse>() {
                @Override
                public void onResponse(NetworkResponse response) {
                    try {
                        String resultResponse = new String(response.data);
                        Gson gson = new GsonBuilder().create();
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonResp = jsonParser.parse(resultResponse).getAsJsonObject();
                        StatusBean responseBean = gson.fromJson(jsonResp, StatusBean.class);
                        if (responseBean != null && responseBean.getStatus() == 200) {
                            hideProgressBar();

                            setImageBitmapPanCard(responseBean.getmImagePath());


                        } else {
                            if (responseBean != null) {
                                hideProgressBar();

                            }
                            civ_profile_pan.setImageResource(R.drawable.placeholder);

                        }

                    } catch (Exception e) {
                        hideProgressBar();
                        civ_profile_pan.setImageResource(R.drawable.placeholder);

                    }
                }

            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mIsRequestInProgress) {
                        hideProgressBar();
                        if (error.getClass().equals(NoConnectionError.class)) {
                            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
                        } else {
                            UIUtils.showToast(mContext, getResources().getString(R.string.ErrorMsg));
                        }
                    }
                }
            }) {

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView
                    params.put("avatar", new DataPart(mSelectedFilePath.substring(mSelectedFilePath.lastIndexOf("/") + 1),
                            AppHelper.readBytesFromFile(mSelectedFilePath)));
                    return params;
                }


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("user_id", UserUtils.getInstance().getUserID(mContext));
                    return params;
                }

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", "" + UserUtils.getInstance().getUserID(mContext));
                    return params;
                }
            };

            multipartRequest.setShouldCache(false);
            multipartRequest.setTag("");
            multipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            AppController.getInstance().addToRequestQueue(multipartRequest);
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
}
