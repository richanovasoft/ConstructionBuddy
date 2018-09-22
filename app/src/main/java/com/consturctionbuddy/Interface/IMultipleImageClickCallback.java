package com.consturctionbuddy.Interface;


import com.consturctionbuddy.Bean.SectionDataModel;

public interface IMultipleImageClickCallback {

    public void itemClicked(int aIndex, SectionDataModel aTimeLineImage);
    public void itemOverFlow(int aIndex,SectionDataModel aTimeLineImage);
}
