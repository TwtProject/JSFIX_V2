package com.example.jsfix_v2;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

public class web_view extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String newLink;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);

        newLink = getIncomeUrl();
        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl(newLink);
    }
    private String getIncomeUrl(){
        if (getIntent().hasExtra("link_url")){
            String link = getIntent().getStringExtra("link_url");
            return link;
        }
        return null;
    }
}
