package com.consturctionbuddy.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consturctionbuddy.R;

public class ProfileFragment extends Fragment {

    private Context mContext;
    private View mMainView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.activity_edit_profile, container, false);
        init();
        return mMainView;
    }

    private void init() {

    }
}
