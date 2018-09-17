package com.example.cupidlove.utils.apicall;

import android.app.Activity;
import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.example.cupidlove.R;
import com.example.cupidlove.utils.Constant;

import org.apache.http.conn.ssl.SSLSocketFactory;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.HttpsURLConnection;


public class AsyncHttpRequest extends AsyncHttpClient {

    //TODO : Variabl Declaration
    Activity activity;


    public AsyncHttpRequest(Activity activity) {
        this.activity = activity;
    }

    public static AsyncHttpClient newRequest() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.setTimeout(Constant.TIMEOUT);
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);
            MySSLSocketFactory  sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
            client.setSSLSocketFactory(sf);
        }
        catch (Exception e) {
        }
        return client;
    }



}
