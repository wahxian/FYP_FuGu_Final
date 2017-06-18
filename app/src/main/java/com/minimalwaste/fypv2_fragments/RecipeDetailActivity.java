package com.minimalwaste.fypv2_fragments;

import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class RecipeDetailActivity extends AppCompatActivity {
    TextView mTextView;
    ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_recipe_detail);

//        mImageView = (ImageView) findViewById(R.id.IVRecipeDetail);
        mTextView = (TextView) findViewById(R.id.TVARD);

        Bundle mBundle = getIntent().getExtras();
        if(mBundle!=null) {
//            Picasso.with(this).load(mBundle.getString("recipeIndex")).into(mImageView);
////            mImageView.setImageResource(mBundle.getInt("recipeIndex"));
            mTextView.setText(mBundle.getString("recipeName"));

//            getSupportActionBar().setTitle(mBundle.getString("recipeName"));
            String url = "http://www.yummly.com/recipe/" + mBundle.getString("recipeID");

            WebView webb = (WebView)findViewById(R.id.WVARD);
            webb.setWebViewClient(new WebViewClient());

            webb.loadUrl(url);

        }
    }
}
