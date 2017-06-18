package com.minimalwaste.fypv2_fragments;


import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    //Initialise Variables and variables to store weight and timeStamp data
    BarChart barChart;
    String weightURL = "http://foodappee.azurewebsites.net/getWaste?id=8";
    List<String> timeList = new ArrayList<>();
    List<Integer> weightList = new ArrayList<>();

    //To parse Time
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
    DateFormat xAxis = new SimpleDateFormat("dd/MM");

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        //---------------FRAGMENT button onclick listener-------------------------------------------
        Button BFDWeek = (Button) view.findViewById(R.id.BFDweek);
        Button BFDMonth = (Button) view.findViewById(R.id.BFDmonth);
        Button BFDYear = (Button) view.findViewById(R.id.BFDyear);
        final TextView TVFD2 = (TextView) view.findViewById(R.id.TVFD2);
        TextView TVFDrecom = (TextView) view.findViewById(R.id.TVFDrecom);

        BFDWeek.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                weightURL = "http://foodappee.azurewebsites.net/getWaste?id=8";
                weightURL+="&days=7";
                getWeightData(weightURL);
                TVFD2.setText("Weekly Waste Weight");
            }
        });
        BFDMonth.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                weightURL = "http://foodappee.azurewebsites.net/getWaste?id=8";
                weightURL+="&days=30";
                getWeightData(weightURL);
                TVFD2.setText("Monthly Waste Weight");

            }
        });
        BFDYear.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                weightURL = "http://foodappee.azurewebsites.net/getWaste?id=8";
                weightURL+="&days=365";
                getWeightData(weightURL);
                TVFD2.setText("Yearly Waste Weight");
//                putDataIntoView(getView());       //Cannot put this here, coz takes some time to get JSON data

            }
        });

        //-------------------Initialise with data---------------------------------------------------
        getWeightData(weightURL+"&days=7");
        TVFD2.setText("Weekly Waste Weight");
        putDataIntoView(view);

        //------------------Recommendation----------------------------------------------------------
        String recommendations = "Congratulations, you have saved 5kg of waste compared to last week! \n";
        recommendations += "Please go to the vouchers page to claim your reward";
        TVFDrecom.setText(recommendations);

        return view;
    }

    private void getWeightData(String weightURL) {
        Log.d("Go 1st", "getWeightData: Entering");
        Ion.with(getActivity())     //This or GetActivity? getActivity for fragments
                .load(weightURL)
                .asString()
                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String data) {
                        Log.d("Data sent back", data);
                        processWeightData(data);
                        putDataIntoView(getView());         //Must Put this in here coz takes some time to get JSON data
                    }
                });
    }

    private void processWeightData(String data) {
        try {
            Log.d("Go 2nd", "processWeightData: Entering");
            timeList.clear();
            weightList.clear();

            JSONArray weightJSONArray = new JSONArray(data);
            for (int i = 0; i < weightJSONArray.length(); i++) {
                JSONObject weightJSON = weightJSONArray.getJSONObject(i);

                //-----Get TimeStamp Value-----
                String timeString= weightJSON.getString("Timestamp");
                //Parse it as a Date object
                Date timeDate = sdf.parse(timeString);
                String dates = xAxis.format(timeDate);
                timeList.add(dates);

                //-----Get Weight Amount-----
                Integer weightString = weightJSON.getInt("Weight");
                weightList.add(weightString);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void putDataIntoView(View view) {         //Duration: 0 week, 1 month, 2 year
        //=========================Bar Chart for Weight==============================
        Log.d("Go 3rd", "putDataIntoView: Entering");
        barChart = (BarChart) view.findViewById(R.id.barGraphWeight);

        //Form Bar chart entries (y axis values)
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < weightList.size(); i++) {
            barEntries.add(new BarEntry(weightList.get(i),i));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Dates");

        //Form bar chart (x axis values)
        ArrayList<String> theDates = new ArrayList<>();
        for (int j = 0; j < timeList.size(); j++) {
            theDates.add(timeList.get(j));
        }

        //Combine x and y axis data to form bar chart
        BarData theData = new BarData(theDates, barDataSet);
        barChart.setData(theData);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.animateXY(2000, 2000);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.setDescription("Waste Weight");
        barChart.setBackgroundColor(Color.rgb(232, 240, 247));
        barDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
    }


}

//Bar Chart using
//https://github.com/PhilJay/MPAndroidChart