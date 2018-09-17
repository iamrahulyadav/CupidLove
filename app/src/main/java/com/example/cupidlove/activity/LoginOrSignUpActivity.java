package com.example.cupidlove.activity;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.firebase.crash.FirebaseCrash;
//import com.potenza.multidesignss.R;
//import com.potenza.multidesignss.adapter.ListDialogAdapter;
//import com.potenza.multidesignss.interfaces.OnItemClickListner;
//import com.potenza.multidesignss.model.ListItem;
//import com.potenza.multidesignss.utils.BaseActivity;
//import com.potenza.multidesignss.utils.Constant;

import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.model.AllLanguage;
import com.example.cupidlove.model.Register;
import com.example.cupidlove.utils.BaseSignUpActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.Utils;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.example.cupidlove.utils.URLS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginOrSignUpActivity extends BaseSignUpActivity implements ResponseListener {

    //TODO: Bind The All XML View With Java File
    @BindView(R.id.fb_login_button)
    LoginButton fb_login_button;

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    @BindView(R.id.layoutDots)
    LinearLayout layoutDots;

    @BindView(R.id.tvChooseLanguage)
    TextViewRegular tvChooseLanguage;

    @BindView(R.id.npLanguage)
    NumberPicker npLanguage;

    @BindView(R.id.llLanguages)
    LinearLayout llLanguages;

    //TODO : VAriable Declaration
    CallbackManager callbackManager;
    private TextView[] dots;
    private int[] layouts;
    private MyViewPagerAdapter myViewPagerAdapter;
    private int currentPosition;
    //    private AlertDialog alertDialog;
    List<String> listLanguage = new ArrayList<>();
    String selectedLang="";
    String AGEUSER;

    List<AllLanguage.Language> langList = new ArrayList<>();
    int selectedLanguagePosition = 0;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_sign_up);
        ButterKnife.bind(this);
        Constant.ISConnectionDone = false;
        setScreenLayoutDirection();
        callbackManager = CallbackManager.Factory.create();
        loginWithFB();

        callAllLanguages();
        setView();
        autoScroll();
    }


    //TODO: Location Service Is Not Avalable
    @Override
    public void onResume() {
        super.onResume();
        Utils.checkLocationService(this);
    }

    //TODO: Get The Permission For Access User Location
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("3err", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("", "Permission granted, updates requested, starting location updates");
                Utils.startStep3(this);
                buildGoogleApiClient();
            } else {
                // Permission denied.
                Toast.makeText(this, "Allow permission to access your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO : Auto Scroll Method
    public void autoScroll() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPosition == myViewPagerAdapter.getCount() - 1) {
                            currentPosition = 0;
                        } else {
                            currentPosition = currentPosition + 1;
                        }
                        view_pager.setCurrentItem(currentPosition);
                        addBottomDots(currentPosition);
                        autoScroll();
                    }
                }, 1000);

            }
        }, 1000);
    }

    private void setView() {
        layouts = new int[]{
                R.drawable.slider1,
                R.drawable.slider2,
                R.drawable.slider3,
                R.drawable.slider4};

        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        view_pager.setAdapter(myViewPagerAdapter);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        layoutDots.removeAllViews();
        dots = new TextView[layouts.length];

        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparent_white));
            layoutDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.white));
    }


    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_image, container, false);
            ImageView imageView = view.findViewById(R.id.ivImage);
            imageView.setImageResource(layouts[position]);
            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    //TODO : click Login
    @OnClick(R.id.tvLogin)
    public void tvLoginClick() {
        Intent intent = new Intent(LoginOrSignUpActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    //TODO : When Select FaceBook Login
    @OnClick(R.id.llFacebookLogin)
    public void llFacebookLoginClick() {
        LoginManager.getInstance().logInWithReadPermissions(
                this, Arrays.asList("user_photos", "email", "public_profile")
//                Arrays.asList("user_photos", "email", "user_birthday", "public_profile", "user_education_history", "user_work_history")
        );
    }

    //TODO : When Select Email Login
    @OnClick(R.id.llEmailLogin)
    public void tvEmailClick() {
        Intent intent = new Intent(LoginOrSignUpActivity.this, LoginActivity.class);
        startActivity(intent);
    }

//    @OnClick(R.id.tvChooseLanguage)
//    public void tvChooseLanguageClick() {
//        showLanguageDialog();
//    }

//    private void showLanguageDialog() {
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(LoginOrSignUpActivity.this);
//        LayoutInflater inflater = LoginOrSignUpActivity.this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.layout_list_dialog, null);
//        dialogBuilder.setView(dialogView);
//        OnItemClickListner onItemClickListner = new OnItemClickListner() {
//            @Override
//            public void onItemClick(int position) {
//                alertDialog.dismiss();
//                Constant.LANGUAGE = list.get(position).getOther();
//                setLocale(list.get(position).getOther());
//            }
//        };
//        RecyclerView recyclerView = dialogView.findViewById(R.id.rvList);
//        ListDialogAdapter dialogAdapter = new ListDialogAdapter(LoginOrSignUpActivity.this, onItemClickListner);
//        dialogAdapter.addAll(list);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
//        recyclerView.setLayoutManager(mLayoutManager);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//        recyclerView.setAdapter(dialogAdapter);
//
//
//        alertDialog = dialogBuilder.create();
//        Window window = alertDialog.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        window.setGravity(Gravity.CENTER);
//
//        alertDialog.show();
//    }

    //TODO: When Select Language
    @OnClick(R.id.tvChooseLanguage)
    public void tvChooseLanguageClick() {
        llLanguages.setVisibility(View.VISIBLE);
    }

    //TODO : When Click On Cancel Button
    @OnClick(R.id.tvCancel)
    public void tvCancelClick() {
        llLanguages.setVisibility(View.GONE);
    }

    //TODO : When Select Language Then Click On Done Button
    @OnClick(R.id.tvDone)
    public void tvDoneClick() {

        if(selectedLang == null) {
            selectedLang="";
        }

        if (selectedLang.equals("")) {
            selectedLang = "en";
        }

        SharedPreferences.Editor pre = getPreferences().edit();
        pre.putString(RequestParamUtils.LANGUAGE, selectedLang);
        pre.commit();

        Prefs.putString(RequestParamUtils.LANGUAGE, selectedLang);
        if (selectedLang != null) {
            if (selectedLang.equalsIgnoreCase("French"))
                setLocale("fr");
            else if (selectedLang.equalsIgnoreCase("Russian"))
                setLocale("ru");

            else if (selectedLang.equalsIgnoreCase("arabic"))
                setLocale("ar");

            else if (selectedLang.equalsIgnoreCase("hindi"))
                setLocale("hi");

            else setLocale("en");
        } else {
            setLocale("en");
        }
        if (Constant.isRtl == 0) {
            Prefs.putBoolean(RequestParamUtils.IS_RTL, false);
        } else {
            Prefs.putBoolean(RequestParamUtils.IS_RTL, true);
        }

        llLanguages.setVisibility(View.GONE);//set default selected language for app
    }

    //TODO : Which Langugae Seleceted that Language Data set
    public void setLanguageData() {

        final String[] language = listLanguage.toArray(new String[listLanguage.size()]);
        npLanguage.setMinValue(0); //from array first value
        npLanguage.setMaxValue(language.length - 1); //to array last value
        npLanguage.setDisplayedValues(language);
        npLanguage.setWrapSelectorWheel(false);
        npLanguage.setValue(selectedLanguagePosition);

        npLanguage.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

                Log.e("Selceted value", language[newVal]);
                Constant.isRtl = Integer.parseInt(langList.get(newVal).rtl);
                selectedLang = language[newVal];
                selectedLanguagePosition = newVal;
                //Display the newly selected value from picker
//                tv.setText("Selected value : " + values[newVal]);
            }
        });
    }

    //TODO: Login With Face Book
    public void loginWithFB() {

        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String accessToken = loginResult.getAccessToken().getToken();
                Log.i("accessToken", accessToken);

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                Log.e("LoginActivity", response.toString());
                                loginWithFacebook(object);
                                //TODO:Facebook login is from here
                            }
                        });
                Bundle parameters = new Bundle();
