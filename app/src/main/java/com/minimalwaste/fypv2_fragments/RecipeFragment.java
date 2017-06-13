package com.minimalwaste.fypv2_fragments;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.async.http.body.JSONArrayBody;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment {

//    String[] recipeName = {"bread","milk","butter","chicken","biscuit","jam","sausage","coke"};
//    int[] recipeIndex = {R.drawable.bread,R.drawable.milk,R.drawable.butter,R.drawable.chicken,
//            R.drawable.biscuit,R.drawable.jam,R.drawable.sausage,R.drawable.coke,};

    //-------------------For Horizontal Recipe
//    private ArrayList<String> mDataset;
    Integer[] array = {R.drawable.agliaolio,R.drawable.meatball,R.drawable.sweetandsourpork};
    ArrayList<Integer> arrayList = new ArrayList<>(Arrays.asList(array));

//    testing.add(R.drawab)
//    testing.add(R.drawable.agliaolio);
//    ,R.drawable.meatball,R.drawable.sweetandsourpork);

    RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView.Adapter mAdapter;

    //-------------------Vertical Recipe Need 2 arrays to store image and text
    String urlString = "http://api.yummly.com/v1/api/recipes?_app_id=040ca89b&_app_key=b52b549137b851a8e6fe14e451c86218";
    List<String> mImageURLS = new ArrayList<>();
    List<String> mRecipeNames = new ArrayList<>();
    List<String> mRecipeID = new ArrayList<>();
    List<String> mRating = new ArrayList<>();
    ListView listView;


    public RecipeFragment() {
        // Required empty public constructor
    }

//    public void BFRAdd(View view) {
//        Intent intent = new Intent(getActivity(),AddRecipeActivity.class);
//        startActivity(intent);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //***Make Sure to have fragment Recipe!!!!!!!!!***** Not other fragment
        View view = inflater.inflate(R.layout.fragment_recipe, container, false);


        //=============================Add Personal Recipe===================================


        //=============================For Horizontal List===================================
        //Fragments always need view. for findViewByID

//        mDataset = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            mDataset.add("NewTitle # " + i);
//        }

        mRecyclerView = (RecyclerView) view.findViewById(R.id.RVFR);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new HRecipeAdapter(arrayList);
        mRecyclerView.setAdapter(mAdapter);

//        mRecyclerView.addOnItemTouchListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent intent = new Intent(getActivity(),RecipeDetailActivity.class);
//                intent.putExtra("recipeName", recipeName[i]);
//                intent.putExtra("recipeIndex", recipeIndex[i]);
//                startActivity(intent);
//            }
//        });
//        mRecyclerView.setOnClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }
//        });

        //==============================For OLD Vertical List=====================================

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

        //==============================For NEW Vertical List=====================================

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
                        for (int i = 0; i < mImageURLS.size(); i++) {
                            Log.d("After processYummlyData", "onCompleted: " + mImageURLS.get(i));
                        }
                    }
                });
    }

    private void processYummlyData(String data) {
        try {

            //Split up JSON String into Individual Arrays
            JSONObject json = new JSONObject(data);
//            JSONObject Criteria = json.getJSONObject("criteria");
//            String allowedIngredient = Criteria.getString("allowedIngredient");

            JSONArray mJsonArrayMatches = json.getJSONArray("matches");
            for (int i = 0; i < mJsonArrayMatches.length(); i++) {
                JSONObject mJsonObjectMatches = mJsonArrayMatches.getJSONObject(i);

                //Get Image Link
                JSONObject imageUrlsBySize = mJsonObjectMatches.getJSONObject("imageUrlsBySize");
                String ImageLink = imageUrlsBySize.getString("90");
                mImageURLS.add(ImageLink);

                //Get Recipe Name
                String RecipeName = mJsonObjectMatches.getString("recipeName");
                mRecipeNames.add(RecipeName);

                String RecipeID = mJsonObjectMatches.getString("id");
                mRecipeID.add(RecipeID);

                String Rating = mJsonObjectMatches.getString("rating");
                mRating.add(Rating);

            }

            final String[] mImageURLSArray = new String[mImageURLS.size()];
            mImageURLS.toArray(mImageURLSArray);
            final String[] mRecipeNamesArray = new String[mRecipeNames.size()];
            mRecipeNames.toArray(mRecipeNamesArray);
            final String[] mRecipeIDArray = new String[mRecipeID.size()];
            mRecipeID.toArray(mRecipeIDArray);
            String[] mRatingArray = new String[mRating.size()];
            mRating.toArray(mRatingArray);

//            RecipeAdapter recipeAdapter = new RecipeAdapter(getActivity(),mImageURLSArray,mRecipeNamesArray, mRecipeIDArray, mRatingArray);
            RecipeAdapter recipeAdapter = new RecipeAdapter(getActivity(),mImageURLSArray,mRecipeNamesArray, mRatingArray);

            listView.setAdapter(recipeAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(),RecipeDetailActivity.class);

//                String url = "http://www.yummly.com/recipe/" + mRecipeIDArray[i];

//                Intent intent = new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse(url));
//                startActivity(intent);

                intent.putExtra("recipeName", mRecipeNamesArray[i]);
                intent.putExtra("recipeID", mRecipeIDArray[i]);
                intent.putExtra("recipeIndex", mImageURLSArray[i]);
                startActivity(intent);
            }
        });

        } catch (JSONException e) {
            Log.wtf("json Exception ",e);
        }

    }

}
