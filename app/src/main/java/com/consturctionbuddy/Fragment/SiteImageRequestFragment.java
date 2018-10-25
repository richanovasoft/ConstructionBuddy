package com.consturctionbuddy.Fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.consturctionbuddy.Activity.NavigationActivity;
import com.consturctionbuddy.Adapter.SiteImageAdapter;
import com.consturctionbuddy.Bean.SiteImage.SiteImageBean;
import com.consturctionbuddy.Bean.SiteImage.SiteImageListBean;
import com.consturctionbuddy.Interface.ISiteImageEditClickListner;
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

public class SiteImageRequestFragment extends Fragment implements ISiteImageEditClickListner {

    private View mMainView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView mRecyclerView;
    private SiteImageAdapter mSiteImageAdapter;
    private ArrayList<SiteImageBean> mServiceList;
    private Context mContext;

    private boolean mIsRequestInProgress;
    private boolean mProgressBarShowing = false;
    private RelativeLayout mProgressBarLayout;


    public SiteImageRequestFragment() {
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
        mMainView = inflater.inflate(R.layout.fragment_site_image, container, false);

        init();
        return mMainView;
    }

    private void init() {

        ((NavigationActivity) mContext).setTitle("List site management");

        mProgressBarLayout = mMainView.findViewById(R.id.rl_progressBar);
        mRecyclerView = (RecyclerView) mMainView.findViewById(R.id.rv_service_request);
        mServiceList = new ArrayList<>();


        startHttpRequestForSiteImageList();

    }

    private void startHttpRequestForSiteImageList() {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            showProgressBar();
            String baseUrl = Constant.API_SITE_IMAGE_LIST;
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                SiteImageListBean listResponseBean = gson.fromJson(jsonResp, SiteImageListBean.class);

                                if (listResponseBean != null && listResponseBean.getmSiteImageList() != null && listResponseBean.getStatus() == 200) {

                                    hideProgressBar();
                                    setSiteImageList(listResponseBean.getmSiteImageList());
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

    private void setSiteImageList(ArrayList<SiteImageBean> posts) {

        if (posts.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mServiceList = posts;
            mSiteImageAdapter = new SiteImageAdapter(mContext, mServiceList, this);
            mLayoutManager = new LinearLayoutManager(mContext);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
            mRecyclerView.setAdapter(mSiteImageAdapter);
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

    @Override
    public void iSiteImagEditClick(SiteImageBean siteImageBean) {

    }
}
