package com.consturctionbuddy.Adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.consturctionbuddy.Bean.TimeLine.ProjectImg;
import com.consturctionbuddy.Bean.TimeLineImage;
import com.consturctionbuddy.Fragment.SliderFragment;

import java.util.ArrayList;



public class SliderPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<ProjectImg> mUrlList;

    public SliderPagerAdapter(ArrayList<ProjectImg> arrayList, FragmentManager fm) {
        super(fm);
        mUrlList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new SliderFragment();
        ProjectImg url = mUrlList.get(position);
        Bundle b = new Bundle();
        b.putString("fullImageUrl", url.getPath());
        f.setArguments(b);

        return f;
    }

    @Override
    public int getCount() {
        return mUrlList.size();
    }
}