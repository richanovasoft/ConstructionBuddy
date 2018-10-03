package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consturctionbuddy.Bean.SiteImageBean;
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
        holder.tv_service2.setText(curBean.getmSiteImage());
        holder.tv_service3.setText(curBean.getmUpdate());
        holder.tv_service4.setText(curBean.getmRemove());


    }

    @Override
    public int getItemCount() {
        return mSiteImageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomRegularTextView tv_service1;
        CustomRegularTextView tv_service2;
        CustomRegularTextView tv_service3;
        CustomRegularTextView tv_service4;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_service1 = itemView.findViewById(R.id.tv_service1);
            tv_service2 = itemView.findViewById(R.id.tv_service2);
            tv_service3 = itemView.findViewById(R.id.tv_service3);
            tv_service4 = itemView.findViewById(R.id.tv_service4);

        }
    }
}
