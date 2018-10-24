package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.consturctionbuddy.Bean.SiteImage.SiteImageBean;
import com.consturctionbuddy.R;
import com.consturctionbuddy.custom.CustomRegularTextView;

import java.util.ArrayList;


public class SiteImageAdapter extends RecyclerView.Adapter<SiteImageAdapter.MyViewHolder> {

    private ArrayList<SiteImageBean> mSiteImageList;
    private Context mContext;


    public SiteImageAdapter(Context aContext, ArrayList<SiteImageBean> aOrderlist) {
        this.mContext = aContext;
        this.mSiteImageList = aOrderlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_site_image_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final SiteImageBean curBean = mSiteImageList.get(position);

        holder.tv_service1.setText(curBean.getmSiteName());

        holder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    @Override
    public int getItemCount() {
        return mSiteImageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomRegularTextView tv_service1;
        private LinearLayout ll_edit;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_service1 = itemView.findViewById(R.id.tv_staff_name);
            ll_edit = itemView.findViewById(R.id.ll_edit);


        }
    }
}
