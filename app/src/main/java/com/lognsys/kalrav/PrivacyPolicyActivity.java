package com.lognsys.kalrav;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class PrivacyPolicyActivity extends AppCompatActivity {
    WebView simpleWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        simpleWebView = (WebView) findViewById(R.id.simpleWebView);
        simpleWebView.loadUrl("file:///android_asset/privacypolicy.html");

    }
}
