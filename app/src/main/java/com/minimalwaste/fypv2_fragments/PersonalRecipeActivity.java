package com.minimalwaste.fypv2_fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PersonalRecipeActivity extends AppCompatActivity {

    PersonalRecipeSQLiteHelper dbHandler;

    //Lists to keep track of data from sql
    List<String> RecipeName = new ArrayList<>();
    List<String> Image = new ArrayList<>();
    List<String> Rating = new ArrayList<>();
    List<List<String>> Ingredients = new ArrayList<>();
    List<Integer> TypeList = new ArrayList<>();

    ImageView IVAPRtest;
    ListView LVAPR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_recipe);

        getSupportActionBar().setTitle("Personal Recipe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LVAPR = (ListView) findViewById(R.id.LVAPR);

        //-----------------------------------------------------------------------------------
        dbHandler = new PersonalRecipeSQLiteHelper(this, null, null, 1);

        //-------------------------Get JSON string from helper---------------------------------
        try {
            String data = dbHandler.getDBToJSON();
            parsePersonalRecipeJSON(data);
            Log.d("Image", "AFTER PARSING");

        } catch (Exception e) {
            Log.d("Database error", "onCreate: Failed to get classList from DATABASE");
        }

    }


    //********************************************************************************************//
    //------------------Parse JSON String to components and put into listview-----------------------
    //********************************************************************************************//
    private void parsePersonalRecipeJSON(String data) {
        try {
            JSONObject json = new JSONObject(data);
            JSONArray jsonArrayRecipe = json.getJSONArray("Recipe");

            for (int i = 0; i < jsonArrayRecipe.length(); i++) {
                //Index i of jsonArray
                Log.d("ArrayLength", "parsePersonalRecipeJSON: " + jsonArrayRecipe.length());
                JSONObject iRecipe = jsonArrayRecipe.getJSONObject(i);
//
                //Get recipe name
                String recipeName = iRecipe.getString("RecipeName");
                RecipeName.add(recipeName);

                //Get Image blob
                String imageString = iRecipe.getString("Image");
                Image.add(imageString);
//
//                //Get Ratings
                String rating = iRecipe.getString("Rating");
                Rating.add(rating);

                //Get Ingredients
                List<String> ingredients = new ArrayList<>();
                JSONArray ingredientsArray = iRecipe.getJSONArray("Ingredients");
                for (int j = 0; j < ingredientsArray.length(); j++) {
                    ingredients.add(ingredientsArray.getString(j));
                }
                Ingredients.add(ingredients);

                TypeList.add(0);


//
            }

            //--RecipeAdapter(@NonNull Context context, String[] recipeImage, String[] recipeNames, String[] recipeRating)

            //Convert from List to Arrays to put into Adapter
            final String[] recipeNameArray = new String[RecipeName.size()];
            RecipeName.toArray(recipeNameArray);
            final String[] imageArray = new String[Image.size()];
            Image.toArray(imageArray);
            final String[] ratingArray = new String[Rating.size()];
            Rating.toArray(ratingArray);
            final Integer[] typeArray = new Integer[TypeList.size()];
            TypeList.toArray(typeArray);

            //Adapt ListView to the model we want and past it back into ListView
            RecipeAdapter recipeAdapter = new RecipeAdapter(this, imageArray, recipeNameArray, ratingArray, typeArray);
            LVAPR.setAdapter(recipeAdapter);

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Falied to parse JSON", "parsePersonalRecipeJSON: Faile");
        }
    }

    /**
     *-------------------------DEFAULT--------------------------------------------------------------
     */

    //******************************************************//
    //  Go Back to main page when back button is pressed    //
    //******************************************************//
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, ItemsActivity.class);
                intent.putExtra("LoginResult", "backFromLogin");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


/**
 * ################################################################################################
 */

//-----------------------Test Image from intent------------------------------------------

//        String base64Image = getIntent().getStringExtra("ImageIn64");
//        try {
//            //Decode base 64 string Image to bitmap to bitmap image
//            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
//            Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
//                    decodedString.length);
//
//            IVAPRtest.setImageBitmap(base64Bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//---------------------Get personalRecipe Class from helper---------------------------------
//    private void printDBdata(List<PersonalRecipeC> data) {
//        for (int i = 0; i < data.size(); i++) {
//            PersonalRecipeC ipersonalRecipeC = data.get(i);
//
//            //Extract information out
//            String iimage = ipersonalRecipeC.getImage();
//            byte[] decodedString = Base64.decode(iimage, Base64.DEFAULT);
//            Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
//                    decodedString.length);
//            IVAPRtest.setImageBitmap(base64Bitmap);
//
//            ipersonalRecipeC.getIngredients();
//            ipersonalRecipeC.getRating();
//            ipersonalRecipeC.getRecipeName();
//
//        }
//    }