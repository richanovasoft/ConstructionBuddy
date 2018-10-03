package com.consturctionbuddy.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.consturctionbuddy.Adapter.CalendarAdapter;
import com.consturctionbuddy.Bean.Leafe;
import com.consturctionbuddy.Bean.LeavesBean;
import com.consturctionbuddy.R;
import com.consturctionbuddy.Utility.AppController;
import com.consturctionbuddy.Utility.Constant;
import com.consturctionbuddy.Utility.UIUtils;
import com.consturctionbuddy.Utility.UserUtils;
import com.consturctionbuddy.Utility.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class LeaveManagement extends Fragment {


    public GregorianCalendar month, itemmonth;// calendar instances.

    public CalendarAdapter adapter;// adapter instance
    // public Handler handler;// for grabbing some event values for showing the dot
    // marker.
    public ArrayList<String> items; // container to store calendar items which
    // needs showing the event marker
    ArrayList<String> event;
    LinearLayout rLayout;
    ArrayList<String> date;
    ArrayList<String> desc;

    private Context mContext;

    ArrayList<Leafe> values;

    private String strMonth, strYear;
    private View mMainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();
        mMainView = inflater.inflate(R.layout.fragment_leave_management, container, false);
        init();
        return mMainView;
    }

    private void init() {

        mContext = getActivity();
        values = new ArrayList<>();
        rLayout = (LinearLayout) mMainView.findViewById(R.id.text);
        month = (GregorianCalendar) GregorianCalendar.getInstance();


        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new CalendarAdapter(mContext, month);

        GridView gridview = (GridView) mMainView.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);


        String strCurrentdate = adapter.addCurrentDate();


        String[] separatedTime = strCurrentdate.split("-");
        strMonth = separatedTime[1];
        strYear = separatedTime[0];

        System.out.println("strCurrentdate = " + strCurrentdate);


        TextView title = (TextView) mMainView.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));

        RelativeLayout previous = (RelativeLayout) mMainView.findViewById(R.id.previous);

        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) mMainView.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {

                ((CalendarAdapter) parent.getAdapter()).setSelected(v);
                String selectedGridDate = CalendarAdapter.dayString
                        .get(position);

                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*", "");
                int gridvalue = Integer.parseInt(gridvalueString);

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
            }
        });


        setEvents();
    }

    private void setEvents() {

        startHttpRequestForAddEvents(strMonth, strYear);

    }

    private void startHttpRequestForAddEvents(final String strMonth, final String strYear) {

        boolean internetAvailable = Utils.isConnectingToInternet(mContext);
        if (internetAvailable) {
            String baseUrl = Constant.API_EVENT_LIST;
            StringRequest mStrRequest = new StringRequest(Request.Method.POST, baseUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {

                                Gson gson = new GsonBuilder().create();
                                JsonParser jsonParser = new JsonParser();
                                JsonObject jsonResp = jsonParser.parse(response).getAsJsonObject();
                                LeavesBean listResponseBean = gson.fromJson(jsonResp, LeavesBean.class);

                                if (listResponseBean != null && listResponseBean.getLeaves() != null && listResponseBean.getStatus() == 200) {

                                    setEventItemList(listResponseBean.getLeaves());
                                }


                            } catch (Exception e) {

                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getClass().equals(NoConnectionError.class)) {
                            } else {
                                UIUtils.showToast(mContext, getResources().getString(R.string.VolleyErrorMsg));
                            }
                        }
                    }) {
                @Override
                public Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("userid", UserUtils.getInstance().getUserID(mContext));
                    return params;
                }
            };
            mStrRequest.setShouldCache(false);
            mStrRequest.setTag("");
            AppController.getInstance().addToRequestQueue(mStrRequest);
            mStrRequest.setRetryPolicy(new DefaultRetryPolicy(
                    10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        } else {
            UIUtils.showToast(mContext, getResources().getString(R.string.InternetErrorMsg));
        }

    }

    private void setEventItemList(ArrayList<Leafe> leaves) {
        values = leaves;
    }


    protected void setNextMonth() {

        System.out.println(">>>>GregorianCalendar.MONTH = " + GregorianCalendar.MONTH);
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);

        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);

        }


        String selectedDate = adapter.addSelectedDate();
        String[] separatedTime = selectedDate.split("-");
        strMonth = separatedTime[1];
        strYear = separatedTime[0];

        startHttpRequestForAddEvents(strMonth, strYear);


    }

    protected void setPreviousMonth() {

        System.out.println(">>>>GregorianCalendar.MONTH = " + GregorianCalendar.MONTH);
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);

        }

        String selectedDate = adapter.addSelectedDate();

        String[] separatedTime = selectedDate.split("-");
        strMonth = separatedTime[1];
        strYear = separatedTime[0];

        startHttpRequestForAddEvents(strMonth, strYear);


    }


    public void refreshCalendar() {
        TextView title = (TextView) mMainView.findViewById(R.id.title);

        adapter.refreshDays();
        adapter.notifyDataSetChanged();
        title.setText(android.text.format.DateFormat.format("MMMM yyyy", month));
    }

}
