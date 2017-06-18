package com.minimalwaste.fypv2_fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

    //-------------------For Horizontal Recipe
    String[] nameArray = {"Aglia Olio", "Meatball", "Sweet and Sour Port"};
    Integer[] imgArray = {R.drawable.agliaolio, R.drawable.meatball, R.drawable.sweetandsourpork};
    ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(imgArray));

//    RecyclerView mRecyclerView;
//    private RecyclerView.LayoutManager mLayoutManager;
//    private RecyclerView.Adapter mAdapter;

    //-------------------Vertical Recipe Need 2 arrays to store image and text------
    String urlString = "http://api.yummly.com/v1/api/recipes?_app_id=040ca89b&_app_key=b52b549137b851a8e6fe14e451c86218&requirePictures=true&maxResult=30&start=20";
    List<String> mImageURLS = new ArrayList<>();
    List<String> mRecipeNames = new ArrayList<>();
    List<String> mRecipeID = new ArrayList<>();
    List<String> mRating = new ArrayList<>();
    List<Integer> mType = new ArrayList<>();
    ListView listView;

    //-------------------SQLite Helper to get our Itemlist------
    ItemsSQLiteHelper itemsSQLiteHelper;
    String[] availableItems;


    public RecipeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //***Make Sure to have fragment Recipe!!!!!!!!!***** Not other fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);  //Getactivity instead of this in fragment

        //Initialise SQL
        itemsSQLiteHelper = new ItemsSQLiteHelper(getActivity(), null, null, 1);
