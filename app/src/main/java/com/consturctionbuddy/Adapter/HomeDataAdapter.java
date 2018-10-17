package com.consturctionbuddy.Adapter;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.consturctionbuddy.Bean.TimeLine.Datum;
import com.consturctionbuddy.Interface.IMultipleImageClickCallback;
import com.consturctionbuddy.R;
import com.consturctionbuddy.custom.CustomBoldTextView;
import com.consturctionbuddy.custom.CustomRegularTextView;

import java.util.ArrayList;

public class HomeDataAdapter extends RecyclerView.Adapter<HomeDataAdapter.ItemRowHolder> {

    private ArrayList<Datum> dataList;
    private Context mContext;
    private IMultipleImageClickCallback mIMultipleImageClickCallback;
    private boolean isDescExpanded;


    public HomeDataAdapter(Context context, ArrayList<Datum> dataList, IMultipleImageClickCallback aIMultipleImageClickCallback) {
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
    public void onBindViewHolder(final ItemRowHolder itemRowHolder, int i) {


        final Datum allItemsInSection = dataList.get(i);
        String sectionName = dataList.get(i).getDatetime();
        itemRowHolder.itemTitle.setText(sectionName);
        itemRowHolder.itemTitle.setVisibility(View.VISIBLE);

        itemRowHolder.tv_image_date.setVisibility(View.GONE);

        itemRowHolder.tv_image_desc.setText(allItemsInSection.getTodayWorkDescr());
        itemRowHolder.tv_image_title.setText(allItemsInSection.getTodayWorkSubject());

        itemRowHolder.tv_image_desc.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (!isDescExpanded) {
                    isDescExpanded = false;
                    if (itemRowHolder.tv_image_desc.getLineCount() > 4) {
                        itemRowHolder.tv_read_more.setVisibility(View.VISIBLE);
                        ObjectAnimator animation = ObjectAnimator.ofInt(itemRowHolder.tv_image_desc, "maxLines", 4);
                        animation.setDuration(0).start();
                    }
                }
            }
        });

        itemRowHolder.tv_read_more.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                final Dialog dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.show_more_dialog);

                CustomRegularTextView text = dialog.findViewById(R.id.text_dialog);
                text.setText(allItemsInSection.getTodayWorkDescr());

                Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
                dialogButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });

        MultipleImagesAdapter itemListDataAdapter = new MultipleImagesAdapter(mContext, allItemsInSection.getProjectImg(), mIMultipleImageClickCallback);

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

        private CustomBoldTextView tv_image_date;
        private CustomBoldTextView tv_image_title;
        private CustomRegularTextView tv_image_desc;
        private CustomRegularTextView tv_read_more;


        public ItemRowHolder(View view) {
            super(view);

            this.recycler_view_list = (RecyclerView) view.findViewById(R.id.recycler_view_list);
            this.itemTitle = (TextView) view.findViewById(R.id.itemTitle);
            tv_image_date = view.findViewById(R.id.tv_image_date);
            tv_image_desc = view.findViewById(R.id.tv_image_desc);
            tv_image_title = view.findViewById(R.id.tv_image_title);
            tv_read_more = view.findViewById(R.id.tv_read_more);
        }
    }
}
