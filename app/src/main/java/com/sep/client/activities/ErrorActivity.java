package com.sep.client.activities;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.sep.client.R;
import com.sep.client.util.NetworkUtil;

public class ErrorActivity extends Activity {
    private static final String TAG = "ErrorActivity";

    private BroadcastReceiver networkStateReceiver = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "on create");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.error);

        TextView title = (TextView) findViewById(R.id.title);
        Typeface robotoBold = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        title.setTypeface(robotoBold);

        TextView error = (TextView) findViewById(R.id.error_message);
        Typeface robotoRegular = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        error.setTypeface(robotoRegular);
    }

    @Override
    protected void onResume() {
        super.onResume();

        networkStateReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (NetworkUtil.isDataAvailable(ErrorActivity.this)) {
                    Intent newIntent = new Intent().setClass(ErrorActivity.this, LoginActivity.class);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(newIntent);
                    finish();
                }
            }
        };

        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkStateReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(networkStateReceiver);
    }
}