//        =============================Add Personal Recipe===================================
//
//
//        //=============================For Horizontal List===================================
//        //Fragments always need view. for findViewByID
//
////        mDataset = new ArrayList<>();
////        for (int i = 0; i < 20; i++) {
////            mDataset.add("NewTitle # " + i);
////        }
//
//        mRecyclerView = (RecyclerView) view.findViewById(R.id.RVFR);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//
//        mAdapter = new HRecipeAdapter(arrayList);
//        mRecyclerView.setAdapter(mAdapter);
//
////        mRecyclerView.addOnItemTouchListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Intent intent = new Intent(getActivity(), HRecipeDetailActivity.class);
////                intent.putExtra("recipeName", nameArray[i]);
////                intent.putExtra("recipeIndex", imgArray[i]);
////                startActivity(intent);
////            }
////        });
////        mRecyclerView.setOnClickListener(new AdapterView.OnItemClickListener() {
////            @Override
////            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////            }
////        });
//
//        ==============================For OLD Vertical List=====================================
//
//        fetchData(urlString);
//
//        ListView listView = (ListView) view.findViewById(R.id.LVFRecipe);
//
//        MyAdapter myAdapter = new MyAdapter(getActivity(),recipeName,recipeIndex);////
//        listView.setAdapter(myAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(),RecipeDetailActivity.class);
//                intent.putExtra("recipeName", recipeName[i]);
//                intent.putExtra("recipeIndex", recipeIndex[i]);
//                startActivity(intent);
//            }
//        });

        //------On Item click for Add Items------
        Button addButton = (Button) view.findViewById(R.id.BFRadd);
        addButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(v.getContext(), AddRecipeActivity.class);
                startActivity(intent);
            }
        });


        //------On Click of Search Parameter-------------------------------------------------------
        Button searchButton = (Button) view.findViewById(R.id.BFRfind);
        final EditText searchParam = (EditText) view.findViewById(R.id.ETFRsearch);
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                urlString = "http://api.yummly.com/v1/api/recipes?_app_id=040ca89b&_app_key=b52b549137b851a8e6fe14e451c86218&requirePictures=true";
                urlString = urlString + "&q=" + searchParam.getText().toString();
                fetchData(urlString);
                listView = (ListView) getActivity().findViewById(R.id.LVFRecipe);   //getActivity instead of
                searchParam.setText("");    //Clear Search Area so can search for other
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });


        //==============================For NEW Vertical List=====================================

        availableItems = itemsSQLiteHelper.getItemList().split(",");

        for (int i = 0; i < availableItems.length; i++) {
            Log.d("Avaliable Items are", "onCreateView: Item" + availableItems[i]);
//            urlString += "&allowedIngredient[]="+availableItems[i];
            urlString += "&q=" + availableItems[i];
        }
        fetchData(urlString);
        listView = (ListView) view.findViewById(R.id.LVFRecipe);

        //==============================Return Everything=======================================
        return view;
    }


    private void fetchData(String urlString) {
        Ion.with(getActivity())     //This or GetActivity? getActivity for fragments
                .load(urlString)
                .asString()
                .setCallback(new FutureCallback<String>() {

                    @Override
                    public void onCompleted(Exception e, String data) {
                        Log.d("Data sent back", data);
                        processYummlyData(data);
                    }
                });
    }

    private void processYummlyData(String data) {
        try {
            //Clear list
            mImageURLS.clear();
            mRecipeNames.clear();
            mRecipeID.clear();
            mRating.clear();
            mType.clear();

            //--------------Put Personal Recipes inside here --------------------
            //Get data from Personal Recipe Database using PersonalRecipeSQLiteHelper
            PersonalRecipeSQLiteHelper PRdbHelper = new PersonalRecipeSQLiteHelper(getContext(), null, null, 1);
            List<PersonalRecipeC> personalRecipeCList = PRdbHelper.getDataByIngredient(availableItems);

            //Parse Class List to Arrays
            for (int i = 0; i < personalRecipeCList.size(); i++) {
                Log.d(TAG, "processYummlyData: Returned Recipe Name"+personalRecipeCList.get(i).getRecipeName());
                mRecipeID.add("");
                mRecipeNames.add(personalRecipeCList.get(i).getRecipeName());
                mImageURLS.add(personalRecipeCList.get(i).getImage());
                mRating.add(personalRecipeCList.get(i).getRating());
                mType.add(0);       //To signify it is my own recipe
            }

            //-------------Split up JSON String into JSON Objects to Extract VALUES from KEYS--------
            JSONObject json = new JSONObject(data);
            JSONArray mJsonArrayMatches = json.getJSONArray("matches");



            for (int i = 0; i < mJsonArrayMatches.length(); i++) {
                JSONObject mJsonObjectMatches = mJsonArrayMatches.getJSONObject(i);

                //-----Get Image Link-----
                JSONObject imageUrlsBySize = mJsonObjectMatches.getJSONObject("imageUrlsBySize");
                String ImageLink = imageUrlsBySize.getString("90");
                //process image data to get higher resolution images
                StringBuilder sb = new StringBuilder(ImageLink);
                sb.deleteCharAt(ImageLink.length() - 1);
                sb.deleteCharAt(ImageLink.length() - 2);
                sb.deleteCharAt(ImageLink.length() - 3);
                sb.deleteCharAt(ImageLink.length() - 4);
                ImageLink = sb.toString() + "360-c";
                mImageURLS.add(ImageLink);

                //-----Get Recipe Name
                String RecipeName = mJsonObjectMatches.getString("recipeName");
                mRecipeNames.add(RecipeName);

                String RecipeID = mJsonObjectMatches.getString("id");
                mRecipeID.add(RecipeID);

                //-----Parsing Rating number into Strings as images can only be string in Android-----
                String RatingInt = mJsonObjectMatches.getString("rating");
                String Rating;
                switch (RatingInt) {
                    case "1":
                        Rating = "one";
                        break;
                    case "2":
                        Rating = "two";
                        break;
                    case "3":
                        Rating = "three";
                        break;
                    case "4":
                        Rating = "four";
                        break;
                    case "5":
                        Rating = "five";
                        break;
                    default:
                        Rating = "five";
                }
                mRating.add(Rating);

                mType.add(1);       //1 = yummly Recipe and 0 = personal Recipe

            }


            //Convert from List to Arrays: recipe, Image, RecipeName, Rating, mType
            final String[] mRecipeIDArray = new String[mRecipeID.size()];       //ID For webpage only
            mRecipeID.toArray(mRecipeIDArray);
            final String[] mImageURLSArray = new String[mImageURLS.size()];
            mImageURLS.toArray(mImageURLSArray);
            final String[] mRecipeNamesArray = new String[mRecipeNames.size()];
            mRecipeNames.toArray(mRecipeNamesArray);
            String[] mRatingArray = new String[mRating.size()];
            mRating.toArray(mRatingArray);
            final Integer[] mTypeArray = new Integer[mType.size()];
            mType.toArray(mTypeArray);



            //--------------Put YUMMLY Recipe data into Adapter----------------------
            RecipeAdapter recipeAdapter = new RecipeAdapter(getActivity(), mImageURLSArray, mRecipeNamesArray, mRatingArray, mTypeArray);
            listView.setAdapter(recipeAdapter);

            //On List Click listener
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    //-----Goto Recipe page onClickListener-----
                    Intent intent = new Intent(getActivity(), RecipeDetailActivity.class);
                    intent.putExtra("recipeName", mRecipeNamesArray[i]);
                    intent.putExtra("recipeID", mRecipeIDArray[i]);
                    intent.putExtra("recipeIndex", mImageURLSArray[i]);
                    startActivity(intent);
                }
            });

        } catch (JSONException e) {
            Log.wtf("json Exception ", e);
        }

    }

}
