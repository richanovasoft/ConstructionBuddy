package com.consturctionbuddy.Bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class SectionDataModel implements Parcelable{


    private String headerTitle;
    private ArrayList<TimeLineImage> allItemsInSection;


    public SectionDataModel() {

    }

    public SectionDataModel(String headerTitle, ArrayList<TimeLineImage> allItemsInSection) {
        this.headerTitle = headerTitle;
        this.allItemsInSection = allItemsInSection;
    }


    protected SectionDataModel(Parcel in) {
        headerTitle = in.readString();
        allItemsInSection = in.createTypedArrayList(TimeLineImage.CREATOR);
    }

    public static final Creator<SectionDataModel> CREATOR = new Creator<SectionDataModel>() {
        @Override
        public SectionDataModel createFromParcel(Parcel in) {
            return new SectionDataModel(in);
        }

        @Override
        public SectionDataModel[] newArray(int size) {
            return new SectionDataModel[size];
        }
    };

    public String getHeaderTitle() {
        return headerTitle;
    }

    public void setHeaderTitle(String headerTitle) {
        this.headerTitle = headerTitle;
    }

    public ArrayList<TimeLineImage> getAllItemsInSection() {
        return allItemsInSection;
    }

    public void setAllItemsInSection(ArrayList<TimeLineImage> allItemsInSection) {
        this.allItemsInSection = allItemsInSection;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(headerTitle);
        dest.writeTypedList(allItemsInSection);
    }
}
