package com.minimalwaste.fypv2_fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button signInButton = (Button) findViewById(R.id.BALsignin);
        Button signUpButton = (Button) findViewById(R.id.BALsignup);
        Button skipButton = (Button) findViewById(R.id.BALskip);

//        getSupportActionBar().setTitle("FuGu Login");
        getSupportActionBar().hide();

        //-----Sign Up go to Signed in Recipe Activity-----
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(v.getContext(), ItemsActivity.class);
                //Login Authentication
                //Login Authentication token to be sent back as putExtra
//                intent.putExtra("Token", "1");
                intent.putExtra("LoginResult", "backFromLogin");
                startActivity(intent);
            }
        });

        //-----Sign Up go to Register Activity-----
        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        //-----Sign Up go to Recipe Activity-----
        skipButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent intent = new Intent(v.getContext(), ItemsActivity.class);
//                intent.putExtra("Token", "1");
                intent.putExtra("LoginResult", "backFromLogin");
                startActivity(intent);
            }
        });
    }
}
