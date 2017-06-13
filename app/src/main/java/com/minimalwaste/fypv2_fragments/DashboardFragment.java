package com.minimalwaste.fypv2_fragments;


import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DashboardFragment extends Fragment {

    //Initialise Variables
    BarChart barChart;

    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        //=========================Bar Chart for Volume==============================
        barChart = (BarChart) view.findViewById(R.id.barGraphVolume);
        ArrayList<BarEntry> barEntries2 = new ArrayList<>();
        barEntries2.add(new BarEntry(44f,0));
        barEntries2.add(new BarEntry(24f,1));
        barEntries2.add(new BarEntry(15f,2));
        barEntries2.add(new BarEntry(75f,3));
        barEntries2.add(new BarEntry(49f,4));

        BarDataSet barDataSet2 = new BarDataSet(barEntries2,"Dates");

        ArrayList<String> theDates2 = new ArrayList<>();
        theDates2.add("April");
        theDates2.add("May");
        theDates2.add("June");
        theDates2.add("July");
        theDates2.add("August");


        BarData theData2 = new BarData(theDates2,barDataSet2);
        barChart.setData(theData2);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.animateXY(2000, 2000);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.setDescription("Volume");
        barDataSet2.setColors(ColorTemplate.COLORFUL_COLORS);



        //=========================Bar Chart for Weight==============================
        barChart = (BarChart) view.findViewById(R.id.barGraphWeight);
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        barEntries.add(new BarEntry(12f,0));
        barEntries.add(new BarEntry(18f,1));
        barEntries.add(new BarEntry(25f,2));
        barEntries.add(new BarEntry(33f,3));
        barEntries.add(new BarEntry(60f,4));

        BarDataSet barDataSet = new BarDataSet(barEntries,"Dates");

        ArrayList<String> theDates = new ArrayList<>();
        theDates.add("April");
        theDates.add("May");
        theDates.add("June");
        theDates.add("July");
        theDates.add("August");


        BarData theData = new BarData(theDates,barDataSet);
        barChart.setData(theData);

        barChart.setTouchEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleEnabled(true);
        barChart.animateXY(2000, 2000);
        barChart.setHorizontalScrollBarEnabled(true);
        barChart.setDoubleTapToZoomEnabled(true);
        barChart.setDescription("Results");
        barChart.setBackgroundColor(114444);
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);

        return view;
    }


}

//Bar Chart using
//https://github.com/PhilJay/MPAndroidChart