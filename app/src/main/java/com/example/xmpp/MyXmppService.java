package com.example.xmpp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.packet.Presence;

public class MyXmppService extends Service {

    //TODO : Variable Declaration
    private static final String DOMAIN = "13.59.86.207";
    //        private static final String USERNAME = "bhumi";
//    private static final String PASSWORD = "123456";
    public static ConnectivityManager cm;
    public static MyXMPP xmpp;
    public static boolean ServerchatCreated = false;
    String text = "";


    @Override
    public IBinder onBind(final Intent intent) {
        return new LocalBinder<MyXmppService>(this);
    }

    public Chat chat;

    @Override
    public void onCreate() {
        super.onCreate();
        cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Prefs.getString(RequestParamUtils.XMPPUSERNAME, "").equals("") || Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") == null) {
            String uname = Prefs.getString(RequestParamUtils.USER_ID, "") + "_" + Prefs.getString(RequestParamUtils.FIRST_NAME, "") + Constant.XMPP_USERNAME_POSTFIX;
            xmpp = MyXMPP.getInstance(MyXmppService.this, DOMAIN, uname, Constant.XMPP_PASSWORD);
        } else {
            xmpp = MyXMPP.getInstance(MyXmppService.this, DOMAIN, Prefs.getString(RequestParamUtils.XMPPUSERNAME, ""), Prefs.getString(RequestParamUtils.XMPPUSERPASSWORD, ""));
        }
        xmpp.connect("onCreate");
    }

    @Override
    public int onStartCommand(final Intent intent, final int flags,
                              final int startId) {
        return Service.START_NOT_STICKY;
    }

    @Override
    public boolean onUnbind(final Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {

        Presence presence = new Presence(Presence.Type.unavailable);
        try {
//            xmpp.connection.sendStanza(presence);
            if (xmpp.connection.isConnected()) {
                Log.e("Disconnect ","Presence send");
                xmpp.connection.sendStanza(presence);
                xmpp.connection.disconnect();
            }
        } catch (Exception exception) {
            Log.e(" Conn close exception", exception.getMessage());
            if (xmpp.connection.isConnected()) {
                xmpp.connection.disconnect();
            }
        }

        super.onDestroy();


    }

    public static boolean isNetworkConnected() {
        return cm.getActiveNetworkInfo() != null;
    }
}
