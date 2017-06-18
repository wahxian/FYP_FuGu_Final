package com.minimalwaste.fypv2_fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.json.JSONStringer;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by Wah Xian on 16/06/2017.
 */

public class PersonalRecipeSQLiteHelper extends SQLiteOpenHelper {
    //**********************************************************************************************
    //1. Declare Database information: Version, DBName, TableName within DB, ColumnsNames.
    //**********************************************************************************************

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PersonalRecipeDB.db";
    public static final String TABLE_PERSONAL_RECIPE = "PersonalRecipe";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "recipename";
    public static final String COLUMN_RATING = "rating";
    public static final String COLUMN_INGREDIENT = "ingredient";
    public static final String COLUMN_IMAGE = "image";

    //**********************************************************************************************
    //2. Create assigned constructors and pass info to SQLiteOpenHelper superclass
    //**********************************************************************************************

    public PersonalRecipeSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);     //Define default by changing so that when creating object can type null
    }

    //Constructing in Fragments:
//    PersonalRecipeSQLiteHelper PRdbHelper = new PersonalRecipeSQLiteHelper(getContext, null, null, 1);

    //**********************************************************************************************
    //3. OnCreate method, create table
    //**********************************************************************************************

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_PERSONAL_RECIPE + "(" +          //remember to leave spaces
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_RECIPE_NAME + " TEXT, " +
                COLUMN_RATING + " TEXT, " +
                COLUMN_INGREDIENT + " TEXT, " +
                COLUMN_IMAGE + " TEXT);";   //Remember to end SQL query with ;
        Log.d("CreateQuery", "onCreate: create Query" + query);
        db.execSQL(query);
    }

    //**********************************************************************************************
    //4. onUpgrade table: Upgrade table for version change
    //**********************************************************************************************

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PERSONAL_RECIPE);
        onCreate(db);
    }

    //**********************************************************************************************
    //5. Add<Class> function: to parse PersonalRecipeC class and insert it into database
    //**********************************************************************************************

    public void addPersonalRecipeC(PersonalRecipeC personalRecipeC) {
        //Define ContentValue for us to insert multiple columns at a time
        ContentValues values = new ContentValues();

        //Put Values from class into ContentValue   ---- Following 1NF --------
        int noIngredients = personalRecipeC.getIngredients().size();
        for (int i = 0; i < noIngredients; i++) {
            //id Autoincrements, put things into database
            values.put(COLUMN_RECIPE_NAME, personalRecipeC.getRecipeName());
            values.put(COLUMN_IMAGE, personalRecipeC.getImage());
            values.put(COLUMN_INGREDIENT, personalRecipeC.getIngredients().get(i));  //Returns an Arraylist
            values.put(COLUMN_RATING, personalRecipeC.getRating());
        }

        //Get Writable Database and put in
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_PERSONAL_RECIPE, null, values);
        db.close();

    }

    //**********************************************************************************************
    //6. Delete<Class> function:
    //**********************************************************************************************

    public void deletePersonalRecipeC(String recipeName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_PERSONAL_RECIPE + " WHERE " + COLUMN_RECIPE_NAME + "=\"" + recipeName + "\";");
    }



    //**********************************************************************************************
    //7. GetDataFromSQLite function: As a string <JSON Good>    ************************************
    //**********************************************************************************************

    public String getDBToJSON() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERSONAL_RECIPE;

        String JSONstring = "{\"Recipe\":[";
        String previousRecipe = "";
        int first = 0;

        //Form Cursor to start at top to table position
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        Log.d("Outside", "getDBToJSON: Outside While loop");


        //Parse Database to JSON Message
        while (!cursor.isAfterLast()) {
            Log.d("Enter", "getDBToJSON: Enter While loop");
            String currentRecipe = cursor.getString(cursor.getColumnIndex("recipename"));
            Log.d("RecipeName", "getDBToJSON: inside While loop: " + currentRecipe);

            if (currentRecipe != null) {
                if (!currentRecipe.equals(previousRecipe)) {
//                    //If not the first time then need to end Ingredients first then only next
//                    JSONstring += "]},";
                    if (first != 0) {
                        JSONstring += "]},";
                    }
//
//                    //Add whole ending and starting JSON String
                    JSONstring += "{\"RecipeName\":" + "\"" + currentRecipe + "\"" +
                            ",\"Image\":" + "\"" + cursor.getString(cursor.getColumnIndex("image")) + "\"" +
                            ",\"Rating\":" + "\"" + cursor.getString(cursor.getColumnIndex("rating")) + "\"" +
                            ",\"Ingredients\":[" + "\"" + cursor.getString(cursor.getColumnIndex("ingredient")) + "\"";
//
                    if (cursor.isLast()) {
                        JSONstring += "]}]}";
                    }

                } else {        //Current Recipe equals previous recipe
                    //Add JSON ingredients
                    JSONstring += "," + "\"" + cursor.getString(cursor.getColumnIndex("ingredient")) + "\"";

                    if (cursor.isLast()) {
                        JSONstring += "]}]}";
                    }

                }

            }
            first = 1;
            previousRecipe = currentRecipe;
            cursor.moveToNext();

        }
        Log.d("Exit", "getDBToJSON: After While loop");

        db.close();
        return JSONstring;

    }

    public String getColumnImage() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_PERSONAL_RECIPE;
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        String image64 = cursor.getString(cursor.getColumnIndex("image"));

        return image64;
    }

    public List<PersonalRecipeC> getDataByIngredient(String[] ingredients){
        SQLiteDatabase db = getWritableDatabase();



        //To keep track of the recipes we have put into our return class
        List<String> RecipeList = new ArrayList<>();

        //Variables for class
        String recipeName = "";
        List<String> ingredientList = new ArrayList<>();
        String image = "";
        String rating = "";
        List<PersonalRecipeC> personalRecipeCList = new ArrayList<>();


        for (int i = 0; i < ingredients.length; i++) {
//            String query = "SELECT * FROM " + TABLE_PERSONAL_RECIPE + " WHERE " + COLUMN_INGREDIENT + " = \'" + ingredients[i] + "\' COLLATE SQL_Latin1_General_CP1_CI_AS;" ;
            String query = "SELECT * FROM " + TABLE_PERSONAL_RECIPE + " WHERE " + COLUMN_INGREDIENT + " = \'" + ingredients[i] + "\';" ;

            Log.d("SQL query is", "getDataByIngredient: "+query);
            Cursor cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
            int count=0;

            //If recipe has not been stated before, then put it into class
            while(!cursor.isAfterLast()) {


                Log.d(TAG, "getDataByIngredient: ONE");
                recipeName = cursor.getString(cursor.getColumnIndex("recipename"));
                if(!RecipeList.contains(recipeName)){       //If does not exist in list then insert it into list

                    RecipeList.add(recipeName);
//                    String recipeName, List<String> ingredients, String image, String rating
                    ingredientList.add(cursor.getString(cursor.getColumnIndex("ingredient")));
                    image = cursor.getString(cursor.getColumnIndex("image"));
                    rating = cursor.getString(cursor.getColumnIndex("rating"));

                    PersonalRecipeC personalRecipeC = new PersonalRecipeC();    //Define with null As we have null constructor ****MUST DEFINE INSIDE
                    personalRecipeC.setRecipeName(recipeName);
                    personalRecipeC.setIngredients(ingredientList);
                    personalRecipeC.setImage(image);
                    personalRecipeC.setRating(rating);

                    personalRecipeCList.add(count, personalRecipeC);

                    count++;
                    Log.d("Added PRecipes", "getDataByIngredient: "+recipeName);
                    Log.d(TAG, "getDataByIngredient: TWO");

                }
                Log.d(TAG, "getDataByIngredient: THREE");
                cursor.moveToNext();
            }
        }
        db.close();
        return personalRecipeCList;
    }

    //String recipeName, List<String> ingredients, String image, String rating
}

