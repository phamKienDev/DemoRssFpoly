package com.hlub.dev.testrss;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class NewsActivity extends AppCompatActivity {
    private WebView webviewRss;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        webviewRss = (WebView) findViewById(R.id.webviewRss);

        Intent intent=getIntent();
        String link=intent.getStringExtra("linkTinTuc");

        //truyen link doc tin tuc
        webviewRss.loadUrl(link);

        //doc tren app
        webviewRss.setWebViewClient(new WebViewClient());



    }
}
