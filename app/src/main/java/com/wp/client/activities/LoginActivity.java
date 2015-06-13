package com.wp.client.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.wp.client.Application;
import com.wp.client.R;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A login screen that offers login via phone/password.
 */
public class LoginActivity extends Activity {

    public static final String APP_PREFERENCES_LOGIN_URL;
    public static final String APP_PREFERENCES_REGISTRATION_URL;
    public static final String APP_PREFERENCES_URL;
    public static final String SENDER_ID;

    public GoogleCloudMessaging gcm;
    public String regid;
    public Context context;

    static {

        String val = Application.getInstance().getApplicationContext().getResources().getString(R.string.login_url);
        APP_PREFERENCES_LOGIN_URL = val;
        val = Application.getInstance().getApplicationContext().getResources().getString(R.string.registration_url);
        APP_PREFERENCES_REGISTRATION_URL = val;
        val = Application.getInstance().getApplicationContext().getResources().getString(R.string.mobile_connection_url);
        APP_PREFERENCES_URL = val;
        val = Application.getInstance().getApplicationContext().getResources().getString(R.string.sender_id);
        SENDER_ID = val;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        context = getApplicationContext();
        EditText myEditText = (EditText) findViewById(R.id.mobile_number);
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(myEditText.getWindowToken(), 0);
        Button myBtn = (Button) findViewById(R.id.btnLogin);
        myBtn.requestFocus();
        String languageToLoad = "es"; // spanish language hardcoded
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());

        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(context);
            String link = getResponseLink(context);
            if (regid == Application.NOT_FOUND_ERROR) {
                registerInBackground();

                if (!(link == Application.NOT_FOUND_ERROR)) {
                    Intent j = new Intent(getApplicationContext(), BrowserActivity.class);
                    j.putExtra("link", link);
                    startActivity(j);
                }
            } else {
                if (!(link == Application.NOT_FOUND_ERROR)) {
                    Intent j = new Intent(getApplicationContext(), BrowserActivity.class);
                    j.putExtra("link", link);
                    startActivity(j);
                }
            }
        } else {
            Log.i(Application.TAG, "No valid Google Play Services APK found.");
        }


    }


    private boolean isPhoneValid(String phone_number) {

        return phone_number.length() == 10;
    }

    private boolean isPasswordValid(String password) {

        return password.length() > 4;
    }


    public void onClick(View view) {
        RequestQueue queue = Application.getInstance().getRequestQueue();
        String url = APP_PREFERENCES_LOGIN_URL;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if (response.contains("true")) {
                            Intent i = new Intent(getApplicationContext(), BrowserActivity.class);
                            String json = response;

                            JSONParser parser = new JSONParser();

                            Object obj = null;
                            try {
                                obj = parser.parse(json);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            Object obj1 = null;
                            try {
                                obj1 = parser.parse(json);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            JSONObject jsonObj = (JSONObject) obj1;

                            String s = (jsonObj.get("link").toString());
                            String g = s.replace("localhost", "10.0.2.2");
                            storeResponseLink(context = getApplicationContext(), g);
                            i.putExtra("link", g);
                            startActivity(i);
                        } else {
                            TextView mWarning;
                            mWarning = (TextView) findViewById(R.id.warning_all);
                            mWarning.setVisibility(TextView.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle(R.string.network_alert)
                                .setMessage(R.string.network_problem)
                                .setCancelable(false)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        LoginActivity.this.finish();
                                    }
                                }).create().show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                EditText mInputPhone;
                EditText mInputPassword;
                mInputPhone = (EditText) findViewById(R.id.mobile_number);
                mInputPassword = (EditText) findViewById(R.id.password);
                Map<String, String> params = new HashMap<String, String>();
                params.put("phone_number", mInputPhone.getText().toString());
                params.put("password", mInputPassword.getText().toString());

                return params;
            }
        };
        queue.add(postRequest);
    }

    public void getRegister(View view) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Application.getInstance().getPlayServiceRequest()).show();
            } else {
                Log.i(Application.TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    // You need to do the Play Services APK check here too.
    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(context);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = "Device registered, registration ID=" + regid;

                    sendRegistrationIdToBackend();

                    storeRegistrationId(context, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                    Log.d("Error.Response", ex.toString());
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle(R.string.network_alert)
                            .setMessage(R.string.network_problem)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoginActivity.this.finish();
                                }
                            }).create().show();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //mDisplay.append(msg + "\n");
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
        RequestQueue queue = Application.getInstance().getRequestQueue();
        String url = APP_PREFERENCES_REGISTRATION_URL;
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                        if (response.contains("200")) {

                            // do something
                        } else {
                            // do something
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.AddingID", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("reg_id", regid);
                params.put("user_id", "2345");

                return params;
            }
        };
        queue.add(postRequest);
    }

    /**
     * Gets the current response Link for application.
     * If result is empty, the app needs to login.
     *
     * @return response Link, or empty string if there is no existing
     * response Link.
     */
    public String getResponseLink(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String responseLink = prefs.getString(Application.PROPERTY_RESPONSE_LINK, "");
        if (responseLink.isEmpty()) {
            Log.i(Application.TAG, "ResponseLink not found.");
            return Application.NOT_FOUND_ERROR;
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(Application.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = Application.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(Application.TAG, "App version changed.");
            return Application.NOT_FOUND_ERROR;
        }
        return responseLink;
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    public void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = Application.getAppVersion(context);
        Log.i(Application.TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Application.PROPERTY_REG_ID, regId);
        editor.putInt(Application.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Stores the registration ID and app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param link    registration link
     */
    public void storeResponseLink(Context context, String link) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = Application.getAppVersion(context);
        Log.i(Application.TAG, "Saving registration link on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Application.PROPERTY_RESPONSE_LINK, link);
        editor.putInt(Application.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service.
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(Application.PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(Application.TAG, "Registration not found.");
            return Application.NOT_FOUND_ERROR;
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing registration ID is not guaranteed to work with
        // the new app version.
        int registeredVersion = prefs.getInt(Application.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = Application.getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(Application.TAG, "App version changed.");
            return Application.NOT_FOUND_ERROR;
        }
        return registrationId;
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    public SharedPreferences getGCMPreferences(Context context) {
        // This app persists the registration ID in shared preferences, but
        // how you store the registration ID in your app is up to you.
        return getSharedPreferences(LoginActivity.class.getSimpleName(),
                Context.MODE_PRIVATE);
    }

    public static String MY_PREF = "APP_PREF";

}


