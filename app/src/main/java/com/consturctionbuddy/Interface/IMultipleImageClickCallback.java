package com.consturctionbuddy.Interface;


import com.consturctionbuddy.Bean.SectionDataModel;
import com.consturctionbuddy.Bean.TimeLine.Datum;
import com.consturctionbuddy.Bean.TimeLine.ProjectImg;

public interface IMultipleImageClickCallback {

    public void itemClicked(int aIndex, Datum aTimeLineImage);
    public void itemOverFlow(int aIndex,Datum aTimeLineImage);
}
