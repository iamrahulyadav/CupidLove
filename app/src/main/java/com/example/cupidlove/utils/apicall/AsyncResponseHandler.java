package com.example.cupidlove.utils.apicall;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.example.cupidlove.utils.Utils;


public abstract class AsyncResponseHandler extends AsyncHttpResponseHandler {

    //TODO : Variable Declaration
    private Activity context;

    public AsyncResponseHandler(Activity context) {
        this.context = context;
    }

    public AsyncResponseHandler(Context context) {
    }

    abstract public void onSuccess(String content);

    //
    abstract public void onFailure(Throwable e, String content);

    @Override
    public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
        try {

            String response = new String(responseBody, "UTF-8");
            try {

                if (response != null && response.length() > 0) {

//                    try {
//                        JSONArray jsonArray = new JSONArray(response);
//                        response = jsonArray.get(0).toString();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }

//                    Response res = new Gson().fromJson(response, Response.class);

//                    if (res.st == 506) {
//
//                        //Edit
//
////                        Utils.clearLoginCredetials(context);
////
////                        LocalBroadcastManager.getInstance(context)
////                                .sendBroadcast(
////                                        new Intent(Constants.FINISH_ACTIVITY));
////
////                        Intent intent = new Intent(context,
////                                LoginActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
////                                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
////                        context.startActivity(intent);
//                        return;
//                    }

                }

            } catch (Exception e) {
                Utils.sendExceptionReport(e);
            }

            onSuccess(response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable e) {
        try {
            e.printStackTrace();
            Debug.e("onFailure", "" + new String(responseBody, "UTF-8"));
            onFailure(e, "" + e.getMessage());
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }



}
