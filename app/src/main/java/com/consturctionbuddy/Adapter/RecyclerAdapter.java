package com.consturctionbuddy.Adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.consturctionbuddy.R;
import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

/**
 * Created by sunil on 12/23/16.
 */

public class RecyclerAdapter extends ExpandableRecyclerViewAdapter<RecyclerAdapter.TitleViewHolder, RecyclerAdapter.SubTitleViewHolder> {

    private Context context;
    private ItemClickChild mListener;
    public RecyclerAdapter(Context context, List<? extends ExpandableGroup> groups, Activity activity) {
        super(groups);
        this.context = context;
        mListener = (ItemClickChild) activity;
    }

    @Override
    public TitleViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_title, parent, false);
        return new TitleViewHolder(view);
    }

    @Override
    public SubTitleViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_subtitle, parent, false);
        return new SubTitleViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(SubTitleViewHolder holder, int flatPosition,
                                      ExpandableGroup group, final int childIndex) {

        final SubTitle subTitle = ((TitleMenu) group).getItems().get(childIndex);
        holder.setSubTitletName(subTitle.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onChildClick(childIndex);
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(TitleViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGenreTitle(context, group);
    }

    public interface ItemClickChild{
        void onChildClick(int position);
    }


    public class TitleViewHolder extends GroupViewHolder {

        private TextView titleName;
        private ImageView arrow;

        public TitleViewHolder(View itemView) {
            super(itemView);
            titleName = (TextView) itemView.findViewById(R.id.list_item_name);
            arrow = (ImageView) itemView.findViewById(R.id.list_item_arrow);
        }

        public void setGenreTitle(Context context, ExpandableGroup title) {
            if (title instanceof TitleMenu) {
                titleName.setText(title.getTitle());
                if (((TitleMenu) title).getImageUrl() != null && !((TitleMenu) title).getImageUrl().isEmpty()) {


                }
            }
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }
    }

    public static class TitleMenu extends ExpandableGroup<SubTitle> {

        private String imageUrl;

        public TitleMenu(String title, List<SubTitle> items, String imageUrl) {
            super(title, items);
            this.imageUrl = imageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }

    public static class SubTitle implements Parcelable {

        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public SubTitle(String name) {
            this.name = name;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
        }

        protected SubTitle(Parcel in) {
            this.name = in.readString();
        }

        public final Parcelable.Creator<SubTitle> CREATOR = new Parcelable.Creator<SubTitle>() {
            @Override
            public SubTitle createFromParcel(Parcel source) {
                return new SubTitle(source);
            }

            @Override
            public SubTitle[] newArray(int size) {
                return new SubTitle[size];
            }
        };
    }


    public class SubTitleViewHolder extends ChildViewHolder {

        private TextView subTitleTextView;

        public SubTitleViewHolder(View itemView) {
            super(itemView);
            subTitleTextView = (TextView) itemView.findViewById(R.id.subtitle);
        }

        public void setSubTitletName(String name) {
            subTitleTextView.setText(name);
        }
    }
}