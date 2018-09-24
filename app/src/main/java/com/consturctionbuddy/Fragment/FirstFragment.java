package com.consturctionbuddy.Fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consturctionbuddy.Activity.ShowPagerImagesActivity;
import com.consturctionbuddy.Adapter.HomeDataAdapter;
import com.consturctionbuddy.Bean.SectionDataModel;
import com.consturctionbuddy.Bean.TimeLineImage;
import com.consturctionbuddy.Interface.IMultipleImageClickCallback;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;

import java.util.ArrayList;

public class FirstFragment extends Fragment implements IMultipleImageClickCallback {


    private Context mContext;
    private ArrayList<SectionDataModel> mTimeLineImageList;
    private RecyclerView mTimeList;
    private View mMainView;

    private SectionDataModel dm;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.activity_timeline, container, false);
        init();
        return mMainView;
    }

    private void init() {

        mTimeLineImageList = new ArrayList<>();
        mTimeList = mMainView.findViewById(R.id.rv_productImg);
        setMultipleList();
    }


    private void setMultipleList() {

        createDummyData();
        if (mTimeLineImageList.size() > 0) {

           /* mTimeList.setItemViewCacheSize(20);
            mTimeList.setDrawingCacheEnabled(true);
            mTimeList.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
*/
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            mTimeList.setLayoutManager(layoutManager);
            mTimeList.setNestedScrollingEnabled(false);
            HomeDataAdapter mProductImageAdapter = new HomeDataAdapter(mContext, mTimeLineImageList, this);
            mTimeList.setAdapter(mProductImageAdapter);

        }
    }


    public void createDummyData() {
        for (int i = 1; i <= 5; i++) {

            dm = new SectionDataModel();

            dm.setHeaderTitle("21/9/18");

            ArrayList<TimeLineImage> singleItem = new ArrayList<TimeLineImage>();
            for (int j = 0; j <= 5; j++) {
                singleItem.add(new TimeLineImage("https://cdn2.vectorstock.com/i/1000x1000/49/71/crane-poured-concrete-construction-vector-7324971.jpg", "Testing", "21/9/2018", "\"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.t is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout" + j));
            }

            dm.setAllItemsInSection(singleItem);

            mTimeLineImageList.add(dm);

        }
    }

    @Override
    public void itemClicked(int aIndex, SectionDataModel aTimeLineImage) {
        showOverFlowImages(aIndex, dm);
    }

    @Override
    public void itemOverFlow(int aIndex, SectionDataModel aTimeLineImage) {
        showOverFlowImages(aIndex, dm);
    }


    private void showOverFlowImages(int aIndex, SectionDataModel aTimeLineImage) {
        Intent intent = new Intent(mContext, ShowPagerImagesActivity.class);
        intent.putExtra(Constant.INTENT_IMAGE_SELECTED_INDEX_KEY, aIndex);
        intent.putExtra(Constant.INTENT_IMAGE_LIST_INDEX_KEY, aTimeLineImage);
        //intent.putParcelableArrayListExtra(Constant.INTENT_IMAGE_LIST_INDEX_KEY, aTimeLineImage);
        startActivity(intent);
    }
}


