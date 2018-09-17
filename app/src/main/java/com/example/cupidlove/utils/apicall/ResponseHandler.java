package com.example.cupidlove.utils.apicall;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.activity.LoginOrSignUpActivity;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * Created by UV on 26-Dec-16.
 */
public class ResponseHandler extends AsyncResponseHandler implements ResponseListener {

    //Todo : Variable Declaration
    private ResponseListener responseListener;
    private String methodName;

    Activity activity;

    public ResponseHandler(Activity context, ResponseListener responseListener, String methodName) {
        super(context);
        this.activity = context;
        this.responseListener = responseListener;
        this.methodName = methodName;
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onFinish() {
        super.onFinish();
        try {
            responseListener.onResponse("", methodName);
        } catch (Exception e) {
            Log.e("ResponseHandler onFinish Exception", e.getMessage()+"");
        }
    }

    @Override
    public void onSuccess(String response) {
        try {

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("error") && jsonObject.getInt("error_code") == 101 ) {
                    //session expired--logout
                    logout();
                    responseListener.onResponse(response, methodName);
                } else {
                    responseListener.onResponse(response, methodName);
                }
            } catch (Exception e) {
                responseListener.onResponse(response, methodName);
                Log.e("erro", e.getMessage());
            }
        } catch (Exception e) {
            Log.e("ResponseHandler onSuccess Exception", e.getMessage());
        }
    }

    @Override
    public void onFailure(Throwable e, String content) {
        responseListener.onResponse("", methodName);
        Log.e("Response ", "onFailure" + e.getMessage());
    }

    //TODO : LOGOUT user
    public void logout() {

        try {
            ((BaseActivity) activity).showProgress("");

            SharedPreferences.Editor pre = ((BaseActivity) activity).getPreferences().edit();

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id",((BaseActivity) activity). getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("logout", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().SESSION_EXPIRED, params, new ResponseHandler(activity, this, "sessionExpired"));
        } catch (Exception e) {
            Debug.e("logout Exception", e.getMessage());
        }
    }


    public void onResponse(String response, String methodName) {


        Log.e("response", response);
        if (response == null || response.equals("")) {
            ((BaseActivity)activity).dismissProgress();
            return;
        } else if (methodName.equals("sessionExpired")) {
            ((BaseActivity)activity).dismissProgress();
            ((BaseActivity) activity).sessionExpiredCode(activity);
            Intent intent = new Intent(activity, LoginOrSignUpActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);

        }
    }

    @Override
    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
        super.onFailure(statusCode, headers, responseBody, e);
        Log.e("Response is ",responseBody.toString() + " and exception is "+e.getMessage());
    }

    @Override
    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
        Log.e("Response is ",responseBody.toString());
        super.onSuccess(statusCode, headers, responseBody);
    }
}
