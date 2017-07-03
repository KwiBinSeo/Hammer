package com.example.vip.hammer;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class activity_grap1 extends AppCompatActivity {

    private WebView webview;
    private String url = "file:///android_asset/www/index.html";

    private Button BackBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_grap1);
        webview = (WebView) findViewById(R.id.webView);
        webview.setWebViewClient(new WebViewClient());
        webview.loadUrl(url);
        webview.getSettings().setJavaScriptEnabled(true);

        BackBtn = (Button) findViewById(R.id.BackBtn);

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Onclick", "CallMainActivity");
                finish();
            }
        });

    }
}
