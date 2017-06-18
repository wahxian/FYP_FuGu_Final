package com.minimalwaste.fypv2_fragments;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WiFiConfig extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_config);

        //Set title and display back button
        getSupportActionBar().setTitle("ESP8266 Configuration");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String url = "http://192.168.4.1/";
        WebView webb = (WebView)findViewById(R.id.WVAWFC);
        webb.setWebViewClient(new WebViewClient());

        webb.loadUrl(url);

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
}
