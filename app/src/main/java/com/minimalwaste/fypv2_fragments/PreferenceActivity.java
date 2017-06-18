package com.minimalwaste.fypv2_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

public class PreferenceActivity extends AppCompatActivity {

    //-----Declare Cuisines that are available-----
    String[] cuisines = {"american", "italian", "asian", "mexican", "french", "indian", "chinese", "mediterranean",
            "greek","english","spanish","thai","german","moroccan","irish","japanese","korean"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preference);

        GridView gridView;

        //Set Title bar
//        getSupportActionBar().setTitle("Preference");
        getSupportActionBar().hide();

        //-----Put Content into GridView------
        AdapterPreference preference = new AdapterPreference(this, cuisines);
        gridView = (GridView) this.findViewById(R.id.GVAP);
        gridView.setAdapter(preference);

        //Set Button onClick method
        Button finButton = (Button) findViewById(R.id.BAP);
        finButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(v.getContext(), ItemsActivity.class);
                intent.putExtra("LoginResult", "backFromLogin");
                startActivity(intent);
            }
        });

    }
}
