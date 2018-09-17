package com.example.cupidlove.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.example.cupidlove.R;
import com.example.cupidlove.helper.GetLocationServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by root on 16/2/16.
 */
public class Utils {

    //TODO : variable Declaration
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    public static boolean mAlreadyStartedService = false;


    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size = 1024;
        try {
            byte[] bytes = new byte[buffer_size];
            for (; ; ) {
                int count = is.read(bytes, 0, buffer_size);
                if (count == -1)
                    break;
                os.write(bytes, 0, count);
            }
        } catch (Exception ex) {
        }
    }

    public static String streamToString(InputStream is) throws IOException {
        String str = "";

        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(is));

                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }

                reader.close();
            } finally {
                is.close();
            }

            str = sb.toString();
        }

        return str;
    }


    public static boolean resetExternalStorageMedia(Context context,
                                                    String filePath) {
        try {
            if (Environment.isExternalStorageEmulated())
                return (false);
            Uri uri = Uri.parse("file://" + new File(filePath));
            Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, uri);

            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
            return (false);
        }

        return (true);
    }

    public static void setPref(Context c, String pref, String val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putString(pref, val);
        e.commit();
    }

    public static String getPref(Context c, String pref, String val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getString(pref,
                val);
    }

    public static void setPref(Context c, String pref, boolean val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putBoolean(pref, val);
        e.commit();

    }

    public static boolean getPref(Context c, String pref, boolean val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(
                pref, val);
    }

    public static void delPref(Context c, String pref) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.remove(pref);
        e.commit();
    }

    public static void setPref(Context c, String pref, int val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putInt(pref, val);
        e.commit();

    }

    public static int getPref(Context c, String pref, int val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getInt(pref,
                val);
    }

    public static void setPref(Context c, String pref, long val) {
        SharedPreferences.Editor e = PreferenceManager.getDefaultSharedPreferences(c).edit();
        e.putLong(pref, val);
        e.commit();
    }

    public static long getPref(Context c, String pref, long val) {
        return PreferenceManager.getDefaultSharedPreferences(c).getLong(pref,
                val);
    }

    public static void setPref(Context c, String file, String pref, String val) {
        SharedPreferences settings = c.getSharedPreferences(file,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor e = settings.edit();
        e.putString(pref, val);
        e.commit();

    }

    public static String getPref(Context c, String file, String pref, String val) {
        return c.getSharedPreferences(file, Context.MODE_PRIVATE).getString(
                pref, val);
    }
    //TODO : Chech the email Is Valid Or Not
    public static boolean isValidEmail(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
                    .matches();
        }
    }

    public static void sendExceptionReport(Exception e) {
        e.printStackTrace();

        try {
            // Writer result = new StringWriter();
            // PrintWriter printWriter = new PrintWriter(result);
            // e.printStackTrace(printWriter);
            // String stacktrace = result.toString();
            // new CustomExceptionHandler(c, URLs.URL_STACKTRACE)
            // .sendToServer(stacktrace);
        } catch (Exception e1) {
            e1.printStackTrace();
        }

    }

    public static int getDeviceWidth(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.widthPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return 480;
    }

    public static int getDeviceHeight(Context context) {
        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return metrics.heightPixels;
        } catch (Exception e) {
            Utils.sendExceptionReport(e);
        }

        return 800;
    }

    public static void hideKeyBoard(Context c, View v) {
        InputMethodManager imm = (InputMethodManager) c
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    public static String parseCalendarFormat(Calendar c, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(c.getTime());
    }

    public static String parseTime(long time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        return sdf.format(new Date(time));
    }

    public static Date parseTime(String time, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern,
                Locale.getDefault());
        try {
            return sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String parseTime(String time, String fromPattern,
                                   String toPattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(fromPattern,
                Locale.getDefault());
        try {
            Date d = sdf.parse(time);
            sdf = new SimpleDateFormat(toPattern, Locale.getDefault());
            return sdf.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String nullSafe(String content) {
        if (content == null) {
            return "";
        }

        return content;
    }

    public static String nullSafe(String content, String defaultStr) {
        if (content.isEmpty()) {
            return defaultStr;
        }

        return content;
    }

    public static String nullSafeDash(String content) {
        if (content.isEmpty()) {
            return "-";
        }

        return content;
    }

    public static String nullSafe(int content, String defaultStr) {
        if (content == 0) {
            return defaultStr;
        }

        return "" + content;
    }

    public static Typeface getRobotoRegular(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/Roboto-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Typeface getRobotoLight(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/Roboto-Thin.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Typeface getRobotoBold(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/Roboto-Bold.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Typeface getCondensedNormal(Context c) {
        try {
            return Typeface.createFromAsset(c.getAssets(),
                    "fonts/RobotoCondensed-Regular.ttf");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //TODO : Check The GPS is enable Or NOt
    public static boolean isGPSProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static boolean isNetworkProviderEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);

        return locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public static boolean isLocationProviderEnabled(Context context) {
        return (isGPSProviderEnabled(context) || isNetworkProviderEnabled(context));
    }

    public static ArrayList<String> asList(String str) {
        ArrayList<String> items = new ArrayList<String>(Arrays.asList(str
                .split("\\s*,\\s*")));

        return items;
    }

    public static String implode(ArrayList<String> data) {
        try {
            String devices = "";
            for (String iterable_element : data) {
                devices = devices + "," + iterable_element;
            }

            if (devices.length() > 0 && devices.startsWith(",")) {
                devices = devices.substring(1);
            }

            return devices;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String getExtenstion(String urlPath) {
        if (urlPath.contains(".")) {
            String extension = urlPath.substring(urlPath.lastIndexOf(".") + 1);
            return extension;
        }

        return "";
    }

    //TODO : Check the internet is connected or not
//    public static boolean isInternetConnected(Context mContext) {
//        boolean outcome = false;
//
//        try {
//            if (mContext != null) {
//                ConnectivityManager cm = (ConnectivityManager) mContext
//                        .getSystemService(Context.CONNECTIVITY_SERVICE);
//
//                NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
//
//                for (NetworkInfo tempNetworkInfo : networkInfos) {
//                    if (tempNetworkInfo.isConnected()) {
//                        outcome = true;
//                        break;
//                    }
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return outcome;
//    }


//    public static void showDialog(final Context c, String title, String message) {
//
//        android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(c)
//                .setTitle(title)
//                .setMessage(message)
//                .setPositiveButton(R.string.hint_ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton(R.string.hint_cancel, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                }).create();
//        dialog.show();
//        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
//        nbutton.setTextColor(c.getResources().getColor(R.color.colorPrimary));
//        Button ybutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
//        ybutton.setTextColor(c.getResources().getColor(R.color.colorPrimary));
//    }

    public static void clearLoginCredetials(Activity c) {
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(c).edit();
        editor.clear();
        editor.commit();
//        FacebookSdk.sdkInitialize(c);
//        LoginManager.getInstance().logOut();
    }


    public static void printHashKey(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(Constant.PACKAGE_NAME,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA");
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                md.update(signature.toByteArray());
                Log.e("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

            Log.e("hashkey_error", e.toString());
        }
    }


    public boolean emailValidator(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }


    public static void checkLocationService(Activity activity) {

        //Check whether this user has installed Google play service which is being used by Location updates.
        if (isGooglePlayServicesAvailable(activity)) {


            //Passing null to indicate that it is executing for the first time.


        } else {
            Toast.makeText(activity,activity.getResources().getString(R.string.google_service), Toast.LENGTH_LONG).show();
        }
    }

//    public static Boolean startStep2(DialogInterface dialog, Activity activity) {
//        ConnectivityManager connectivityManager
//                = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
//
//        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
//            promptInternetConnect(activity);
//            return false;
//        }
//
//
//        if (dialog != null) {
//            dialog.dismiss();
//        }

        //Yes there is active internet connection. Next check Location is granted by user or not.
//
//        if (checkPermissions(activity)) { //Yes permissions are granted by the user. Go to the next step.
//            startStep3(activity);
//        } else {  //No user has not granted the permissions yet. Request now.
//            requestPermissions(activity);
//        }
//        return true;
//    }


//    private static void promptInternetConnect(final Activity activity) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
//        builder.setTitle(R.string.app_name);
//        builder.setMessage("Internet is not avaliable");
//
//        String positiveText = "Referesh";
//        builder.setPositiveButton(positiveText,
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        //Block the Application Execution until user grants the permissions
//                        if (startStep2(dialog, activity)) {
//
//                            //Now make sure about location permission.
//                            if (checkPermissions(activity)) {
//
//                                //Step 2: Start the Location Monitor Service
//                                //Everything is there to start the service.
//                                startStep3(activity);
//                            } else if (!checkPermissions(activity)) {
//                                requestPermissions(activity);
//                            }
//
//                        }
//                    }
//                });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

    /**
     * Step 3: Start the Location Monitor Service
     */
    public static void startStep3(Activity activity) {

        //And it will be keep running until you close the entire application from task manager.
        //This method will executed only once.

        if (!mAlreadyStartedService) {

            //Start location sharing service to app server.........
            Intent intent = new Intent(activity, GetLocationServices.class);
            activity.startService(intent);

            mAlreadyStartedService = true;
            //Ends................................................
        }
    }


    /**
     * //TODO : Return the current state of the permissions needed.
     */

    public static boolean checkPermissions(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        int permissionState1 = activity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionState2 = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        return permissionState1 == PackageManager.PERMISSION_GRANTED && permissionState2 == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * TODO : Start permissions requests.
     */
    public static void requestPermissions(final Activity activity) {

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        android.Manifest.permission.ACCESS_FINE_LOCATION);

        boolean shouldProvideRationale2 =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


        // Provide an additional rationale to the img_user. This would happen if the img_user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale || shouldProvideRationale2) {
            Log.i("permission", "Displaying permission rationale to provide additional context.");
            showSnackbar(R.string.hint_cancel,
                    android.R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(activity,
                                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });
        } else {
            Log.i("permission", "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the img_user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(activity,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    public static void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(
                null,
                mainTextStringId+"",
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionStringId+"", listener).show();
    }


    public static boolean isGooglePlayServicesAvailable(Activity activity) {
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        int status = googleApiAvailability.isGooglePlayServicesAvailable(activity);
        if (status != ConnectionResult.SUCCESS) {

            if (googleApiAvailability.isUserResolvableError(status)) {
                googleApiAvailability.getErrorDialog(activity, status, 2404).show();
            }
            return false;
        }
        return true;
    }

}
