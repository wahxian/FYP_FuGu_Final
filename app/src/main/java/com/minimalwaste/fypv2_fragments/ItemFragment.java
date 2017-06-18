package com.minimalwaste.fypv2_fragments;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RemoteViews;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;



import static android.content.Context.NOTIFICATION_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {

    //-----Get Current Date and date formatter-----
    String currentDate = "";
//    DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");
    Date DcurrentDate;
//    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss");

    //Initialise the Arrays to put Data into (Using ArrayList) -> Dynamic can add as we go
    List<String> Item = new ArrayList<>();
    List<String> inFridge = new ArrayList<>();
    List<String> Expiry = new ArrayList<>();
    List<String> Price = new ArrayList<>();
    String[] parts;

    String itemURL = "http://foodappee.azurewebsites.net/GetItems?id=7";

    //---------Define SQL helper
    ItemsSQLiteHelper itemsDBhelper;

    //---------Notification
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;
    private int notification_id;
    private RemoteViews remoteViews;
    private Context context;


    public ItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        //------------2nd Way of Date https://stackoverflow.com/questions/5369682/get-current-time-and-date-on-android
//        currentDate = df.format(Calendar.getInstance().getTime());    //Used to format Date into a String according to DateFormat set by SimpleDateFormat
        DcurrentDate = Calendar.getInstance().getTime();
        Log.d("Current date is", "onCreateView: Date is " + currentDate);

        //------------Parse the data from file to Custom ItemListView
        fetchJSONitems(itemURL);

        //------------SQL initialisation to keep Available items information-----------------------
        itemsDBhelper = new ItemsSQLiteHelper(getActivity(), null, null, 1);

        //------------Notification SETUP https://github.com/miskoajkula/Custom_Notification/tree/master/app/src/main/java/app/custom_notification
        context = getActivity();
        notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(getContext());

        remoteViews = new RemoteViews("com.minimalwaste.fypv2_fragments",R.layout.custom_notification);



        return view;

    }

    //**********************************************************************************************
    //               Fetch Item List in JSON and parse it to form ListView                         *
    //**********************************************************************************************
    private void fetchJSONitems(String itemURL) {
        Ion.with(getActivity())     //This or GetActivity? getActivity for fragments
                .load(itemURL)
                .asString()
                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String data) {
                        Log.d("Data sent back", data);
                        processItemsData(data);
                    }
                });
    }

    private void processItemsData(String data) {
        try {
            //--Clear ArrayList Every refresh to let new JSON data get in--
            Item.clear();
            inFridge.clear();
            Expiry.clear();
            Price.clear();

            //--Create JSONObject to store the whole JSON Object (within {})
            JSONObject json = new JSONObject(data);

            //--Create JSONArray to store arrays of items (within [])
            JSONArray jsonItemsArray = json.getJSONArray("FoodItems");

            //--Parse JSONArray to get individual food items--
            for (int i = 0; i < jsonItemsArray.length(); i++) {
                JSONObject ijsonItem = jsonItemsArray.getJSONObject(i);

                //Get Product Name
                String iName = ijsonItem.getString("ProductName");
                Item.add(iName);

                //Get Price of Item
                String iPrice = "£" + ijsonItem.getString("Price") + "0";
                Price.add(iPrice);

                //Get Expiry date
                String iDate = ijsonItem.getString("Expiry");
                //Processing to derive days remaining***************************************************************************************************************************
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
                Date DexpiryDate = sdf.parse(iDate);
                long diff =  DexpiryDate.getTime() - DcurrentDate.getTime();
                int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
                String days = Integer.toString(numOfDays);
                Expiry.add(days);

                //Get inFridge Data
                String iinFridge = ijsonItem.getString("inFridge");
                if (iinFridge == "true") {
                    iinFridge = "Inside Fridge";
                }
                inFridge.add(iinFridge);
            }

            //Convert arraylist to arrays
            final String[] ItemArray = new String[Item.size()];     //Arrays length(), ArrayList size()
            Item.toArray(ItemArray);
            final String[] PriceArray = new String[Price.size()];
            Price.toArray(PriceArray);
            final String[] DateArray = new String[Expiry.size()];
            Expiry.toArray(DateArray);
            final String[] inFridgeArray = new String[inFridge.size()];
            inFridge.toArray(inFridgeArray);

            //----put them into ListView using Adapter
            ListView listView = (ListView) getActivity().findViewById(R.id.LVFI);
            ItemAdapter itemAdapter = new ItemAdapter(getActivity(), ItemArray, inFridgeArray, DateArray, PriceArray);  //inFridge Replace with Brand
            listView.setAdapter(itemAdapter);

            //-----------------When refreshed, delete old database and replace with new one---------
            itemsDBhelper.deleteDB();
            itemsDBhelper.addItems(ItemArray,inFridgeArray,DateArray,PriceArray);
            Log.d("After DB", "Added Items: " + itemsDBhelper.getItemList());


            //------------------Show Notification if item about to expire---------------------------
            for (int i = 0; i < ItemArray.length; i++) {
                if(Integer.parseInt(DateArray[i])>=0 && Integer.parseInt(DateArray[i])<=5){
                    int id = getContext().getResources().getIdentifier("com.minimalwaste.fypv2_fragments:drawable/" + ItemArray[i].toLowerCase(), null, null);
                    remoteViews.setImageViewResource(R.id.notif_icon,id);
                    remoteViews.setTextViewText(R.id.notif_title,ItemArray[i]+" is expiring in " + DateArray[i] + " days.");

                    notification_id = (int) System.currentTimeMillis();

                    Intent button_intent = new Intent("button_click");
                    button_intent.putExtra("id",notification_id);
                    PendingIntent button_pending_event = PendingIntent.getBroadcast(getContext(),notification_id,
                            button_intent,0);

                    remoteViews.setOnClickPendingIntent(R.id.button,button_pending_event);

                    Intent notification_intent = new Intent(context,ItemsActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);

                    builder.setSmallIcon(R.mipmap.ic_launcher)
                            .setAutoCancel(true)
                            .setCustomContentView(remoteViews)
                            .setContentIntent(pendingIntent);

                    notificationManager.notify(notification_id,builder.build());

                }
            }

            //OnClick of Items in List
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //-----Goto Recipe page onClickListener-----
                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
//                    intent.putExtra("recipeName", mRecipeNamesArray[i]);
//                    intent.putExtra("recipeID", mRecipeIDArray[i]);
//                    intent.putExtra("recipeIndex", mImageURLSArray[i]);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.d("NOOB", "processItemsData: Failed to get Data");
        }
    }
}

