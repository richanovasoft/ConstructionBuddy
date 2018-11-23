package com.consturctionbuddy.Fragment;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Activity.NavigationActivity;
import com.consturctionbuddy.Adapter.MultipleImagesNewAdapter;
import com.consturctionbuddy.Adapter.SpinnerAdapter;
import com.consturctionbuddy.Bean.DailyWork.DailyWorkBean;
import com.consturctionbuddy.Bean.ProjectList.ProjectListBean;
import com.consturctionbuddy.Bean.ProjectList.Projectlist;
import com.consturctionbuddy.Bean.Projects.StateInfo;
import com.consturctionbuddy.Bean.Projects.StateResponse;
import com.consturctionbuddy.Interface.IMultipleImageNewClickCallback;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.AppHelper;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.DateUtils;
import com.consturctionbuddy.Utility.FilePath;
import com.consturctionbuddy.Utility.ImageUtils;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.Utility.ValidatorUtils;
import com.consturctionbuddy.Utility.VolleyMultipartRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class DailyWorkInformationFragment extends Fragment implements IMultipleImageNewClickCallback {

    private Context mContext;
    private View mMainView;
    private Spinner mSpinnerServices;
    private AutoCompleteTextView et_prefer_date, et_prefer_time, et_problem_description;
    private Calendar mSelectedDOBCalendar;

    private Button btn_submit, btn_cancel, btnUpload;

    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;
    private RecyclerView rv_post_multipleImg;
    private ArrayList<String> mStringsImgList;
    private Projectlist mSelectedStateBean;
    private ArrayList<Projectlist> mStateData;
    private String mProjectId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_daily_work_information_form, container, false);
        init();
        return mMainView;
    }

    private void init() {

        ((NavigationActivity) mContext).setTitle("Daily work information");

        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);

        et_prefer_date = mMainView.findViewById(R.id.et_prefer_date);
        et_prefer_time = mMainView.findViewById(R.id.et_working_subject);
        et_problem_description = mMainView.findViewById(R.id.et_problem_description);
        btn_submit = mMainView.findViewById(R.id.btn_submit);
        btn_cancel = mMainView.findViewById(R.id.btn_cancel);
        mStringsImgList = new ArrayList<>();

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setValidation();
            }
        });

        mSpinnerServices = mMainView.findViewById(R.id.sp_services);
        btnUpload = mMainView.findViewById(R.id.btn_upload);
        rv_post_multipleImg = mMainView.findViewById(R.id.rv_post_multipleImg);

        mStringsImgList = new ArrayList<>();

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                captureImage();
            }
        });


        mSelectedDOBCalendar = Calendar.getInstance();
        mSelectedDOBCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mSelectedDOBCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        mSelectedDOBCalendar.set(Calendar.YEAR, 1990);
        String dateStr = DateUtils.getDateStr(mSelectedDOBCalendar, DateUtils.DATE_WITHOUT_TIME_SERVER_FORMAT);
        et_prefer_date.setText(dateStr);


        startHttpRequestForProjectList();

        mSpinnerServices.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);


        et_prefer_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();

            }
        });

        et_prefer_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker();
                    et_prefer_date.clearFocus();
                }
            }
        });


    }


    private void startHttpRequestForProjectList() {
        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            mIsRequestInProgress = true;
            String baseUrl = Constant.API_PROJECTS_LIST;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {


                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                ProjectListBean projectListBean = gson.fromJson(jsonResp, ProjectListBean.class);

                                if (projectListBean.getProjectlist() != null && projectListBean.getProjectlist().size() > 0) {

                                    hideProgressBar();
                                    setProjectList(projectListBean);


                                } else {

                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage("Unable to add daily work information.")
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
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (mIsRequestInProgress) {
                                hideProgressBar();
                                if (error.getClass().equals(NoConnectionError.class)) {
                                    mSpinnerServices.setVisibility(View.GONE);
                                } else {
                                    UIUtils.showToast(mContext, getString(R.string.VolleyErrorMsg));
                                }
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("userid", UserUtils.getInstance().getUserID(mContext));
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
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            mSpinnerServices.setVisibility(View.GONE);
        }
    }

    private void setProjectList(final ProjectListBean stateList) {

        mStateData = stateList.getProjectlist();

        SpinnerAdapter adapter = new SpinnerAdapter(mContext, R.layout.custom_spinner, stateList.getStateList());
        mSpinnerServices.setAdapter(adapter);
        mSpinnerServices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                mSelectedStateBean = mStateData.get(position);
                mProjectId = mSelectedStateBean.get_id();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {

            }
        });

    }


    private void captureImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            startActivityForResult(intent, Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }


    private void setValidation() {

        UIUtils.hideKeyBoard(getActivity());

        if (mSpinnerServices != null && mSpinnerServices.getSelectedItem() != null) {


        } else {

            UIUtils.showToast(mContext, "Please select project name.");
        }

        if (ValidatorUtils.NotEmptyValidator(mContext, et_prefer_time, true, "Please enter subject.")
                && ValidatorUtils.NotEmptyValidator(mContext, et_problem_description, true, "Please enter description..")
                && ValidatorUtils.NotEmptyValidator(mContext, et_prefer_date, true, "Please select date.")) {

            startHttpRequestForOngoingProject();
        }

    }


    private void startHttpRequestForOngoingProject() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {

            String baseUrl = Constant.API_DAILY_WORK;
            showProgressBar();
            VolleyMultipartRequest mStrRequest = new VolleyMultipartRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {

                                String resultResponse = new String(response.data);
                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(resultResponse).getAsJsonObject();
                                DailyWorkBean uploadProfileResponse = gson.fromJson(jsonResp, DailyWorkBean.class);

                                if (uploadProfileResponse.getStatus() != null && uploadProfileResponse.getStatus().equals("True")) {

                                    hideProgressBar();
                                    addServiceList();


                                } else {

                                    hideProgressBar();
                                    new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                            .setTitle(getString(R.string.app_name))
                                            .setMessage("Unable to add daily work information.")
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
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();
                            if (error.getClass().equals(NoConnectionError.class)) {
                                UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));

                            } else {
                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    // file name could found file base or direct access from real path
                    // for now just get bitmap data from ImageView

                    for (int i = 0; i < mStringsImgList.size(); i++) {

                        params.put("project_img" + "[" + i + "]", new DataPart(mStringsImgList.get(i).substring(mStringsImgList.get(i).lastIndexOf("/") + 1),
                                AppHelper.readBytesFromFile(mStringsImgList.get(i))));
                    }

                    return params;
                }

                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId", UserUtils.getInstance().getUserID(mContext));
                    params.put("projectid", mProjectId);
                    params.put("today_work_descr", et_problem_description.getText().toString());
                    params.put("today_work_subject", et_prefer_time.getText().toString());
                    params.put("working_date", et_prefer_date.getText().toString());
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
        } else {
            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constant.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            mIsRequestInProgress = true;
            rv_post_multipleImg.setVisibility(View.VISIBLE);

            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    mIsRequestInProgress = false;
                    Uri mSelectedSingleImageUri = data.getData();
                    setImagePath(mSelectedSingleImageUri);

                    System.out.println("mSelectedSingleImageUri = " + mSelectedSingleImageUri);

                    String strSelectedImg = String.valueOf(mSelectedSingleImageUri);
                    mStringsImgList.add(strSelectedImg);

                    multipleImgList(mStringsImgList);


                } else {
                    mIsRequestInProgress = false;
                    ClipData mClipData = data.getClipData();
                    if (mClipData != null) {
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();

                            setImagePath(uri);
                            System.out.println("***uri = " + uri);
                            String path = String.valueOf(uri);
                            try {
                                mStringsImgList.add(path);
                                System.out.println("&&mStringsImgList = " + mStringsImgList.size());
                                System.out.println("&&&path = " + path);
                                multipleImgList(mStringsImgList);

                            } catch (Exception e) {
                                e.printStackTrace();
                                System.out.println("&&&e = " + e);
                            }
                        }
                    } else {
                        UIUtils.showToast(mContext, getString(R.string.app_name));
                    }
                }

                mIsRequestInProgress = false;
            }
        }
    }


    public void setImagePath(Uri mSelectedSingleImageUri) {
        String selectedFilePath = FilePath.getPath(mContext, mSelectedSingleImageUri);
        mStringsImgList.add(selectedFilePath);

        if (selectedFilePath != null && mSelectedSingleImageUri.getPath() != null && !mSelectedSingleImageUri.getPath().equals("")) {

            File mFile = new File(mSelectedSingleImageUri.getPath());
            if (mFile != null) {
                try {

                    long fileSizeInBytes = mFile.length();
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;
                    if (fileSizeInMB < Constant.MAX_UPLOAD_FILE_SIZE) {
                        Bitmap mProfileBitmap = MediaStore.Images.Media.getBitmap(mContext.getContentResolver(), mSelectedSingleImageUri);
                        mProfileBitmap = ImageUtils.getScaledImage(mSelectedSingleImageUri, mContext);
                        selectedFilePath = Utils.getImageStr(mProfileBitmap);

                        System.out.println("mProfileBitmap = " + selectedFilePath);
                    } else {
                        UIUtils.showToast(mContext, getString(R.string.UploadFileFileOption));
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("e = " + e.getMessage());
                    e.printStackTrace();
                }

            } else {
                UIUtils.showToast(mContext, "Some issue for uploaded images,please try again after some time.");
            }

        }

    }


    private void multipleImgList(ArrayList<String> aStringsImgList) {
        mStringsImgList = aStringsImgList;

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        rv_post_multipleImg.setLayoutManager(layoutManager);
        MultipleImagesNewAdapter mMultipleImagesAdapter = new MultipleImagesNewAdapter(mContext, mStringsImgList, this);
        rv_post_multipleImg.setAdapter(mMultipleImagesAdapter);
    }

    private void addServiceList() {

        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                .setTitle(getString(R.string.app_name))
                .setMessage("Successfully added your Project.")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent intent = new Intent(mContext, NavigationActivity.class);
                        mContext.startActivity(intent);


                    }
                }).show();
    }


    private void datePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mSelectedDOBCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mSelectedDOBCalendar.set(Calendar.MONTH, monthOfYear);
                        mSelectedDOBCalendar.set(Calendar.YEAR, year);
                        String dateStr = DateUtils.getDateStr(mSelectedDOBCalendar, DateUtils.DATE_WITHOUT_TIME_SERVER_FORMAT);
                        et_prefer_date.setText(dateStr);

                    }
                },
                mSelectedDOBCalendar.get(Calendar.YEAR),
                mSelectedDOBCalendar.get(Calendar.MONTH),
                mSelectedDOBCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());


        datePickerDialog.show();

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
    public void itemClicked(int aIndex) {

    }

    @Override
    public void itemOverFlow(int aIndex) {

    }
}