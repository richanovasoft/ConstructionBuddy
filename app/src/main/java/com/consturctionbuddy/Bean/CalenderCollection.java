package com.consturctionbuddy.Bean;

import java.util.ArrayList;

public class CalenderCollection {
    public String date = "";
    public String event_message = "";

    public static ArrayList<CalenderCollection> date_collection_arr;

    public CalenderCollection(String date, String event_message) {

        this.date = date;
        this.event_message = event_message;

    }

}