package com.consturctionbuddy.Fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Activity.ApplyLeavesActivity;
import com.consturctionbuddy.Activity.NavigationActivity;
import com.consturctionbuddy.Adapter.LeavesAdapter;
import com.consturctionbuddy.Bean.Leafe;
import com.consturctionbuddy.Bean.LeavesBean;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestForLeaveFragment extends Fragment {

    private View mMainView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private LeavesAdapter mLeaveListAdapter;
    private ArrayList<Leafe> mleaveList;
    private Context mContext;

    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;


    private FloatingActionButton fab_add_my_album_listing;

    public RequestForLeaveFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;

        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_add_leaves, container, false);

        init();
        return mMainView;
    }

    private void init() {

        ((NavigationActivity) mContext).setTitle("Request for leave");

        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);
        mRecyclerView = (RecyclerView) mMainView.findViewById(R.id.rv_service_request);
        fab_add_my_album_listing = mMainView.findViewById(R.id.fab_add_my_album_listing);
        fab_add_my_album_listing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ApplyLeavesActivity.class);
                mContext.startActivity(intent);
            }
        });
        mleaveList = new ArrayList<>();


        startHttpRequestForLeaveRequest();

    }

    private void startHttpRequestForLeaveRequest() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_LEAVE_REQUEST_LIST;
            showProgressBar();
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                LeavesBean listResponseBean = gson.fromJson(jsonResp, LeavesBean.class);

                                if (listResponseBean != null && listResponseBean.getLeaves() != null && listResponseBean.getStatus() == 200) {

                                    hideProgressBar();
                                    setLeaveList(listResponseBean.getLeaves());
                                } else {
                                    hideProgressBar();
                                    mRecyclerView.setVisibility(View.GONE);

                                }


                            } catch (Exception e) {
                                hideProgressBar();
                                mRecyclerView.setVisibility(View.GONE);

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

    private void setLeaveList(ArrayList<Leafe> posts) {

        if (posts.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mleaveList = posts;
            mLeaveListAdapter = new LeavesAdapter(mContext, mleaveList);
            mLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mLeaveListAdapter);
            mRecyclerView.setNestedScrollingEnabled(false);
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

    @Override
    public void onResume() {
        super.onResume();

    }
}
