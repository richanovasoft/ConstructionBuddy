package com.consturctionbuddy.Adapter;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.consturctionbuddy.Bean.SectionDataModel;
import com.consturctionbuddy.Bean.TimeLineImage;
import com.consturctionbuddy.Interface.IMultipleImageClickCallback;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.custom.CustomBoldTextView;
import com.consturctionbuddy.custom.CustomRegularTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MultipleImagesAdapter extends RecyclerView.Adapter<MultipleImagesAdapter.MyViewHolder> {

    private Context mContext;
    private List<TimeLineImage> mMultipleImgList;
    private int mAdapterSize;
    private int mSelectedIndex;
    private boolean isDescExpanded;
    private SectionDataModel mSectionDataModel;

    private IMultipleImageClickCallback mIMultipleImageClickCallback;

    public MultipleImagesAdapter(Context aContext, ArrayList<TimeLineImage> aMultipleImgList, IMultipleImageClickCallback aIMultipleImageClickCallback, SectionDataModel allItemsInSection) {
        this.mContext = aContext;
        this.mMultipleImgList = aMultipleImgList;
        this.mIMultipleImageClickCallback = aIMultipleImageClickCallback;
        mSelectedIndex = 0;
        if (mMultipleImgList.size() > Constant.MAX_PRODUCT_IMAGE_DISPLAY) {
            mAdapterSize = Constant.MAX_PRODUCT_IMAGE_DISPLAY;
        } else {
            mAdapterSize = aMultipleImgList.size();
        }

        this.mSectionDataModel = allItemsInSection;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_multiple_imgs_adapter, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final TimeLineImage timeLineImage = mMultipleImgList.get(position);

        if (timeLineImage.getMsTrImageUrl() != null) {
            Picasso.with(mContext)
                    .load(timeLineImage.getMsTrImageUrl()).placeholder(R.drawable.placeholder)
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

        holder.tv_image_date.setText(timeLineImage.getmDate());
        holder.tv_image_date.setVisibility(View.GONE);

        holder.tv_image_desc.setText(timeLineImage.getmDesc());
        holder.tv_image_title.setText(timeLineImage.getmTitle());
        holder.tv_image_desc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isDescExpanded) {
                    isDescExpanded = false;
                    if (holder.tv_image_desc.getLineCount() > 4) {
                        holder.tv_read_more.setVisibility(View.VISIBLE);
                        ObjectAnimator animation = ObjectAnimator.ofInt(holder.tv_image_desc, "maxLines", 4);
                        animation.setDuration(0).start();
                    }
                }
            }
        });

        holder.tv_read_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.show_more_dialog);

                CustomRegularTextView text = dialog.findViewById(R.id.text_dialog);
                text.setText(timeLineImage.getmDesc());

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


                /*if (!isDescExpanded) {
                    isDescExpanded = true;
                    ObjectAnimator animation = ObjectAnimator.ofInt(holder.tv_image_desc, "maxLines", 40);
                    animation.setDuration(10).start();
                    holder.tv_read_more.setText("Read less");

                } else {
                    isDescExpanded = false;
                    ObjectAnimator animation = ObjectAnimator.ofInt(holder.tv_image_desc, "maxLines", 4);
                    animation.setDuration(10).start();
                    holder.tv_read_more.setText("Read more...");
                }*/

            }
        });


        holder.mRowMainRl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position == Constant.MAX_PRODUCT_IMAGE_DISPLAY - 1) {
                    if (mMultipleImgList.size() > Constant.MAX_PRODUCT_IMAGE_DISPLAY) {
                        mIMultipleImageClickCallback.itemOverFlow(position, mSectionDataModel);
                    } else {
                        mIMultipleImageClickCallback.itemClicked(position, mSectionDataModel);
                        mSelectedIndex = position;
                        notifyDataSetChanged();
                    }
                } else {
                    mIMultipleImageClickCallback.itemClicked(position, mSectionDataModel);
                    mSelectedIndex = position;
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

        private CustomBoldTextView tv_image_date;
        private CustomBoldTextView tv_image_title;
        private CustomRegularTextView tv_image_desc;
        private CustomRegularTextView tv_read_more;


        public MyViewHolder(View view) {
            super(view);
            imageView = view.findViewById(R.id.iv_multiple_image_icon);
            mRowMainRl = view.findViewById(R.id.rl_product_details_image_row_main);
            mOverFlowRl = view.findViewById(R.id.rl_multiple_image_over_flow);
            mOverFlowTv = view.findViewById(R.id.tv_multiple_item_over_flow);
            tv_image_date = view.findViewById(R.id.tv_image_date);
            tv_image_desc = view.findViewById(R.id.tv_image_desc);
            tv_image_title = view.findViewById(R.id.tv_image_title);
            tv_read_more = view.findViewById(R.id.tv_read_more);
        }
    }
}