/**
 *
 */

//--1st way og Get calendar date
//            Calendar c = Calendar.getInstance();
//            date = c.get(Calendar.DATE);
//            Log.d("Current date is", "onCreateView: Date is" +date);

//--------------------------------------------------------------------------------------------------
//------------------Show Notification if item about to expire---------------------------
//            remoteViews.setImageViewResource(R.id.notif_icon,R.mipmap.ic_launcher);
//                    remoteViews.setTextViewText(R.id.notif_title,"TEXT");
//                    remoteViews.setProgressBar(R.id.progressBar,100,40,true);
//
//
//                    getView().findViewById(R.id.button_show_notif).setOnClickListener(new View.OnClickListener() {
//@Override
//public void onClick(View view) {
//
//        notification_id = (int) System.currentTimeMillis();
//
//        Intent button_intent = new Intent("button_click");
//        button_intent.putExtra("id",notification_id);
//        PendingIntent button_pending_event = PendingIntent.getBroadcast(context,notification_id,
//        button_intent,0);
//
//        remoteViews.setOnClickPendingIntent(R.id.button,button_pending_event);
//
//        Intent notification_intent = new Intent(context,ItemsActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,notification_intent,0);
//
//        builder.setSmallIcon(R.mipmap.ic_launcher)
//        .setAutoCancel(true)
//        .setCustomBigContentView(remoteViews)
//        .setContentIntent(pendingIntent);
//
//        notificationManager.notify(notification_id,builder.build());
//
//
//        }
//        });