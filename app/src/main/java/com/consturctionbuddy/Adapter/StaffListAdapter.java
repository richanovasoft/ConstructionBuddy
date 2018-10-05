package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.consturctionbuddy.Activity.EditStaffProfileActivity;
import com.consturctionbuddy.Bean.Totalstaff.TotalStaffListBean;
import com.consturctionbuddy.R;
import com.consturctionbuddy.custom.CustomRegularTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.MyViewHolder> {

    private ArrayList<TotalStaffListBean> mSiteImageList;
    private Context mContext;


    public StaffListAdapter(Context aContext, ArrayList<TotalStaffListBean> aOrderlist) {
        this.mContext = aContext;
        this.mSiteImageList = aOrderlist;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_staff_list, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {

        final TotalStaffListBean curBean = mSiteImageList.get(position);

        holder.tv_service1.setText(curBean.getmStrName());
        holder.tv_service2.setText(curBean.getmStrEmail());
        holder.tv_service3.setText(curBean.getmStrJOD());


        holder.ll_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EditStaffProfileActivity.class);
                mContext.startActivity(intent);
            }
        });

        if (curBean.getmStrImage() != null) {

            Picasso.with(mContext)
                    .load(curBean.getmStrImage()).placeholder(R.drawable.ic_default_profile)
                    .into(holder.img_staff_profile);
        } else {

            holder.img_staff_profile.setImageResource(R.drawable.ic_default_profile);
        }


    }

    @Override
    public int getItemCount() {
        return mSiteImageList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        CustomRegularTextView tv_service1;
        CustomRegularTextView tv_service2;
        CustomRegularTextView tv_service3;
        private LinearLayout ll_edit, ll_delete;
        private ImageView img_staff_profile;


        MyViewHolder(View itemView) {
            super(itemView);
            tv_service1 = itemView.findViewById(R.id.tv_staff_name);
            tv_service2 = itemView.findViewById(R.id.tv_staff_email);
            tv_service3 = itemView.findViewById(R.id.tv_staff_jod);
            ll_edit = itemView.findViewById(R.id.ll_edit);
            ll_delete = itemView.findViewById(R.id.ll_delete);
            img_staff_profile = itemView.findViewById(R.id.img_staff_profile);

        }
    }
}
