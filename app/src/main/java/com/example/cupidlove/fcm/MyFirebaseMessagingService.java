package com.example.cupidlove.fcm;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.SplashActivity;
import com.example.cupidlove.utils.RequestParamUtils;

import java.util.Map;

/**
 * Created by Bhumi Shah on 3/18/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    //TODO : Variable Declarion
    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.e(TAG, "recieved message =" + remoteMessage.toString());
        RemoteMessage.Notification notification = remoteMessage.getNotification();

        Map<String, String> data = remoteMessage.getData();

        Log.e(TAG, "data  = " + data.toString());

        if (data.get("type").equals("1")) {
            //go to notification
            openNotification(data);
        } else if (data.get("type").equals("2")) {
            //its a match
            ItsaMatchNotification(data);
        } else if (data.get("type").equals("3")) {
            ChatNotification(data);
            int count = Prefs.getInt(RequestParamUtils.UNREADCHAT, 0) + 1;
            Prefs.putInt(RequestParamUtils.UNREADCHAT, count);
//            addChat(data.get("friend_ejuser") + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX, data.get("message"), Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, false, null);
        } else if (data.get("type").equals("4")) {
            //admin message
            adminMessage(data);
        }else {
            offlineNotification();
        }
    }

    private void adminMessage(Map<String, String> data) {
        String message;
        message = "Message from Admin:" + data.get("body");
        Bitmap icon;
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        Uri path = Uri.parse("android.resource://com.mynearestshop.customer/raw/noti");
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", data.get("type"));
        intent.putExtra("message", data.get("body"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Cupid Love");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentInfo("");
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setContentText(message);
        notificationBuilder.setColor(Color.GRAY);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

    private void offlineNotification() {
        String message;
        message = "Message from Admin: offline";
        Bitmap icon;
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        Uri path = Uri.parse("android.resource://com.mynearestshop.customer/raw/noti");
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", "4");
        intent.putExtra("message", "offline");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Cupid Love");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentInfo("");
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setContentText(message);
        notificationBuilder.setColor(Color.GRAY);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }


    private void openNotification(Map<String, String> data) {
        String message;
        message = data.get("message");

        Bitmap icon;
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        Uri path = Uri.parse("android.resource://com.mynearestshop.customer/raw/noti");
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", data.get("type"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Cupid Love");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentInfo("");
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setContentText(message);
        notificationBuilder.setColor(Color.GRAY);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());
    }

    private void ItsaMatchNotification(Map<String, String> data) {
        String message;
        message = data.get("friend_Fname") + " matched with you";
        Bitmap icon;
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        Uri path = Uri.parse("android.resource://com.mynearestshop.customer/raw/noti");
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", data.get("type"));
        intent.putExtra("friendid", data.get("friendid"));
        intent.putExtra("friend_Fname", data.get("friend_Fname"));
        intent.putExtra(" friend_Lname", data.get("friend_Lname"));
        intent.putExtra("message", data.get("message"));
        intent.putExtra(RequestParamUtils.XMPPUSERNAME, data.get("friend_ejuser"));
        intent.putExtra("friend_profileImg_url", data.get("friend_profileImg_url"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Cupid Love");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentInfo("");
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setContentText(message);
        notificationBuilder.setColor(Color.GRAY);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);

        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }

    private void ChatNotification(Map<String, String> data) {
        String message;
        if (data.get("message").contains("$***$") && data.get("message").contains("#")) {
            message = data.get("friend_Fname") + " Send You Date Request";
        } else {
            message = data.get("friend_Fname") + " Send You Message: " + data.get("message");
        }
        Bitmap icon;
        icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        Uri path = Uri.parse("android.resource://com.mynearestshop.customer/raw/noti");
        Intent intent = new Intent(this, SplashActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("type", data.get("type"));
        intent.putExtra("friendid", data.get("friendid"));
        intent.putExtra("friend_Fname", data.get("friend_Fname"));
        intent.putExtra("friend_Lname", data.get("friend_Lname"));
        intent.putExtra("message", data.get("message"));
        intent.putExtra("message", data.get("message"));
        intent.putExtra("friend_profileImg_url", data.get("friend_profileImg_url"));
        intent.putExtra(RequestParamUtils.XMPPUSERNAME,data.get("friend_ejuser"));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
        notificationBuilder.setContentTitle("Cupid Love");
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setContentInfo("");
        notificationBuilder.setLargeIcon(icon);
        notificationBuilder.setContentText(message);
        notificationBuilder.setColor(Color.GRAY);
        notificationBuilder.setSmallIcon(R.mipmap.ic_launcher);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setLights(Color.YELLOW, 1000, 300);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notificationBuilder.build());

    }


}
