package com.consturctionbuddy.Activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Bean.StatusBean;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.DateUtils;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.Utility.ValidatorUtils;
import com.consturctionbuddy.custom.CustomEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ApplyLeavesActivity extends AppCompatActivity {

    private Context mContext;
    private CustomEditText et_prefer_date, et_prefer_time, et_problem_description;
    private Calendar mSelectedDOBCalendar;

    private int mHour, mMinute;

    private Button btn_submit, btn_cancel;

    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_leave);
        // Set up the login form.

        changeStatusBarColor();
        mContext = this;


        ApplyLeavesActivity.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mContext = this;
        changeStatusBarColor();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setTitle("Apply Leave");
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

        et_prefer_date = findViewById(R.id.et_prefer_date);
        et_prefer_time = findViewById(R.id.et_prefer_time);
        et_problem_description = findViewById(R.id.et_problem_description);
        btn_submit = findViewById(R.id.btn_submit);
        btn_cancel = findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setValidation();
            }
        });


        mSelectedDOBCalendar = Calendar.getInstance();
        mSelectedDOBCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mSelectedDOBCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        mSelectedDOBCalendar.set(Calendar.YEAR, 1990);
        String dateStr = DateUtils.getDateStr(mSelectedDOBCalendar, DateUtils.DATE_WITHOUT_TIME_SERVER_FORMAT);
        et_prefer_date.setText(dateStr);

        et_prefer_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker();

            }
        });

        /*et_prefer_date.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker();
                    et_prefer_date.clearFocus();
                }
            }
        });*/


        et_prefer_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker1();

            }
        });

        et_prefer_time.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    datePicker1();
                    et_prefer_time.clearFocus();
                }
            }
        });

    }

    private void setValidation() {

        UIUtils.hideKeyBoard(this);


        if (ValidatorUtils.NotEmptyValidator(mContext, et_problem_description, true, "Please enter reason for leave.")
                && ValidatorUtils.NotEmptyValidator(mContext, et_prefer_date, true, "Please select start date.")
                && ValidatorUtils.NotEmptyValidator(mContext, et_prefer_time, true, "Please select end date.")) {

            startHttpRequestForAddServiceRequest();
        }

    }

    private void startHttpRequestForAddServiceRequest() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {

            String baseUrl = Constant.APPLY_LEAVES_API;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                StatusBean statusBean = gson.fromJson(jsonResp, StatusBean.class);

                                if (statusBean != null && statusBean.getStatus() == 200) {

                                    addServiceList();
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
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userId", UserUtils.getInstance().getUserID(mContext));
                    params.put("reasonleave", et_problem_description.getText().toString());
                    params.put("start_date", et_prefer_date.getText().toString());
                    params.put("end_date", et_prefer_time.getText().toString());
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
            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
        }
    }

    private void addServiceList() {

        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                .setTitle(getString(R.string.app_name))
                .setMessage("Successfully added your Leaves.")
                .setCancelable(false)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        // Intent intent = new Intent(mContext, HomeActivity.class);
                        //  startActivity(intent);
                        finish();

                    }
                }).show();
    }

    private void timePicker() {


        // Get Current Time
        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        et_prefer_time.setText(hourOfDay + ":" + minute);
                    }
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }


    private void datePicker() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
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

    private void datePicker1() {

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        mSelectedDOBCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        mSelectedDOBCalendar.set(Calendar.MONTH, monthOfYear);
                        mSelectedDOBCalendar.set(Calendar.YEAR, year);
                        String dateStr = DateUtils.getDateStr(mSelectedDOBCalendar, DateUtils.DATE_WITHOUT_TIME_SERVER_FORMAT);
                        et_prefer_time.setText(dateStr);

                    }
                },
                mSelectedDOBCalendar.get(Calendar.YEAR),
                mSelectedDOBCalendar.get(Calendar.MONTH),
                mSelectedDOBCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());


        datePickerDialog.show();

    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorBlack);
            window.setStatusBarColor(color);
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
