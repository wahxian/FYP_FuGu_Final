package com.minimalwaste.fypv2_fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.StringTokenizer;


/**
 * A simple {@link Fragment} subclass.
 */
public class ItemFragment extends Fragment {

    //Initialise the Arrays to put Data into (Using Array) -> Not Dynamic cannot change size
//    String[] Item;
//    String[] Brand;
//    String[] Date;
//    String[] Price;

    //Initialise the Arrays to put Data into (Using ArrayList) -> Dynamic can add as we go
    List<String> Item = new ArrayList<>();
    List<String> Brand = new ArrayList<>();
    List<String> Date = new ArrayList<>();
    List<String> Price = new ArrayList<>();
    String[] parts;

    public ItemFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        //Parse the data from file to Custom ItemListView
        FileToList(view);

        return view;

    }

    private void FileToList(View view) {

        //Read the Words from the file and put them into ArrayList
        Item.add("Item");
        Brand.add("Brand");
        Date.add("Date");
        Price.add("Price");
        ReadWordsFromFile();

        //-------------Converting ArrayList back to String Arrays--------------
        String[] itemArray = new String[ Item.size() ];
        Item.toArray( itemArray );
        String[] brandArray = new String[ Brand.size() ];
        Brand.toArray( brandArray );
        String[] dateArray = new String[ Date.size() ];
        Date.toArray( dateArray );
        String[] priceArray = new String[ Price.size() ];
        Price.toArray( priceArray );

        for (int i = 0; i < itemArray.length; i++) {
            Log.d("Inside ItemFragment", "itemArray: "+itemArray[i]);
        }

        ListView listView = (ListView) view.findViewById(R.id.LVFI);
        ItemAdapter itemAdapter = new ItemAdapter(getActivity(),itemArray,brandArray,dateArray,priceArray);

        listView.setAdapter(itemAdapter);

    }


    private void ReadWordsFromFile() {
    /*Scans throughout the text file and gets the word out and puts it into an array*/
        Scanner scan = new Scanner(getResources().openRawResource(R.raw.groceries));
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            parts = null;
            parts = line.split("\t");
            if(parts.length==4) {
                Item.add(parts[0]);
                Brand.add(parts[1]);
                Date.add(parts[2]);
                Price.add(parts[3]);
            }
        }
    }
}
