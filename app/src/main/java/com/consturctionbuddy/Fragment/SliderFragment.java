package com.consturctionbuddy.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.UIUtils;
import com.squareup.picasso.Picasso;

import uk.co.senab.photoview.PhotoView;


public class SliderFragment extends Fragment {

    private PhotoView mImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.slider_one, container, false);

        try {
            mImageView = view.findViewById(R.id.iv_photo);
            try {

                String url = getArguments().getString("fullImageUrl");
                String newPathStr = "";

                mImageView.setZoomable(false);

                if (url != null) {
                    String imagePath = url.trim();
                    newPathStr = imagePath.replaceAll(" ", "%20");

                    Picasso.with(getActivity())
                            .load(newPathStr).into(mImageView, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            if (mImageView != null) {
                                mImageView.setZoomable(true);
                            }
                        }

                        @Override
                        public void onError() {
                        }

                    });
                } else {
                    System.out.println("newPathStr = " + newPathStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
            UIUtils.showToast(getActivity(), "Out of Memory");
        }
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cleanup();
    }

    public void cleanup() {
        if (mImageView != null) {
            Picasso.with(mImageView.getContext())
                    .cancelRequest(mImageView);
            mImageView.setImageDrawable(null);
        }
    }
}
