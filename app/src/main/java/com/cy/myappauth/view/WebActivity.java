package com.cy.myappauth.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.cy.myappauth.R;

public class WebActivity extends AppCompatActivity {
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        WebView webView=(WebView) findViewById(R.id.webview);
        webView.loadUrl("https://youtu.be/pZd234e05-g");
        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings=webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        btn=(Button)findViewById(R.id.btnShare);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"jknin");
                intent.putExtra(Intent.EXTRA_SUBJECT,"qu");
                startActivity(Intent.createChooser(intent,"Share QQQ"));
            }
        });
    }

}