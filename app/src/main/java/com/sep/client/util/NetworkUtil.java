package com.sep.client.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtil {
    public static boolean isDataAvailable(Context context) {
        ConnectivityManager conxMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo mobileNwInfo = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifiNwInfo = conxMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        return ((mobileNwInfo == null ? false : mobileNwInfo.isConnected()) || (wifiNwInfo == null ? false : wifiNwInfo.isConnected()));
    }
}