package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.consturctionbuddy.Bean.ProjectList.ProjectListBean;
import com.consturctionbuddy.Bean.ProjectList.Projectlist;
import com.consturctionbuddy.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter {

    private Context mContext;
    private ArrayList<Projectlist> mData;
    private int mTextColor;

    public SpinnerAdapter(@NonNull Context aContext, @LayoutRes int resource, @NonNull ArrayList<Projectlist> aObjects) {
        super(aContext, resource, aObjects);
        this.mContext = aContext;
        this.mData = aObjects;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View layout = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_spinner_item, parent, false);

        View topDivider = layout.findViewById(R.id.v_spinner_top);
        View bottomDivider = layout.findViewById(R.id.v_spinner_bottom);
        TextView tvText = layout.findViewById(R.id.tv_item);

        Projectlist projectlist = mData.get(position);
        tvText.setText(projectlist.getProjectName());

        if (position == 0) {
            topDivider.setVisibility(View.VISIBLE);
        }

        if (position == mData.size() - 1) {
            bottomDivider.setVisibility(View.GONE);
        }
        return layout;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
