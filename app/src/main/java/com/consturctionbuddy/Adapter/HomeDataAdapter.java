package com.consturctionbuddy.Adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.consturctionbuddy.Bean.SectionDataModel;
import com.consturctionbuddy.Interface.IMultipleImageClickCallback;
import com.consturctionbuddy.R;

import java.util.ArrayList;

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.ItemRowHolder> {

    private ArrayList<SectionDataModel> dataList;
    private Context mContext;
    private IMultipleImageClickCallback mIMultipleImageClickCallback;


    public HomeDataAdapter(Context context, ArrayList<SectionDataModel> dataList, IMultipleImageClickCallback aIMultipleImageClickCallback) {
        this.dataList = dataList;
        this.mContext = context;
        this.mIMultipleImageClickCallback = aIMultipleImageClickCallback;
    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, null);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemRowHolder itemRowHolder, int i) {


        SectionDataModel allItemsInSection = dataList.get(i);
        String sectionName = dataList.get(i).getHeaderTitle();
        itemRowHolder.itemTitle.setText(sectionName);
        itemRowHolder.itemTitle.setVisibility(View.VISIBLE);

        MultipleImagesAdapter itemListDataAdapter = new MultipleImagesAdapter(mContext, allItemsInSection.getAllItemsInSection(), mIMultipleImageClickCallback,allItemsInSection);

        itemRowHolder.recycler_view_list.setHasFixedSize(true);
        itemRowHolder.recycler_view_list.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        itemRowHolder.recycler_view_list.setAdapter(itemListDataAdapter);
        itemRowHolder.recycler_view_list.setNestedScrollingEnabled(false);
    }

    @Override
    public int getItemCount() {
        return (null != dataList ? dataList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder {


        protected RecyclerView recycler_view_list;
        private TextView itemTitle;


        public ItemRowHolder(View view) {
            super(view);

            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);

        }
    }
}
