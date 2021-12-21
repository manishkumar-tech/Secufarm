package com.weather.risk.mfi.myfarminfo.youtubevideostream;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.weather.risk.mfi.myfarminfo.R;
import com.weather.risk.mfi.myfarminfo.activities.MainProfileActivity;

public class YoutubeWebview extends AppCompatActivity {

    //    String URL = "https://youtu.be/RSKRs200gxQ";
    String URL = "";
    //    String URL_ID = "F0dYJ7g0X7g";
    WebView mywebview;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.youtubewebview);
        mywebview = (WebView) findViewById(R.id.webView);
//        WebSettings webSettings = mywebview.getSettings();
//        webSettings.setJavaScriptEnabled(true);


//        URL = URL + URL_ID;

        Bundle bundle = getIntent().getExtras();
        if (bundle.size() > 0) {
            URL = bundle.getString("YoutubeVideoURL");
        }
        mywebview.loadUrl(URL);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(YoutubeWebview.this, MainProfileActivity.class);
        startActivity(intent);
        finish();

    }

}
