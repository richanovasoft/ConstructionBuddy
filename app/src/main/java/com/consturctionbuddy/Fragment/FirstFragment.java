package com.consturctionbuddy.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Activity.NavigationActivity;
import com.consturctionbuddy.Activity.ShowPagerImagesActivity;
import com.consturctionbuddy.Activity.TotalStaffActivity;
import com.consturctionbuddy.Adapter.HomeDataAdapter;
import com.consturctionbuddy.Bean.LeavesBean;
import com.consturctionbuddy.Bean.SectionDataModel;
import com.consturctionbuddy.Bean.TimeLine.Datum;
import com.consturctionbuddy.Bean.TimeLine.ProjectImg;
import com.consturctionbuddy.Bean.TimeLine.TimeLineBean;
import com.consturctionbuddy.Bean.TimeLineImage;
import com.consturctionbuddy.Bean.UserResponse.TotalCount;
import com.consturctionbuddy.Bean.UserResponse.UserInfo;
import com.consturctionbuddy.Interface.IMultipleImageClickCallback;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.consturctionbuddy.custom.CustomRegularTextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FirstFragment extends Fragment implements IMultipleImageClickCallback {


    private Context mContext;
    private ArrayList<Datum> mTimeLineImageList;
    private RecyclerView mTimeList;
    private View mMainView;

    private boolean mProgressBarShowing = false;
    private boolean mIsRequestInProgress;
    private RelativeLayout mProgressBarLayout;
    private LinearLayout ll_total_staff;
    private CustomRegularTextView tv_total_material, tv_total_staff, tv_total_projects, tv_total_Users;
    private LinearLayout ll_top;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.activity_timeline, container, false);
        init();
        return mMainView;
    }

    private void init() {

        ((NavigationActivity) mContext).setTitle(R.string.app_name);

        mTimeLineImageList = new ArrayList<>();
        mTimeList = mMainView.findViewById(R.id.rv_productImg);
        ll_top = mMainView.findViewById(R.id.ll_top);
        ll_top.setVisibility(View.GONE);
        ll_total_staff = mMainView.findViewById(R.id.ll_total_staff);
        tv_total_material = mMainView.findViewById(R.id.tv_total_material);
        tv_total_staff = mMainView.findViewById(R.id.tv_total_staff);
        tv_total_Users = mMainView.findViewById(R.id.tv_total_users);
        tv_total_projects = mMainView.findViewById(R.id.tv_total_projects);
        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);


        ll_total_staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent = new Intent(mContext, TotalStaffActivity.class);
                // startActivity(intent);
            }
        });

        startHttpRequestForTotalCount();
        startHttpRequestForHome();
    }


    private void startHttpRequestForHome() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            showProgressBar();
            String baseUrl = Constant.API_HOME;
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {


                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                TimeLineBean timeLineBean = gson.fromJson(jsonResp, TimeLineBean.class);
                                if (timeLineBean != null && timeLineBean.getStatus() == 200) {

                                    hideProgressBar();
                                    setMultipleList(timeLineBean.getData());
                                } else {
                                    hideProgressBar();
                                    mTimeList.setVisibility(View.GONE);

                                }

                            } catch (Exception e) {
                                hideProgressBar();

                                e.printStackTrace();
                                System.out.println("e = " + e.getMessage());
                                mTimeList.setVisibility(View.GONE);

                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            hideProgressBar();

                            if (error.getClass().equals(NoConnectionError.class)) {
                            } else {
                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userid", UserUtils.getInstance().getUserID(mContext));
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new
                    DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
        }
    }


    private void setMultipleList(ArrayList<Datum> posts) {
        mTimeLineImageList = posts;
        if (mTimeLineImageList.size() > 0) {
            LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mTimeList.setLayoutManager(layoutManager);
            HomeDataAdapter mProductImageAdapter = new HomeDataAdapter(mContext, mTimeLineImageList, this);
            mTimeList.setAdapter(mProductImageAdapter);
            mTimeList.setNestedScrollingEnabled(false);
        }
    }


 /*   public void createDummyData() {
        for (int i = 1; i <= 2; i++) {

            dm = new SectionDataModel();

            dm.setHeaderTitle("21/9/18");

            ArrayList<TimeLineImage> singleItem = new ArrayList<TimeLineImage>();
            for (int j = 0; j <= 5; j++) {
                singleItem.add(new TimeLineImage("https://cdn2.vectorstock.com/i/1000x1000/49/71/crane-poured-concrete-construction-vector-7324971.jpg", "Testing", "21/9/2018", "\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout" + j));
            }

            dm.setAllItemsInSection(singleItem);

            mTimeLineImageList.add(dm);

        }
    }*/


    private void showOverFlowImages(int aIndex, ProjectImg aTimeLineImage) {
        Intent intent = new Intent(mContext, ShowPagerImagesActivity.class);
        intent.putExtra(Constant.INTENT_IMAGE_SELECTED_INDEX_KEY, aIndex);
        intent.putExtra(Constant.INTENT_IMAGE_LIST_INDEX_KEY, aTimeLineImage);
        //intent.putParcelableArrayListExtra(Constant.INTENT_IMAGE_LIST_INDEX_KEY, aTimeLineImage);
        startActivity(intent);
    }


    private void startHttpRequestForTotalCount() {
        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            showProgressBar();

            String baseUrl = Constant.API_TOTAL_COUNT;
            StringRequest mStrRequest = new StringRequest(Request.Method.GET, baseUrl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {


                    try {

                        Gson gson = new GsonBuilder().create();
                        JsonParser jsonParser = new JsonParser();
                        JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                        TotalCount totalCount = gson.fromJson(jsonResp, TotalCount.class);
                        if (totalCount != null && totalCount.getmStatus() == 200) {
                            hideProgressBar();
                            setAllCountValues(totalCount);

                        } else {
                            hideProgressBar();
                            System.out.println(" error");
                        }
                    } catch (Exception e) {
                        System.out.println("e = " + e);
                    }
                }
            },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NetworkError) {
                                hideProgressBar();

                            }
                        }
                    });
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        }
    }

    @SuppressLint("SetTextI18n")
    private void setAllCountValues(TotalCount totalCount) {

        ll_top.setVisibility(View.VISIBLE);

        tv_total_material.setText(totalCount.getmTotalMaterials() + " " + " Total Material");
        tv_total_Users.setText(totalCount.getmTotalUsers() + " " + " Total users");
        tv_total_projects.setText(totalCount.getmTotalProjects() + " " + " Total Projects");
        tv_total_staff.setText(totalCount.getmTotalStaffs() + " " + " Total Staffs");
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
    public void itemClicked(int aIndex, ProjectImg aTimeLineImage) {
        showOverFlowImages(aIndex, aTimeLineImage);
    }

    @Override
    public void itemOverFlow(int aIndex, ProjectImg aTimeLineImage) {
        showOverFlowImages(aIndex, aTimeLineImage);
    }
}


