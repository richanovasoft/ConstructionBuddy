package com.consturctionbuddy.Interface;


import com.consturctionbuddy.Bean.SectionDataModel;
import com.consturctionbuddy.Bean.TimeLine.ProjectImg;

public interface IMultipleImageClickCallback {

    public void itemClicked(int aIndex, ProjectImg aTimeLineImage);
    public void itemOverFlow(int aIndex,ProjectImg aTimeLineImage);
}
