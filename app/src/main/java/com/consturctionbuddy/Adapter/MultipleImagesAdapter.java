package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.consturctionbuddy.Bean.TimeLine.Datum;
import com.consturctionbuddy.Bean.TimeLine.ProjectImg;
import com.consturctionbuddy.Interface.IMultipleImageClickCallback;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.custom.CustomRegularTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MultipleImagesAdapter extends RecyclerView.Adapter<MultipleImagesAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ProjectImg> mMultipleImgList;
    private int mAdapterSize;
    private int mSelectedIndex;

    private Datum mAllItemsInSection;

    private IMultipleImageClickCallback mIMultipleImageClickCallback;


    public MultipleImagesAdapter(Context aContext, ArrayList<ProjectImg> projectImg, Datum allItemsInSection, int aPosition, IMultipleImageClickCallback aIMultipleImageClickCallback) {
        this.mContext = aContext;
        this.mMultipleImgList = projectImg;
        this.mIMultipleImageClickCallback = aIMultipleImageClickCallback;
        this.mAllItemsInSection = allItemsInSection;
        mSelectedIndex = aPosition;
        if (mMultipleImgList.size() > Constant.MAX_PRODUCT_IMAGE_DISPLAY) {
            mAdapterSize = Constant.MAX_PRODUCT_IMAGE_DISPLAY;
        } else {
            mAdapterSize = projectImg.size();
        }

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_multiple_imgs_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final ProjectImg timeLineImage = mMultipleImgList.get(position);

        if (timeLineImage.getPath() != null) {
            Picasso.with(mContext)
                    .load(timeLineImage.getPath()).placeholder(R.drawable.placeholder)
                    .into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.placeholder);
        }
        boolean overFlowPosition = false;
        if (position == Constant.MAX_PRODUCT_IMAGE_DISPLAY - 1 && position < mMultipleImgList.size() - 1) {
            overFlowPosition = true;
        }
        if (overFlowPosition) {
            holder.mOverFlowRl.setVisibility(View.VISIBLE);
            String overFlowTxt = "+" + (mMultipleImgList.size() - Constant.MAX_PRODUCT_IMAGE_DISPLAY);
            holder.mOverFlowTv.setText(overFlowTxt);
        } else {
            holder.mOverFlowRl.setVisibility(View.GONE);
        }


        holder.mRowMainRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == Constant.MAX_PRODUCT_IMAGE_DISPLAY - 1) {
                    if (mMultipleImgList.size() > Constant.MAX_PRODUCT_IMAGE_DISPLAY) {
                        mIMultipleImageClickCallback.itemOverFlow(mSelectedIndex, mAllItemsInSection);
                    } else {
                        mIMultipleImageClickCallback.itemClicked(mSelectedIndex, mAllItemsInSection);
                        //mSelectedIndex = position;
                        notifyDataSetChanged();
                    }
                } else {
                    mIMultipleImageClickCallback.itemClicked(mSelectedIndex, mAllItemsInSection);
                    //mSelectedIndex = position;
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAdapterSize;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        RelativeLayout mRowMainRl;
        RelativeLayout mOverFlowRl;
        CustomRegularTextView mOverFlowTv;


        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_multiple_image_icon);
            mRowMainRl = view.findViewById(R.id.rl_product_details_image_row_main);
            mOverFlowRl = view.findViewById(R.id.rl_multiple_image_over_flow);
            mOverFlowTv = view.findViewById(R.id.tv_multiple_item_over_flow);

        }
    }
}
