package com.consturctionbuddy.Fragment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.DateUtils;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.ValidatorUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DailyWorkInformationFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Context mContext;
    private View mMainView;
    private Spinner mSpinnerServices;
    private AutoCompleteTextView et_prefer_date, et_prefer_time, et_problem_description;
    private Calendar mSelectedDOBCalendar;

    private int mHour, mMinute;

    private Button btn_submit, btn_cancel;

    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;
    private String item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_daily_work_information_form, container, false);
        init();
        return mMainView;
    }

    private void init() {

        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);

        et_prefer_date = mMainView.findViewById(R.id.et_prefer_date);
        et_prefer_time = mMainView.findViewById(R.id.et_working_subject);
        et_problem_description = mMainView.findViewById(R.id.et_problem_description);
        btn_submit = mMainView.findViewById(R.id.btn_submit);
        btn_cancel = mMainView.findViewById(R.id.btn_cancel);

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


        mSelectedDOBCalendar = Calendar.getInstance();
        mSelectedDOBCalendar.set(Calendar.DAY_OF_MONTH, 1);
        mSelectedDOBCalendar.set(Calendar.MONTH, Calendar.JANUARY);
        mSelectedDOBCalendar.set(Calendar.YEAR, 1990);
        String dateStr = DateUtils.getDateStr(mSelectedDOBCalendar, DateUtils.DATE_WITHOUT_TIME_SERVER_FORMAT);
        et_prefer_date.setText(dateStr);
        mSpinnerServices.getBackground().setColorFilter(ContextCompat.getColor(mContext, R.color.colorBlack), PorterDuff.Mode.SRC_ATOP);

        mSpinnerServices.setOnItemSelectedListener(this);
        String compareValue = "Select Project Name";

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Select Project Name");
        categories.add("Project A");
        categories.add("Project B");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        mSpinnerServices.setAdapter(dataAdapter);


        int spinnerPosition = dataAdapter.getPosition(compareValue);
        mSpinnerServices.setSelection(spinnerPosition);


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


    private void setValidation() {

        UIUtils.hideKeyBoard(getActivity());

        if (mSpinnerServices != null && mSpinnerServices.getSelectedItem() != null) {
            item = (String) mSpinnerServices.getSelectedItem();
        } else {

            UIUtils.showToast(mContext, "Please select project name.");
        }

        if (ValidatorUtils.NotEmptyValidator(mContext, et_prefer_time, true, "Please enter subject.")
                && ValidatorUtils.NotEmptyValidator(mContext, et_problem_description, true, "Please enter description..")
                && ValidatorUtils.NotEmptyValidator(mContext, et_prefer_date, true, "Please select date.")) {

            addServiceList();
          //  startHttpRequestForOngoingProject();
        }

    }

    private void startHttpRequestForOngoingProject() {

        /*boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {

            String baseUrl = Constant.API_ADD_SERVICE;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                JSONObject root = new JSONObject(response);
                                String strService = root.optString("service_id");
                                if (strService != null) {

                                    hideProgressBar();
                                    addServiceList();


                                } else {
                                    String strStatus = root.optString("status");
                                    String strMsg = root.optString("message");
                                    if (strStatus.equals("Failed")) {

                                        hideProgressBar();
                                        new AlertDialog.Builder(mContext, R.style.MyAlertDialogStyle)
                                                .setTitle(getString(R.string.app_name))
                                                .setMessage("Unable to add service.")
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
                    params.put("serviceList ", item);
                    params.put("problemDescription", et_problem_description.getText().toString());
                    params.put("preferDate", et_prefer_date.getText().toString());
                    params.put("preferTime ", et_prefer_time.getText().toString());
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
        }*/
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

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