//                parameters.putString("fields", "picture, email, id,first_name, last_name, birthday,education,work,interested_in, gender");
                parameters.putString("fields", "picture, email, id,first_name, last_name,interested_in, gender");

                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // App code
                Toast.makeText(LoginOrSignUpActivity.this, getResources().getString(R.string.login_canceled), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Toast.makeText(LoginOrSignUpActivity.this, getResources().getString(R.string.something_went_wrong_try_after_somtime), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    //TODO : Set The FaceBook Data On application
    public void loginWithFacebook(JSONObject jsonObject) {
        try {
            showProgress("");
            RequestParams params = new RequestParams();

            params.put("Status", "0");
            params.put("about", "About Me");
            params.put("access_location", "1");
            params.put("device", "android");

            String refreshToken = FirebaseInstanceId.getInstance().getToken();
            params.put("device_token", refreshToken);

            if (jsonObject.has("work")) {
                if (jsonObject.getString("birthday") != null) {
                    //birthday in mm/dd/yyyy format
                    String birthday = jsonObject.getString("birthday");
                    int mm = Integer.parseInt(birthday.substring(0, birthday.indexOf("/")));
                    int dd = Integer.parseInt(birthday.substring(birthday.indexOf("/") + 1, birthday.lastIndexOf("/")));
                    int yyyy = Integer.parseInt(birthday.substring(birthday.lastIndexOf("/") + 1, birthday.length()));
                    String bdate = dd + "/" + mm + "/" + yyyy;
                    AGEUSER = getAge(yyyy, mm, dd);
                    params.put("dob", bdate);
                } else {
                    params.put("dob", "-");
                }
            } else {
                params.put("dob", "-");
            }
            if (jsonObject.has("education")) {

                if (jsonObject.getJSONArray("education") != null) {
                    JSONArray jsonArray = jsonObject.getJSONArray("education");
                    String education = jsonArray.getJSONObject(jsonArray.length() - 1).getJSONObject("school").getString("name");
                    params.put("education", education);
                } else {
                    params.put("education", "-");
                }
            } else {
                params.put("education", "-");
            }
            params.put("email", jsonObject.getString("email") + "");
            params.put("fb_id", jsonObject.getString("id") + "");
            params.put("fname", jsonObject.getString("first_name") + "");
            params.put("gender", jsonObject.getString("gender") + "");
            if (jsonObject.getString("gender") != null) {
                if (jsonObject.getString("gender").toLowerCase().equals("male")) {
                    params.put("gender_pref", "female");
                } else {
                    params.put("gender_pref", "male");
                }
            } else {
                params.put("gender_pref", "female");
            }
            params.put("lname", jsonObject.getString("last_name") + "");
            params.put("location_lat", getPreferences().getString(RequestParamUtils.LATITUDE, ""));
            params.put("location_long", getPreferences().getString(RequestParamUtils.LONGITUDE, ""));

            if (jsonObject.has("work")) {
                if (jsonObject.getString("work") != null) {
                    String profession = jsonObject.getJSONArray("work").getJSONObject(0).getJSONObject("position").getString("name");
                    params.put("profession", profession + "");
                } else {
                    params.put("profession", "-");
                }
            } else {
                params.put("profession", "-");
            }
            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().LOGIN_WITH_FACEBOOK, params, new ResponseHandler(this, this, "loginwithfacebook"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //TODO : When Select Language
    private void callAllLanguages() {
        try {

            showProgress("");
            RequestParams params = new RequestParams();

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().ALL_LANGUAGES, params, new ResponseHandler(LoginOrSignUpActivity.this, this, "all_languages"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    //TODO : Set DeFault Language
    private void callDefaultLanguages() {
        try {

            showProgress("");
            RequestParams params = new RequestParams();

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().DEFAULT_LANGUAGES, params, new ResponseHandler(LoginOrSignUpActivity.this, this, "default_Language"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    //TODO : Response Of Login With FacebBook
    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();

        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("loginwithfacebook")) {

            //login with fb
            Log.e("fb", response);

            final Register registerRider = new Gson().fromJson(
                    response, new TypeToken<Register>() {
                    }.getType());

            if (!registerRider.error) {

                SharedPreferences.Editor pre = getPreferences().edit();
                pre.putString(RequestParamUtils.USER_DATA, response);
                pre.putString(RequestParamUtils.USER_ID, registerRider.body.id + "");
                pre.putString(RequestParamUtils.AUTH_TOKEN, registerRider.body.authToken + "");
                pre.putString(RequestParamUtils.FIRST_NAME, registerRider.body.fname + "");
                pre.putString(RequestParamUtils.LAST_NAME, registerRider.body.lname + "");
                pre.putString(RequestParamUtils.EMAIL, registerRider.body.email + "");
                pre.putString(RequestParamUtils.EDUCATION, registerRider.body.education + "");
                pre.putString(RequestParamUtils.PROFETION, registerRider.body.profession + "");
                pre.putString(RequestParamUtils.BIRTHDATE, registerRider.body.dob + "");
                pre.putString(RequestParamUtils.AGE, AGEUSER + "");
                pre.commit();


                Prefs.putString(RequestParamUtils.USER_ID, registerRider.body.id + "");
                Prefs.putString(RequestParamUtils.FIRST_NAME, registerRider.body.fname + "");

                if (registerRider.body.newUser.equals("1")) {

                    pre.putString(RequestParamUtils.REGISTER, "register");
                    pre.commit();

                    Intent intent = new Intent(this, DatePreferenceActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                }

            } else {
                Toast.makeText(this, registerRider.message, Toast.LENGTH_SHORT).show();
            }


        } else if (methodName.equals("all_languages")) {
            Log.e(methodName + " Response is ", response);

            final AllLanguage allLaunguageRider = new Gson().fromJson(
                    response, new TypeToken<AllLanguage>() {
                    }.getType());

            langList = allLaunguageRider.language;

            for (int i = 0; i < allLaunguageRider.language.size(); i++) {
                listLanguage.add(allLaunguageRider.language.get(i).name.substring(0, 1).toUpperCase() + allLaunguageRider.language.get(i).name.substring(1));
            }
            setLanguageData();
            callDefaultLanguages();

        } else if (methodName.equals("default_Language")) {
            //setDefault language
            Log.e(methodName + " Response is ", response);

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {

                    Prefs.putString(RequestParamUtils.LANGUAGE, jsonObject.getString("language"));
                    SharedPreferences.Editor pre = getPreferences().edit();
                    pre.putString(RequestParamUtils.LANGUAGE, jsonObject.getString("language"));
                    pre.commit();

                    getPreferences().getString(RequestParamUtils.LANGUAGE, "en");
                    selectedLang=jsonObject.getString("language");

                    Boolean b;
                    if (jsonObject.getString("rtl").equals("0")) {
                        b = false;
                    } else {
                        b = true;
                    }
                    Prefs.putBoolean(RequestParamUtils.IS_RTL, b);

                    if (jsonObject.getString("language") != null) {
                        if (jsonObject.getString("language").equalsIgnoreCase("French"))
                            setLocale("fr");
                        else if (jsonObject.getString("language").equalsIgnoreCase("Russian"))
                            setLocale("ru");

                        else if (jsonObject.getString("language").equalsIgnoreCase("Arabic"))
                            setLocale("ar");

                        else if (jsonObject.getString("language").equalsIgnoreCase("Hindi"))
                            setLocale("hi");

                        else
                            setLocale("en");
                    } else {
                        setLocale("en");
                    }

                    llLanguages.setVisibility(View.GONE);//set default selected language for app
                } else {
                    callDefaultLanguages();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }


    public void setLocale(String lang) {
        Log.e("Locale", "Login");
        String languageToLoad = lang; // your language
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getApplicationContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        this.setContentView(R.layout.activity_login_or_sign_up);
        ButterKnife.bind(this);
        setLanguageData();
        loginWithFB();
        setView();
        setScreenLayoutDirection();

        if (langList.size() == 1) {
            tvChooseLanguage.setVisibility(View.GONE);
        } else {
            tvChooseLanguage.setVisibility(View.VISIBLE);
        }
    }
}


