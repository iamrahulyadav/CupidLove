package com.example.cupidlove.utils;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.crash.FirebaseCrash;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewBold;

public class BaseSignUpActivity extends AppCompatActivity implements  GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener{

    //TODO : Variable Declaration
    public CustomProgressDialog progressDialog;
    Dialog dialog;
    public SharedPreferences sharedpreferences;
    private LinearLayout llMain;
    private ImageView ivSearch, ivMenu;
    private TextViewBold tvTitle;
    private FrameLayout flText;
    private LinearLayout llChat;
    public LinearLayout llSearch;

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 101;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    Location mLastLocation;
    public String lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showInternateDialog();
        checkInternateConnection();
        final Thread.UncaughtExceptionHandler defaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

            public void uncaughtException(Thread thread, Throwable ex) {

                if (defaultHandler != null && ex != null && ex.getMessage() != null) {
                    FirebaseCrash.report(new Exception(ex));
                    defaultHandler.uncaughtException(thread, ex);
                }

            }
        });
    }

    //TODO : Set Title
    public void settvTitle(String title) {
        hideChat();
        tvTitle = (TextViewBold) findViewById(R.id.tvTitle);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(title);
    }

    //TODO : Hide Chat
    public void hideChat() {
        flText = (FrameLayout) findViewById(R.id.flText);
        if (flText != null) {
            flText.setVisibility(View.VISIBLE);
        }

        llChat = (LinearLayout) findViewById(R.id.llChat);
        if (llChat != null) {
            llChat.setVisibility(View.GONE);
            ;
        }


        hideSearchPanel();
    }

    //TODO : Hide Search Panel
    public void hideSearchPanel() {
        llSearch = (LinearLayout) findViewById(R.id.llSearch);
        if (llSearch != null) {
            llSearch.setVisibility(View.GONE);
        }
    }

    public void hideSearch() {
        hideChat();
        ivSearch = (ImageView) findViewById(R.id.ivSearch);
        ivSearch.setVisibility(View.GONE);
    }

    public void hideMenu() {
        hideChat();
        ivMenu = (ImageView) findViewById(R.id.ivMenu);
        ivMenu.setVisibility(View.GONE);
    }

    //TODO : Set Screen Layout Direction
    public void setScreenLayoutDirection() {
        llMain = (LinearLayout) findViewById(R.id.llMain);
        if (Constant.isRtl == 1) {
            llMain.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        } else {
            llMain.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        }

    }


    //TODO:Check Internate Connectivity
    public void checkInternateConnection() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            changeTextStatus(true);
        } else {
            changeTextStatus(false);
        }
    }

    public void changeTextStatus(boolean isConnected) {

        // Change status according to boolean value
        Log.e("Internate Connected", isConnected + "");
        if (isConnected) {
            if (dialog != null)
                dialog.dismiss();
        } else {
            if (dialog != null)
                dialog.show();
        }
    }

    public void setImageView() {
        final LinearLayout llPhotoSelection = findViewById(R.id.llPhotoSelection);
        final LinearLayout.LayoutParams paramsImagePhoto = (LinearLayout.LayoutParams) llPhotoSelection.getLayoutParams();
        llPhotoSelection.post(new Runnable() {
            @Override
            public void run() {
                paramsImagePhoto.height = llPhotoSelection.getWidth();
                llPhotoSelection.setLayoutParams(paramsImagePhoto);
            }
        });
    }

    //TODO:Dialog For NoInternate Connection
    public void showInternateDialog() {
        dialog = new Dialog(this, android.R.style.Theme_Light);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_no_internate_dialog);
        dialog.getWindow().setBackgroundDrawableResource(R.color.black_transparent);
        dialog.findViewById(R.id.ivDismiss).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    dialog.dismiss();
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyApplication.activity = this;
        MyApplication.activityResumed();// On Resume notify the Application
        checkInternateConnection();
        Log.e("Activity Resume", MyApplication.isActivityVisible() + "");
    }


    //TODO : Check The Status of Online Or Offline
    @Override
    protected void onStop() {
        super.onStop();
    }

    //TODO : Check The Status of Online Or Offline
    @Override
    protected void onStart() {
        super.onStart();
    }


    //TODO : Check The Status Restart
    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public SharedPreferences getPreferences() {
        sharedpreferences = getSharedPreferences(
                Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences;
    }


    //TODO : Show Progress
    public void showProgress(String val) {
        progressDialog = new CustomProgressDialog(this);
        progressDialog.showCustomDialog();
    }


    //TODO : Dismiss progress
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dissmissDialog();
        }

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        } else {
            mLocationRequest = LocationRequest.create();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(100); // Update location every second


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

            } else {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


                mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                        mGoogleApiClient);
                if (mLastLocation != null) {
                    lat = String.valueOf(mLastLocation.getLatitude());
                    lon = String.valueOf(mLastLocation.getLongitude());
                    Constant.LAT = Float.parseFloat(lat);
                    Constant.LNG = Float.parseFloat(lon);
                    SharedPreferences sharedpreferences = getSharedPreferences(
                            Constant.MyPREFERENCES, Context.MODE_PRIVATE);

                    SharedPreferences.Editor pre = sharedpreferences.edit();
                    pre.putString(RequestParamUtils.LATITUDE, Constant.LAT + "");
                    pre.putString(RequestParamUtils.LONGITUDE, Constant.LNG + "");
                    pre.commit();


                }
            }

        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }
    public synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        Constant.LAT = Float.parseFloat(lat);
        Constant.LNG = Float.parseFloat(lon);
        SharedPreferences sharedpreferences = getSharedPreferences(
                Constant.MyPREFERENCES, Context.MODE_PRIVATE);

//        SharedPreferences.Editor pre = sharedpreferences.edit();
//        pre.putString(RequestParamUtils.LATITUDE, Constant.LAT + "");
//        pre.putString(RequestParamUtils.LONGITUDE, Constant.LNG + "");
//        pre.commit();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.


                    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                            mGoogleApiClient);
                    if (mLastLocation != null) {
                        lat = String.valueOf(mLastLocation.getLatitude());
                        lon = String.valueOf(mLastLocation.getLongitude());
                        Constant.LAT = Float.parseFloat(lat);
                        Constant.LNG = Float.parseFloat(lon);
                        SharedPreferences sharedpreferences = getSharedPreferences(
                                Constant.MyPREFERENCES, Context.MODE_PRIVATE);

//                        SharedPreferences.Editor pre = sharedpreferences.edit();
//                        pre.putString(RequestParamUtils.LATITUDE, Constant.LAT + "");
//                        pre.putString(RequestParamUtils.LONGITUDE, Constant.LNG + "");
//                        pre.commit();

                    }
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}