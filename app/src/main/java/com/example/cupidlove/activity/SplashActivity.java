package com.example.cupidlove.activity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.utils.BaseSignUpActivity;
import com.example.cupidlove.utils.Config;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import java.util.Calendar;
import java.util.Locale;

import javax.net.ssl.SSLContext;

import butterknife.ButterKnife;

public class SplashActivity extends BaseSignUpActivity implements ResponseListener {

    //TODO: Variable Declaration
    private Bundle bundle;
    public String id, fid, fname, flname, profile_image, message, type, ejebberedId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getAllConfiguration();
        setLanguage();


        SharedPreferences sharedpreferences = getSharedPreferences(
                Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        if (sharedpreferences.getString(RequestParamUtils.LATITUDE, "") == "" &&
                sharedpreferences.getString(RequestParamUtils.LONGITUDE, "") == "") {
            buildGoogleApiClient();
        }
        String id = getSharedPreferences(
                Constant.MyPREFERENCES, Context.MODE_PRIVATE).getString(RequestParamUtils.FIRST_VISIT, "");

        if (id == "") {

        }

    }

    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE,
            }, 1212);
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 1212) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setData();
            } else {
                Snackbar.make(findViewById(R.id.llMain), "Permission must be need", Snackbar.LENGTH_INDEFINITE)
                        .setAction(android.R.string.ok, new View.OnClickListener() {
                            @Override
                            @TargetApi(Build.VERSION_CODES.M)
                            public void onClick(View v) {
                                final Intent i = new Intent();
                                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                i.addCategory(Intent.CATEGORY_DEFAULT);
                                i.setData(Uri.parse("package:" + getPackageName()));
                                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                startActivity(i);
                            }
                        }).show();
            }
        }
    }


    //TODO: Set Data OF Register User
    public void setData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                String id = getSharedPreferences(
                        Constant.MyPREFERENCES, Context.MODE_PRIVATE).getString(RequestParamUtils.USER_ID, "");
                String register = getSharedPreferences(
                        Constant.MyPREFERENCES, Context.MODE_PRIVATE).getString(RequestParamUtils.REGISTER, "");

                if (register.equals("registerdatepref")) {
                    Intent intent = new Intent(SplashActivity.this, DatePreferenceActivity.class);
                    startActivity(intent);
                } else if (register.equals("uploadphoto")) {
                    Intent intent = new Intent(SplashActivity.this, UploadPhotosActivity.class);
                    startActivity(intent);
                } else if (register.equals("registeruserdatepreference")) {
                    Intent intent = new Intent(SplashActivity.this, UserDatePreferencesActivity.class);
                    startActivity(intent);
                } else if (register.equals("registerdiscoverypreference")) {
                    Intent intent = new Intent(SplashActivity.this, DiscoveryPreferencesActivity.class);
                    startActivity(intent);
                } else if (id.equals("")) {
                    Intent intent = new Intent(SplashActivity.this, LoginOrSignUpActivity.class);
                    startActivity(intent);
                } else {

                    if (getIntentData() == null) {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                        intent.putExtra("type", type);
                        intent.putExtra("friendid", fid);
                        intent.putExtra("friend_Fname", fname);
                        intent.putExtra(" friend_Lname", flname);
                        intent.putExtra("message", message);
                        intent.putExtra("friend_profileImg_url", profile_image);
                        intent.putExtra(RequestParamUtils.XMPPUSERNAME, ejebberedId);
                        startActivity(intent);
                    }
                }

                setConfig();
                finish();
            }
        }, 2000);
    }

    //TODO: get user detail when user alredy login
    public String getIntentData() {
        bundle = getIntent().getExtras();
        if (bundle != null) {
            fid = bundle.getString("friendid");
            type = bundle.getString("type");
            fname = bundle.getString("friend_Fname");
            flname = bundle.getString("friend_Lname");
            message = (bundle.getString("message"));
            profile_image = bundle.getString("friend_profileImg_url");
            ejebberedId = bundle.getString(RequestParamUtils.XMPPUSERNAME);
            Constant.FRIEND_LAST_NAME = flname;
            Constant.FRIEND_FIRST_NAME = fname;
            Constant.EJUSERID = bundle.getString(RequestParamUtils.XMPPUSERNAME);
            return type;
        }
        return null;
    }

    //TODO : Update Device Token
    public void updateDeviceToken() {
        try {

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("userid", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("device_token", getPreferences().getString(RequestParamUtils.DEVICE_TOKEN, ""));

            Debug.e("updateDeviceToken", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().UPDATE_TOKEN, params, new ResponseHandler(this, this, "updateDeviceToken"));
        } catch (Exception e) {
            Debug.e("updateDeviceToken Exception", e.getMessage());
        }
    }

    //TODO : Get All Configuration
    public void getAllConfiguration() {
        try {

            RequestParams params = new RequestParams();
            Debug.e("getAllConfiguration", params.toString() + "Called");
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().CONFIGURATION, params, new ResponseHandler(this, this, "getAllConfiguration"));
        } catch (Exception e) {
            Debug.e("getAllConfiguration Exception", e.getMessage());
        }
    }

    //TODO: Set The Response Of the Device Token
    @Override
    public void onResponse(String response, String methodName) {

        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("updateDeviceToken")) {
            Log.e(methodName + " Response :-", response);
            if (mayRequestPermission()) {
                setData();
            }

        } else if (methodName.equals("getAllConfiguration")) {
            Log.e("response", response);

            try {
                final com.example.cupidlove.model.Configuration configurationRider = new Gson().fromJson(
                        response, new TypeToken<com.example.cupidlove.model.Configuration>() {
                        }.getType());

                if (configurationRider.gOOGLEPLACEAPIKEY != null) {
                    getPreferences().edit().putString(Config.GOOGLE_PLACE_API_KEY, configurationRider.gOOGLEPLACEAPIKEY).commit();

                    getPreferences().edit().putString(Config.APP_XMPP_HOST, configurationRider.aPPXMPPHOST).commit();
                    getPreferences().edit().putString(Config.APP_XMPP_SERVER, configurationRider.aPPXMPPSERVER).commit();
                    getPreferences().edit().putString(Config.XMPP_DEFAULT_PASSWORD, configurationRider.xMPPDEFAULTPASSWORD).commit();
                    Boolean boolean1 = Boolean.valueOf(configurationRider.xMPPENABLE);
                    getPreferences().edit().putBoolean(Config.XMPP_ENABLED, boolean1).commit();

                    getPreferences().edit().putString(Config.INSTAGRAM_CALLBACK_BASE, configurationRider.iNSTAGRAMCALLBACKBASE).commit();
                    getPreferences().edit().putString(Config.INSTAGRAM_CLIENT_SECRET, configurationRider.iNSTAGRAMCLIENTSECRET).commit();
                    getPreferences().edit().putString(Config.INSTAGRAM_CLIENT_ID, configurationRider.iNSTAGRAMCLIENTID).commit();
                    getPreferences().edit().putString(Config.ADMOBKEY, configurationRider.adMobKey).commit();
                    getPreferences().edit().putString(Config.ADMOBVIDEOKEY, configurationRider.adMobVideoKey).commit();
                    getPreferences().edit().putString(Config.TERMS_CONDITION, configurationRider.termsAndConditionsUrl).commit();
                    getPreferences().edit().putString(Config.INAPPPURCHASE, configurationRider.removeAddInAppBilling).commit();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }


    //TODO : Set The Language
    public void setLanguage() {

        if (!Prefs.getString(RequestParamUtils.LANGUAGE, "").equals("")) {

            String selectedLang = Prefs.getString(RequestParamUtils.LANGUAGE, "");

            if (selectedLang.equalsIgnoreCase("French"))
                setLocale("fr");
            else if (selectedLang.equalsIgnoreCase("Russian"))
                setLocale("ru");

            else if (selectedLang.equalsIgnoreCase("arabic"))
                setLocale("ar");

            else if (selectedLang.equalsIgnoreCase("hindi"))
                setLocale("hi");

            else
                setLocale("en");
        } else {
            Prefs.putString(RequestParamUtils.LANGUAGE, "en");
            setLocale("en");
        }
    }


    public void setConfig() {
        Constant.NAME_POSTFIX = getPreferences().getString(Config.APP_XMPP_SERVER, "");
        Constant.XMPP_PASSWORD = getPreferences().getString(Config.XMPP_DEFAULT_PASSWORD, "");
        Constant.XMPP_HOST = getPreferences().getString(Config.APP_XMPP_HOST, "");
        Constant.GOOGLE_PLACE_API_KEY = getPreferences().getString(Config.GOOGLE_PLACE_API_KEY, "AIzaSyCgmevoDYy5dcOJGMewrWeXgOgetKPl8SY");
    }


    //TODO : Set Local detail of user
    public void setLocale(String lang) {
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        String deviceToken = getPreferences().getString(RequestParamUtils.DEVICE_TOKEN, "");
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
        SharedPreferences.Editor pre = getPreferences().edit();
        pre.putString(RequestParamUtils.DEVICE_TOKEN, refreshToken);
        pre.commit();

        String id = getPreferences().getString(RequestParamUtils.USER_ID, "");
        if (id.equals("")) {
            if (mayRequestPermission()) {
                setData();
            }

        } else if (deviceToken.equals("") && refreshToken != null) {
            if (mayRequestPermission()) {
                setData();
            }
        } else {
            if (!deviceToken.equals(refreshToken)) {
                updateDeviceToken();
            } else {
                if (mayRequestPermission()) {
                    setData();
                }
            }
        }
//        Utils.printHashKey(this);
    }


}
