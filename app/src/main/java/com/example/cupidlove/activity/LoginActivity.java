package com.example.cupidlove.activity;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cupidlove.customview.edittext.EditTextRegular;
import com.example.cupidlove.model.Login;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.Utils;
import com.example.xmpp.MyXmppService;

import org.json.JSONObject;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements ResponseListener {

    //TODO : Bind The All XML View With JAVA File
    @BindView(R.id.tvLogin)
    TextView tvLogin;

    @BindView(R.id.etEmail)
    EditTextRegular etEmail;

    @BindView(R.id.etPassword)
    EditTextRegular etPassword;

    @BindView(R.id.back)
    ImageView back;

    //TODO : VAriable Declaration
    public static MyXmppService mXmppService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        isFullButton();
        Constant.ISConnectionDone = false;
    }

    @OnClick(R.id.back)
    public void backClick() {
        onBackPressed();
    }


    //TODO: Click On SignUp Button
    @OnClick(R.id.tvSignUp)
    public void tvSignUpClick() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    //TODO: Click On Login Button
    @OnClick(R.id.tvLogin)
    public void tvLoginClick() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
        } else {
            if (Utils.isValidEmail(etEmail.getText().toString())) {
                if (etPassword.getText().toString().isEmpty()) {
                    Toast.makeText(this, getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                } else {
                    //call login
                    loginWithEmail();
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.enter_valid_email_address), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO : click On Forget password for Login
    @OnClick(R.id.tvForgetPassword)
    public void tvForgetPasswordClick() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.enter_username), Toast.LENGTH_SHORT).show();
        } else {
            if (Utils.isValidEmail(etEmail.getText().toString())) {
                //call forget password
                forgetPassword();
            } else {
                Toast.makeText(this, getResources().getString(R.string.enter_valid_email_address), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO: Show The Full Button
    public void isFullButton() {
        if (Constant.IS_FULL_BUTTON_SHOW) {
            tvLogin.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
        } else {
            tvLogin.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
        }
    }

    //TODO : Send the Link for Reset New Password On user email ID
    public void forgetPassword() {
        try {
            showProgress("");
            RequestParams params = new RequestParams();
            params.put("email", etEmail.getText().toString());

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().FORGET_PASSWORD, params, new ResponseHandler(LoginActivity.this, this, "forget_password"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    //TODO : User Login With Email ID
    public void loginWithEmail() {
        try {
            showProgress("");
            RequestParams params = new RequestParams();
            params.put("email", etEmail.getText().toString());
            params.put("password", etPassword.getText().toString());
            params.put("device", "android");

            String refreshToken = FirebaseInstanceId.getInstance().getToken();

            SharedPreferences.Editor pre = getPreferences().edit();
            pre.putString(RequestParamUtils.DEVICE_TOKEN, refreshToken);
            pre.commit();

            params.put("device_token", refreshToken);


            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().LOGIN, params, new ResponseHandler(LoginActivity.this, this, "login"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    //TODO : User Login Response
    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();

        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("forget_password")) {
            Log.e(methodName + " Response is ", response);
            try {
                JSONObject obj = new JSONObject(response);

                new AlertDialog.Builder(this)
                        .setTitle(getResources().getText(R.string.app_name))
                        .setMessage(obj.getString("message"))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

            } catch (Throwable t) {
                Debug.e("error", response);
            }

        } else if (methodName.equals("login")) {
            //login
            final Login loginRider = new Gson().fromJson(
                    response, new TypeToken<Login>() {
                    }.getType());

            if (!loginRider.error) {
                //Login Successfull
                SharedPreferences.Editor pre = getPreferences().edit();
                pre.putString(RequestParamUtils.USER_DATA, response);

                pre.putString(RequestParamUtils.LATITUDE, loginRider.loginUserDetails.locationLat + "");
                pre.putString(RequestParamUtils.LONGITUDE, loginRider.loginUserDetails.locationLong + "");

                pre.putString(RequestParamUtils.USER_ID, loginRider.loginUserDetails.id + "");
                pre.putString(RequestParamUtils.AUTH_TOKEN, loginRider.loginUserDetails.authToken + "");
                pre.putString(RequestParamUtils.FIRST_NAME, loginRider.loginUserDetails.fname + "");
                pre.putString(RequestParamUtils.EMAIL, loginRider.loginUserDetails.email + "");
                pre.putString(RequestParamUtils.EDUCATION, loginRider.loginUserDetails.education + "");
                pre.putString(RequestParamUtils.PROFETION, loginRider.loginUserDetails.profession + "");
                pre.putString(RequestParamUtils.BIRTHDATE, loginRider.loginUserDetails.dob + "");
                pre.putString(RequestParamUtils.LAST_NAME, loginRider.loginUserDetails.lname + "");
                pre.putString(RequestParamUtils.AGE, loginRider.loginUserDetails.age + "");
                Prefs.putString(RequestParamUtils.USER_ID, loginRider.loginUserDetails.id + "");
                Prefs.putString(RequestParamUtils.ROSTER_NICK_NAME, loginRider.loginUserDetails.fname + "");
                Prefs.putString(RequestParamUtils.FIRST_NAME, loginRider.loginUserDetails.ejUser + "");
                pre.putString(RequestParamUtils.DATE_PREF, loginRider.loginUserDetails.datePref);

                if (loginRider.loginUserDetails.enableAdd.equals("0")) {
                    pre.putBoolean(RequestParamUtils.ADENABLED, true);
                } else {
                    pre.putBoolean(RequestParamUtils.ADENABLED, false);
                }

//                Prefs.putString(RequestParamUtils.XMPPUSERNAME, loginRider.loginUserDetails.id + "_" + loginRider.loginUserDetails.fname);
//                Prefs.putString(RequestParamUtils.XMPPUSERPASSWORD, "potenza@123");

                if (loginRider.userGallary.img1 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img1 + "");
                } else if (loginRider.userGallary.img2 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img2 + "");
                } else if (loginRider.userGallary.img3 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img3 + "");
                } else if (loginRider.userGallary.img4 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img4 + "");
                } else if (loginRider.userGallary.img5 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img5 + "");
                } else if (loginRider.userGallary.img6 != null) {
                    pre.putString(RequestParamUtils.PROFILE_IMAGE, loginRider.userGallary.img6 + "");
                }

                pre.commit();

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
//                doBindService();


            } else {
                Toast.makeText(this, loginRider.message, Toast.LENGTH_SHORT).show();
            }
        }
    }


//    public MyXmppService getmService() {
//        return mXmppService;
//    }
}
