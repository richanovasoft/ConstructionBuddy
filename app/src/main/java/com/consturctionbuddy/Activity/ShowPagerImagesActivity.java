package com.consturctionbuddy.Activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.consturctionbuddy.Adapter.SliderPagerAdapter;
import com.consturctionbuddy.Bean.SectionDataModel;
import com.consturctionbuddy.Bean.TimeLine.ProjectImg;
import com.consturctionbuddy.Bean.TimeLineImage;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.custom.CustomViewPager;
import java.util.ArrayList;


public class ShowPagerImagesActivity extends AppCompatActivity {

    private CustomViewPager mViewPager;
    private SliderPagerAdapter mSliderPagerAdapter;
    private LinearLayout mBottomDotLayout;
    private ArrayList<ImageView> mDotImageViewList;
    private int mSelectedIndex;
    private Context mContext;


    ArrayList<ProjectImg> postlist;
    //SectionDataModel sectionDataModel;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_full_images_pager);
        mContext = this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_setting);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.app_name));

        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        changeStatusBarColor();

        mBottomDotLayout = (LinearLayout) findViewById(R.id.ll_bottomdots);
        mViewPager = (CustomViewPager) findViewById(R.id.imageviewPager);

        mSelectedIndex = 0;
        mDotImageViewList = new ArrayList<>();
        postlist = new ArrayList<>();


        if (getIntent() != null) {

            postlist = getIntent().getParcelableArrayListExtra(Constant.INTENT_IMAGE_LIST_INDEX_KEY);

            mSelectedIndex = 0;
        }


        setUpView();

        if (postlist.size() > 1) {
            setDot();
            setSelectedDots(mSelectedIndex);

        } else {
            mBottomDotLayout.setVisibility(View.GONE);
        }
    }


    private void setDot() {

        for (int i = 0; i < postlist.size(); i++) {
            ImageView currentImageview = new ImageView(this);
            LinearLayout.LayoutParams vp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayoutCompat.LayoutParams.WRAP_CONTENT);
            vp.rightMargin = 5;
            currentImageview.setLayoutParams(vp);
            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_white_dots);
            currentImageview.setImageDrawable(myDrawable);
            mBottomDotLayout.addView(currentImageview);
            mDotImageViewList.add(currentImageview);
        }
    }


    private void setSelectedDots(int index) {
        ImageView oldSelectedIv = mDotImageViewList.get(mSelectedIndex);
        Drawable myDrawable = getResources().getDrawable(R.drawable.ic_white_dots);
        oldSelectedIv.setImageDrawable(myDrawable);
        ImageView newSelectedIv = mDotImageViewList.get(index);
        Drawable newDrawable = getResources().getDrawable(R.drawable.ic_yellow_dots);
        newSelectedIv.setImageDrawable(newDrawable);
        mSelectedIndex = index;
    }


    private void setUpView() {
        mSliderPagerAdapter = new SliderPagerAdapter(postlist, getSupportFragmentManager());
        mViewPager.setAdapter(mSliderPagerAdapter);
        mViewPager.setCurrentItem(mSelectedIndex);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelectedDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                handleBackButton();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void handleBackButton() {
        finish();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            int color = ContextCompat.getColor(this, R.color.colorPrimaryDark);
            window.setStatusBarColor(color);
        }
    }
}
