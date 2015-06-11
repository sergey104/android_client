package com.sep.client;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Joe Munoz on 5/11/15.
 * <p/>
 * Use this class to put static things that you want to use in other places
 */
public class Application extends android.app.Application {
    private static Application instance = null;
    public static Context GlobalContext = null;

    public static final String EXTRA_MESSAGE = "message";
    public static final String NOT_FOUND_ERROR = "404";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "1.0.6";
    public static final String PROPERTY_RESPONSE_LINK = "respLink";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String TAG = "GCM Client";

    public Context context;

    private RequestQueue queue = null;

    public Application() {
        super();
        instance = this;
    }

    @Override
    public void onCreate() {
        queue = Volley.newRequestQueue(this);
        super.onCreate();
        Application.GlobalContext = this;

    }

    public static Application getInstance() {
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (queue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            queue = Volley.newRequestQueue(this);
        }

        return queue;
    }


    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return PLAY_SERVICES_RESOLUTION_REQUEST
     */
    public static int getPlayServiceRequest() {
        return PLAY_SERVICES_RESOLUTION_REQUEST;
    }
}
