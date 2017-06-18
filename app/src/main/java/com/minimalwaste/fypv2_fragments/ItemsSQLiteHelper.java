package com.minimalwaste.fypv2_fragments;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Wah Xian on 17/06/2017.
 */

public class ItemsSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ItemsDB.db";
    public static final String TABLE_MY_ITEMS = "MyItems";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_ITEM_NAME = "itemname";
    public static final String COLUMN_EXPIRY_DATE = "expirydate";
    public static final String COLUMN_PRICE = "price";
    public static final String COLUMN_IN_FRIDGE = "infridge";

    public ItemsSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_MY_ITEMS + "(" +          //remember to leave spaces
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_ITEM_NAME + " TEXT, " +
                COLUMN_EXPIRY_DATE + " TEXT, " +
                COLUMN_PRICE + " TEXT, " +
                COLUMN_IN_FRIDGE + " TEXT);";   //Remember to end SQL query with ;
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MY_ITEMS);
        onCreate(db);
    }

    public void deleteItem(String itemName) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MY_ITEMS + " WHERE " + COLUMN_ITEM_NAME + "=\"" + itemName + "\";");
    }

    public void deleteDB(){
        SQLiteDatabase db = getWritableDatabase();
//        db.execSQL("DELETE * FROM " + TABLE_MY_ITEMS + ";");
        db.execSQL("DELETE FROM " + TABLE_MY_ITEMS + ";");
    }

    public void addItems(String[] ItemArray, String[] inFridgeArray, String[] DateArray, String[] PriceArray) {
        //Define ContentValue for us to insert multiple columns at a time
        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();

        //Put Values from class into ContentValue   ---- Following 1NF --------
        int noItems = ItemArray.length;
        for (int i = 0; i < noItems; i++) {
            Log.d("No of Items added", "addItems: Traversing ADD");
            //id Auto increments, put things into database
            values.put(COLUMN_ITEM_NAME, ItemArray[i]);
            values.put(COLUMN_EXPIRY_DATE, DateArray[i]);
            values.put(COLUMN_PRICE, PriceArray[i]);  //Returns an Arraylist
            values.put(COLUMN_IN_FRIDGE, inFridgeArray[i]);
            db.insert(TABLE_MY_ITEMS, null, values);
        }

        //Get Writable Database and put in
        db.close();

    }

    public String getItemList() {
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MY_ITEMS;

        //String to store items available
        String returnString = "";

        //cursor
        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()){
            Log.d("Number of rows in DB", "getItemList: Traversing");
            if(cursor.getString(cursor.getColumnIndex("itemname"))!=null){
                returnString += cursor.getString(cursor.getColumnIndex("itemname")) + ",";
            }
            cursor.moveToNext();
        }

        db.close();

        return returnString;
    }

}