/**
 * ---------------------------------PAST ATTEMPTS--------------------------------------------------
 */

//*********************************************************************************************
//                                      Comments SQL Helper
//*********************************************************************************************
//1. Declare Database information: Version, DBName, TableName within DB, ColumnsNames.
//2. Create assigned constructors and pass info to SQLiteOpenHelper superclass
//3. OnCreate method, create table
//4. onUpgrade table: Upgrade table for version change
//5. Add<Class> function:
//6. Delete<Class> function:
//7. GetDataFromSQLite function: As a string <JSON Good>


//**********************************************************************************************
//7a. GetDataFromSQLite function: As a PersonalRecipeC class    ********************************
//**********************************************************************************************
//    public List<PersonalRecipeC> getDBToClass() {
//        //Declare return variable
//        List<PersonalRecipeC> personalRecipeCList = new ArrayList<>();
//        List<String> ingredientList = new ArrayList<>();
//        PersonalRecipeC personalRecipeC = new PersonalRecipeC();
//        String recipeName;
//        String previousRecipe = "";
//
//        //Settingi Up sql to read from it
//        SQLiteDatabase db = getWritableDatabase();
//        String query = "SELECT * FROM " + TABLE_PERSONAL_RECIPE;
//        Cursor cursor = db.rawQuery(query, null);
//        cursor.moveToFirst();
//
//        //Get Data from Database
////        PersonalRecipeC(String recipeName, List<String> ingredients, String image, String rating)
//        while(!cursor.isAfterLast()) {        //While a row exists
//
//            String currentRecipeName = cursor.getString(1);
//
//            //Recurring recipe
//            if(cursor.getString(1).equals(previousRecipe)){     //Repeating recipe: add to ingredients list
//                String iingredient = cursor.getString(3);
//                ingredientList.add(iingredient);
//
//            }else{                                              //New Recipe: Add new Strings and ingredient list
//
//                personalRecipeC.setRecipeName(currentRecipeName);
//
//                String irating = cursor.getString(2);
//                personalRecipeC.setRating(irating);
//
//                String iingredient = cursor.getString(3);
//                ingredientList.add(iingredient);
//                personalRecipeC.setRating(iingredient);
//
//                String iimage = cursor.getString(4);
//                personalRecipeC.setRating(iimage);
//
//            }
//            cursor.moveToNext();
//
//            //Add to personalRecipe class list if different recipe is next
//            String nextRecipeName = cursor.getString(1);
//            if(!cursor.isAfterLast()&&!nextRecipeName.equals(currentRecipeName)) {
//                personalRecipeC.setIngredients(ingredientList);
//                personalRecipeCList.add(personalRecipeC);
//
//                //Test output
//                Log.d("Class result", "getDBToClass: class list Name "+personalRecipeC.getRecipeName());
//                Log.d("Class result", "getDBToClass: class list Rating "+personalRecipeC.getRating());
//                Log.d("Class result", "getDBToClass: class list Rating "+personalRecipeC.getImage());
//
//            }
//
//            previousRecipe = currentRecipeName;
//
//        }
//
//        return personalRecipeCList;
//    }