package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.consturctionbuddy.Activity.EditStaffProfileActivity;
import com.consturctionbuddy.Bean.TotalUsers.TotalUsersListBean;
import com.consturctionbuddy.R;
import com.consturctionbuddy.custom.CustomRegularTextView;

import java.util.ArrayList;


public class TotalUserListAdapter extends RecyclerView.Adapter<TotalUserListAdapter.MyViewHolder> {

    private ArrayList<TotalUsersListBean> mSiteImageList;
    private Context mContext;


    public TotalUserListAdapter(Context aContext, ArrayList<TotalUsersListBean> aOrderlist) {
        this.mContext = aContext;
        this.mSiteImageList = aOrderlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_users_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final TotalUsersListBean curBean = mSiteImageList.get(position);

        holder.tv_service1.setText(curBean.getmStrName());
        holder.tv_service2.setText(curBean.getmStrEmail());
        holder.tv_service3.setText(curBean.getmStrPhone());
        holder.tv_service4.setText(curBean.getmStrRegisteredDate() + " " + "(" + curBean.getmStatus() + ")");


        holder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditStaffProfileActivity.class);
                mContext.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mSiteImageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomRegularTextView tv_service1;
        CustomRegularTextView tv_service2;
        CustomRegularTextView tv_service3, tv_service4;
        private LinearLayout ll_edit, ll_delete;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_service1 = itemView.findViewById(R.id.tv_staff_name);
            tv_service2 = itemView.findViewById(R.id.tv_staff_email);
            tv_service3 = itemView.findViewById(R.id.tv_staff_jod);
            tv_service4 = itemView.findViewById(R.id.tv_service4);
            ll_edit = itemView.findViewById(R.id.ll_edit);
            ll_delete = itemView.findViewById(R.id.ll_delete);

        }
    }
}
