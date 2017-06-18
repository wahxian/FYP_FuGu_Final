package com.minimalwaste.fypv2_fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class AddRecipeActivity extends AppCompatActivity {

    EditText ETAARname, ETAARingredients,ETAARrating;
    Button BAARimage, BAARadd;
    ImageView IVAAR;
    PersonalRecipeSQLiteHelper dbHandler;

    final int REQUEST_CODE_GALLERY = 123;

//    public static SQLiteHelper sqLiteHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_recipe);

        //Set Black cast on Background image
        View backgroundImage = findViewById(R.id.activity_add_recipe);
        Drawable background = backgroundImage.getBackground();
        background.setAlpha(200);

        //Set title and display back button
        getSupportActionBar().setTitle("Add Recipe");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //******************************************************************************************
        //===================================SQL CODE===============================================
        //******************************************************************************************

        initViews();

        dbHandler = new PersonalRecipeSQLiteHelper(this,null,null,1);

        //===========================Choose Image OnClick===========================================
        BAARimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(
                        AddRecipeActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);
            }
        });
//
        //===========================Add Recipe to Database=========================================
        BAARadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //-----Get text values from EditText and image and
                String recipeName = ETAARname.getText().toString().trim();
                String ingredientString = ETAARingredients.getText().toString().trim();
                List<String> ingredients = Arrays.asList(ingredientString.split("\\s,\\s"));
                String image = imageViewToByte(IVAAR);          //Function is implemented below
                String rating = ETAARrating.getText().toString().trim();

                //Put all data into personalRecipeC class
                PersonalRecipeC personalRecipeC= new PersonalRecipeC(recipeName,ingredients,image,rating);

                //-----put into Database
                try{
                    dbHandler.addPersonalRecipeC(personalRecipeC);
                    Toast.makeText(getApplicationContext(), "Added successfully!", Toast.LENGTH_SHORT).show();
                    ETAARname.setText("");
                    ETAARingredients.setText("");
                    ETAARrating.setText("");
                    IVAAR.setImageResource(R.mipmap.ic_launcher);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * -----------------------------------FUNCTIONS-------------------------------------------------
     */

    //******************************************************//
    //  Initialise Views by getting the ids from layout     //
    //******************************************************//

    private void initViews() {
        ETAARrating = (EditText) findViewById(R.id.ETAARrating);
        ETAARname = (EditText) findViewById(R.id.ETAARname);
        ETAARingredients = (EditText) findViewById(R.id.ETAARingredients);
        BAARimage = (Button) findViewById(R.id.BAARimage);
        BAARadd = (Button) findViewById(R.id.BAARadd);
        IVAAR = (ImageView) findViewById(R.id.IVAAR);
    }

    //****************************************************//
    //  Once User has chosen to give/reject permission    //
    //****************************************************//
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    //******************************************//
    //  Convert Images to Bytes for Storage     //
    //******************************************//

    private String imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        Bitmap resized = Bitmap.createScaledBitmap(bitmap,500,500,true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.PNG, 5, stream);
        byte[] byteArray = stream.toByteArray();
        String encImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encImage;
    }



    //******************************************************//
    //  Get back Image from gallery and show in ImageView   //
    //******************************************************//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                IVAAR.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

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


    public void BAARlist(View view) {
//        //Get JSON string from helper
//        try{
////            String data = dbHandler.getDBToJSON();
////            Log.d("JSON Data", "JSON Output: "+data);
//
//            //Testing if String coming back can be converted to IMage == WE KNOW STRING WORKS EVEN THO MONITOR DOES NOT SHOW ALL
//            String imageData = dbHandler.getColumnImage();
//            ImageView testView = (ImageView) findViewById(R.id.testingPersonal);
//
//
//            byte[] decodedString = Base64.decode(imageData, Base64.DEFAULT);
//            Bitmap base64Bitmap = BitmapFactory.decodeByteArray(decodedString, 0,
//                    decodedString.length);
//
//            testView.setImageBitmap(base64Bitmap);
//
//            Log.d("JSON Data", "JSON Output: "+imageData);
//
//
//        }catch(Exception e){
//            Log.d("Database error", "onCreate: Failed to get JSON from DATABASE");
//        }
    }

    public void BAARlistclicked(View view) {
        try {
            Intent intent1 = new Intent(this,PersonalRecipeActivity.class);
            String imageTest = imageViewToByte(IVAAR);
            intent1.putExtra("ImageIn64",imageTest);
            startActivity(intent1);
        }catch (Exception e) {
            Toast.makeText(this, "Failed to initialise Activity", Toast.LENGTH_SHORT).show();
        }
    }
}
