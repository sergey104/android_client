package com.wp.client.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.wp.client.Application;
import com.wp.client.R;


public class BrowserActivity extends ActionBarActivity {

    private static final String APP_PREFERENCES_URL;

    static {
        String val = Application.getInstance().getApplicationContext().getResources().getString(R.string.mobile_connection_url);
        APP_PREFERENCES_URL = val;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        WebView webview;
        webview = (WebView) findViewById(R.id.webview);
        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = webview.getSettings();
        webSettings.setSavePassword(true);
        webSettings.setSaveFormData(true);
        webSettings.setJavaScriptEnabled(true);
        String link = getIntent().getExtras().getString("link");
        if (link == Application.NOT_FOUND_ERROR) {
            webview.loadUrl(APP_PREFERENCES_URL);
            webview.requestFocus();
        } else {
            webview.getSettings().setJavaScriptEnabled(true);
            webview.loadUrl(link);
            webview.requestFocus();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_browser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
