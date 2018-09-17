package com.example.cupidlove.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Bhumi Shah on 3/18/2017.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    //TODO : Variable Declartion
    private static final String TAG = "MyFirebaseIdService";

    @Override
    public void onTokenRefresh() {
        String refreshToken = FirebaseInstanceId.getInstance().getToken();
//        WelcomeActivity.device_token = refreshToken;
        Log.e("refreshToken",""+refreshToken);

        sendRegistrationToServer(refreshToken);
    }

    private void sendRegistrationToServer(String refreshToken) {

    }
}
