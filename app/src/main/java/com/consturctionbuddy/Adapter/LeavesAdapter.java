package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.consturctionbuddy.Bean.Leafe;
import com.consturctionbuddy.R;
import com.consturctionbuddy.custom.CustomRegularTextView;

import java.util.ArrayList;


public class LeavesAdapter extends RecyclerView.Adapter<LeavesAdapter.MyViewHolder> {

    private ArrayList<Leafe> mOrderList;
    private Context mContext;


    public LeavesAdapter(Context aContext, ArrayList<Leafe> aOrderlist) {
        this.mContext = aContext;
        this.mOrderList = aOrderlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaves_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final Leafe curBean = mOrderList.get(position);
        String status = "";

        if (curBean.getApprovedStatus().equals("0")) {

            status = "Not Approved";
        } else {

            status = "Approved";
        }

        holder.tv_service1.setText(curBean.getStartDate());
        holder.tv_service2.setText(curBean.getEndDate());
        holder.tv_service3.setText(curBean.getReasonleave());
        holder.tv_service4.setText(status);


    }

    @Override
    public int getItemCount() {
        return mOrderList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomRegularTextView tv_service1;
        CustomRegularTextView tv_service2;
        CustomRegularTextView tv_service3;
        CustomRegularTextView tv_service4, prefer_time;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_service1 = itemView.findViewById(R.id.tv_staff_name);
            tv_service2 = itemView.findViewById(R.id.tv_staff_email);
            tv_service3 = itemView.findViewById(R.id.tv_staff_jod);
            tv_service4 = itemView.findViewById(R.id.tv_service4);
            prefer_time = itemView.findViewById(R.id.prefer_time);

        }
    }
}
