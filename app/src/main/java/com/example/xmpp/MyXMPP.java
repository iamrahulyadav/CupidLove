package com.example.xmpp;

/**
 * Created by Bhumi Shah on 4/28/2017.
 */

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.format.DateFormat;
import android.util.Log;

import com.google.gson.Gson;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.model.ChatMessage;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.ChatListner;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.LocalChatListner;
import com.example.cupidlove.utils.MyApplication;
import com.example.cupidlove.utils.RequestParamUtils;

import org.jivesoftware.smack.AbstractXMPPConnection;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.ReconnectionManager;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManager;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smack.roster.SubscribeListener;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.chatstates.ChatStateListener;
import org.jivesoftware.smackx.chatstates.ChatStateManager;
import org.jivesoftware.smackx.chatstates.packet.ChatStateExtension;
import org.jivesoftware.smackx.iqregister.AccountManager;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager;
import org.jivesoftware.smackx.receipts.DeliveryReceiptManager.AutoReceiptMode;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.BareJid;
import org.jxmpp.jid.DomainBareJid;
import org.jxmpp.jid.EntityBareJid;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.jid.parts.Localpart;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Pattern;


public class MyXMPP extends BaseActivity implements PingFailedListener {

    //TODO : Variable Declaration
    public static boolean connected = false;
    public boolean loggedin = false;
    public static boolean isconnecting = false;
    public static boolean isToasted = true;
    private boolean chat_created = false;
    private String serverAddress;
    public static AbstractXMPPConnection connection;
    public static String loginUser;
    public static String passwordUser;
    Gson gson;
    MyXmppService context;
    //    Internet mcontext;
    public static MyXMPP instance = null;
    public static boolean instanceCreated = false;
    public static String userType;

    //    public static ArrayList<ChatMessage> arrayListrecive;
    private XMPPTCPConnectionConfiguration config = null;
    public static DomainBareJid serviceName;


    public ChatListner chatListner;
    public ChatStateListner chatStateListner;
    public LocalChatListner localChatListner;

    public static String Tag = "XMPP :MyXMpp :-";
//    boolean isRec = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MyXMPP(MyXmppService context, String serverAdress, String logiUser,
                  String passwordser) {
        this.serverAddress = serverAdress;
        this.loginUser = logiUser;
        this.passwordUser = passwordser;
        this.context = context;

//        arrayListrecive = new ArrayList<ChatMessage>();
        init();

    }

    public void addChatLister(ChatListner chatListner) {
        this.chatListner = chatListner;
    }

    public void addChatStateLister(ChatStateListner chatStateListner) {
        this.chatStateListner = chatStateListner;
    }

    public void removeChatLister() {
        if (this.chatListner != null) {
            this.chatListner = null;
        }

    }

    public void addChatLister(LocalChatListner localChatListner) {
        this.localChatListner = localChatListner;
    }


    public static MyXMPP getInstance(MyXmppService context, String server,
                                     String user, String pass) {

        if (instance == null) {
            instance = new MyXMPP(context, server, user, pass);
            instanceCreated = true;

        }
        return instance;
    }


    public org.jivesoftware.smack.chat.Chat Mychat;

    ChatManagerListenerImpl mChatManagerListener;
    MMessageListener mMessageListener;
    String text = "";
    String mMessage = "", mReceiver = "";

    static {
        try {
            Class.forName("org.jivesoftware.smack.ReconnectionManager");
        } catch (ClassNotFoundException ex) {
            // problem loaXding reconnection manager
            Log.e(Tag + "app", "Problem in connecting the server");
        }
    }

    public void init() {
        gson = new Gson();
        mMessageListener = new MMessageListener(context);

        mChatManagerListener = new ChatManagerListenerImpl();

        initialiseConnection();

    }

    private void initialiseConnection() {

        try {
//            52.36.137.45
            InetAddress addr = InetAddress.getByName(Constant.XMPP_HOST);
//            serviceName = JidCreate.domainBareFrom("ec2-54-70-184-29.us-west-2.compute.amazonaws.com");
            serviceName = JidCreate.domainBareFrom(Constant.NAME_POSTFIX.replace("@", ""));
            Log.e(Tag + "Service name id", serviceName.toString() + " ");

            config = XMPPTCPConnectionConfiguration.builder()
                    .setHost(Constant.XMPP_HOST)
                    .setHostAddress(addr)
                    .setPort(Constant.PORT)
                    .setXmppDomain(serviceName)
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
                    .setDebuggerEnabled(true).build();

//            InetAddress addr = InetAddress.getByName("192.168.11.28");
//            serviceName = JidCreate.domainBareFrom("bhumi.potenza.local");
//            config = XMPPTCPConnectionConfiguration.builder()
//                    .setHost("192.168.11.28")
//                    .setHostAddress(addr)
//                    .setPort(5222)
//                    .setXmppDomain(serviceName)
//                    .setSecurityMode(ConnectionConfiguration.SecurityMode.disabled)
//                    .setDebuggerEnabled(true).build();

        } catch (XmppStringprepException e) {
            e.printStackTrace();
            Log.e(Tag + "appCon", e.toString());
        } catch (UnknownHostException e) {
            e.printStackTrace();
            Log.e(Tag + "appCon1", e.toString());
        }


//        XMPPTCPConnectionConfiguration.Builder config = XMPPTCPConnectionConfiguration
//                .builder();
//        config.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);
//        config.setServiceName(serverAddress);
//        config.setHost("52.36.137.45");
//        config.setPort(5222);
//        config.setDebuggerEnabled(true);

        XMPPTCPConnection.setUseStreamManagementResumptiodDefault(true);
        XMPPTCPConnection.setUseStreamManagementDefault(true);

        connection = new XMPPTCPConnection(config);
        connection.setPacketReplyTimeout(100000);

        XMPPConnectionListener connectionListener = new XMPPConnectionListener();
        connection.addConnectionListener(connectionListener);
    }

    public void disconnect() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection.disconnect();
            }
        }).start();


    }

    public void connect(final String caller) {

        AsyncTask<Void, Void, Boolean> connectionThread = new AsyncTask<Void, Void, Boolean>() {
            @Override
            protected synchronized Boolean doInBackground(Void... arg0) {
                if (connection.isConnected())
                    return false;
                isconnecting = true;
                Log.e(Tag + "Connect() Function", caller + "=>connecting....");

                try {
                    connection.connect();

                    DeliveryReceiptManager dm = DeliveryReceiptManager
                            .getInstanceFor(connection);
                    dm.setAutoReceiptMode(AutoReceiptMode.always);
                    dm.addReceiptReceivedListener(new ReceiptReceivedListener() {

                        @Override
                        public void onReceiptReceived(Jid fromJid, Jid toJid, String receiptId, Stanza receipt) {

                        }

                    });

                    final Roster roster = Roster.getInstanceFor(MyXMPP.connection);
                    roster.addRosterListener(new RosterListener() {
                        @Override
                        public void entriesAdded(Collection<Jid> addresses) {
                            for (Jid entry : addresses) {
//                                Log.e(Tag + "Roster Entry  ", "entriesAdded" + String.valueOf(entry));
                            }
                        }

                        @Override
                        public void entriesUpdated(Collection<Jid> addresses) {
                            for (Jid entry : addresses) {
//                                Log.e(Tag + "Roster Entry  ", "entriesUpdated" + String.valueOf(entry));
                            }

                        }

                        @Override
                        public void entriesDeleted(Collection<Jid> addresses) {
                            for (Jid entry : addresses) {
//                                Log.e(Tag + "Roster Entry  ", "entriesDeleted" + String.valueOf(entry));
                            }

                        }

                        @Override
                        public void presenceChanged(Presence presence) {

                            if (Constant.chatListFragment != null) {
                                Constant.chatListFragment.rosterChage();
                            }
//                            Log.e(Tag + "Presence Change  ", "entriesDeleted" + presence.getStatus());
                        }


                    });

                    roster.addSubscribeListener(new SubscribeListener() {

                        @Override
                        public SubscribeAnswer processSubscribe(Jid from, Presence subscribeRequest) {

                            Presence subscribed = new Presence(Presence.Type.subscribed);
                            subscribed.setTo(from);
                            try {
                                connection.sendStanza(subscribed);
                                roster.createEntry((BareJid) from, Prefs.getString(RequestParamUtils.ROSTER_NICK_NAME, ""), null);
//                                Log.e(Tag + "Roster", "SubscribeAnswer" + String.valueOf(from));
                            } catch (Exception e) {
                                Log.e(Tag + "Roster Exception", e.getMessage());
                                return null;
                            }
                            return null;
                        }
                    });

                    connected = true;


                } catch (IOException e) {

                    Log.e(Tag + "(" + caller + ")", "IOException: " + e.getMessage());
                } catch (SmackException | XMPPException | InterruptedException e) {
                }
                return isconnecting = false;
            }
        };
        connectionThread.execute();
    }

    public void login() {

        AsyncTask<Void, Void, Void> loginThread = new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Constant.XMPPIALOG != null) {
                            Constant.XMPPIALOG.showCustomDialog();
                        }

                    }
                });

            }

            @Override
            protected Void doInBackground(Void... voids) {
                if (!connection.isConnected()) {
                    connect("Login");
                }


                if (Prefs.getString(RequestParamUtils.XMPPUSERNAME, "").equals("") || Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") == null) {
                    createAccount();
                    Log.e(Tag + "app", "a/c created");
                } else {
                    loginUser = Prefs.getString(RequestParamUtils.XMPPUSERNAME, "def").toLowerCase();
                    passwordUser = Prefs.getString(RequestParamUtils.XMPPUSERPASSWORD, "def");

                    if (loginUser.equals("")) {
                        loginUser = "def";
                    }
                    if (passwordUser.equals("")) {
                        passwordUser = "def";
                    }
                    Log.e(Tag + "login user id :-  " + loginUser, " & Password is :- " + passwordUser);
                    SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
                    SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
                    SASLAuthentication.blacklistSASLMechanism("SCRAM-SHA-1");
                    try {
                        connection.login(loginUser, passwordUser);
                    } catch (XMPPException e) {
                        Log.e(Tag + "XMPPException", "Login  Exception :- " + e.getMessage());
                        createAccount();
                    } catch (SmackException e) {

                        Log.e(Tag + "SmackException", "Login  Exception :- " + e.getMessage());
                    } catch (IOException e) {
                        Log.e(Tag + "IOException", "Login  Exception :- " + e.getMessage());
                    } catch (InterruptedException e) {
                        Log.e(Tag + "InterruptedException", "Login  Exception :- " + e.getMessage());
                    }
                    Log.e(Tag + "LOGIN", "Successfully");

                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Constant.XMPPIALOG != null) {
                            Constant.XMPPIALOG.dissmissDialog();
                        }
                    }
                });

            }
        };
        loginThread.execute();


    }

    public void createAccount() {
        try {
            String uname = Prefs.getString(RequestParamUtils.FIRST_NAME, "").toLowerCase() + Constant.XMPP_USERNAME_POSTFIX;
//                    String uname = "122_donavon";
            Localpart lp = Localpart.from(uname);
            AccountManager accountManager = AccountManager.getInstance(connection);
            accountManager.sensitiveOperationOverInsecureConnection(true);
            accountManager.createAccount(lp, Constant.XMPP_PASSWORD);
            Prefs.putString(RequestParamUtils.XMPPUSERNAME, uname);
            loginUser = Prefs.getString(RequestParamUtils.XMPPUSERNAME, "");
            passwordUser = Prefs.getString(RequestParamUtils.XMPPUSERPASSWORD, "");
            Prefs.putString(RequestParamUtils.XMPPUSERPASSWORD, Constant.XMPP_PASSWORD);
            Log.e(Tag + "Create", "Account : Created");
            login();
        } catch (XmppStringprepException e) {
            Log.e(Tag + "XmppStringprepException", "createAccount Error :- " + e.getMessage());
        } catch (NotConnectedException e) {
            Log.e(Tag + "NotConnectedException", "createAccount Error :- " + e.getMessage());
        } catch (XMPPException e) {
            Log.e(Tag + "XMPPException", "createAccount Error :- " + e.getMessage());
            Prefs.putString(RequestParamUtils.XMPPUSERNAME, Prefs.getString(RequestParamUtils.FIRST_NAME, "").toLowerCase() + Constant.XMPP_USERNAME_POSTFIX);
            loginUser = Prefs.getString(RequestParamUtils.XMPPUSERNAME, "");
            passwordUser = Prefs.getString(RequestParamUtils.XMPPUSERPASSWORD, "");
            Prefs.putString(RequestParamUtils.XMPPUSERPASSWORD, Constant.XMPP_PASSWORD);
            login();
        } catch (SmackException e) {
            Log.e(Tag + "SmackException", "createAccount Error :- " + e.getMessage());
        } catch (InterruptedException e) {
            Log.e(Tag + "InterruptedException", "createAccount Error :- " + e.getMessage());
        }
    }

    @Override
    public void pingFailed() {
        Log.e(Tag + "app", "Listener pingFailed");
    }

    private class ChatManagerListenerImpl implements ChatManagerListener {
        @Override
        public void chatCreated(final org.jivesoftware.smack.chat.Chat chat,
                                final boolean createdLocally) {
            if (!createdLocally)
                chat.addMessageListener(mMessageListener);


        }

    }

    //TODO : SEND Message
    public void sendMessageState(String receiver, ChatState chatState) throws XmppStringprepException {
        EntityBareJid jid = null;
        try {
            jid = JidCreate.entityBareFrom(receiver);
        } catch (XmppStringprepException e) {
            Log.e(Tag + "sendMessage XmppStringprepException", e.getMessage());
        }


        if (!chat_created) {
            Mychat = ChatManager.getInstanceFor(connection).createChat(
                    jid,
                    mMessageListener);
            chat_created = true;
        }

        final ChatStateManager chatStateManager = ChatStateManager.getInstance(MyXMPP.connection);
        if (connection != null) {
            try {
                Chat Mychat = ChatManager.getInstanceFor(MyXMPP.connection).createChat(
                        JidCreate.entityBareFrom(receiver),
                        mMessageListener);
                chatStateManager.setCurrentState(chatState, Mychat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //TODO : SEND Message
    public void sendMessage(ChatMessage chatMessage) throws XmppStringprepException {
        boolean isUpdate = false;
        String Request = "";
        String mtime = (String) DateFormat.format("hh:mm aa", Calendar.getInstance().getTime());
        String mdate = (new SimpleDateFormat("MMM d,yyyy")).format(new Date());
        chatMessage.mdate = mdate;
        chatMessage.mtime = mtime;
        EntityBareJid jid = null;
        Log.e(Tag + "sendMessage to name is ", chatMessage.receiver);
//        toName = "1232_renelle@" + Constant.XMPP_HOST;
//        toName=Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX;
        try {
            jid = JidCreate.entityBareFrom(chatMessage.receiver);
        } catch (XmppStringprepException e) {
            Log.e(Tag + "sendMessage XmppStringprepException", e.getMessage());
        }

        Mychat = ChatManager.getInstanceFor(connection).createChat(
                jid,
                mMessageListener);
//        if (!chat_created) {
//            Mychat = ChatManager.getInstanceFor(connection).createChat(
//                    jid,
//                    mMessageListener);
//            chat_created = true;
//        }

        final Message message = new Message();

        if (chatMessage.messagetype == 1) {
            message.setBody(chatMessage.body);
        } else if (chatMessage.messagetype == 3 || chatMessage.messagetype == 4) {
            String body = chatMessage.body;
            String id = "";
            try {
                JSONObject jsonObject = new JSONObject(body);
                id = jsonObject.getString("PlaceId");
                isUpdate = jsonObject.getBoolean("isUpdate");
                chatMessage.dateid = id;
                Request = jsonObject.getString("Request");
                body = "$***$" + id + "$***$" + chatMessage.body + "#" + jsonObject.getString("Request");

            } catch (JSONException e) {
                Log.e(Tag + "sendMessage jsonException is", e.getMessage());
            }
            message.setBody(body);
        }


        Log.e(Tag + "sendMessage body", message.getBody());

        try {
            if (connection.isAuthenticated()) {
                Mychat.sendMessage(message);

                Log.e(Tag + "sendMessage Is Update ", isUpdate + "");
                if (isUpdate) {

                    JSONObject object = new JSONObject(chatMessage.body);
                    object.put("Request", Request);
                    chatMessage.body = object.toString();
                    MyApplication.mydb.updateChat(chatMessage);
                    if (chatListner != null) {
                        if (Request.equals("1")) {
                            chatListner.setEventInCalender(chatMessage);
                        }
                    }
                    Log.e(Tag + "sendMessage Update Chat", MyApplication.mydb.updateChat(chatMessage) + "Date Id is = " + chatMessage.dateid);
                    if (Request.equals("1")) {
                        chatMessage.body = "Request Accepted";
                    } else if (Request.equals("0")) {
                        chatMessage.body = "Request Declined";
                    }

                    chatMessage.messagetype = 1;
                    chatMessage.dateid = "";
                    chatMessage.msgid = (int) MyApplication.mydb.insertChat(chatMessage);
                    if (chatListner != null) {
                        chatListner.onChat(chatMessage, false);
                    }
                } else {
                    MyApplication.mydb.insertChat(chatMessage);
                    if (chatListner != null) {
                        chatListner.onChat(chatMessage, true);
                    }
                }

                Log.e(Tag + "Message  ", "sent");

            } else {
                login();

            }
        } catch (NotConnectedException e) {
            Log.e(Tag + "NotConnectedException", e.getMessage());

        } catch (Exception e) {
            Log.e(Tag + "Exception",
                    "msg Not sent!" + e.getMessage());
        }

    }

    private class MMessageListener implements ChatMessageListener, ChatStateListener {

        public MMessageListener(Context contxt) {

            Log.e("Message Listner ", "Called");
        }


        @Override
        public void processMessage(final org.jivesoftware.smack.chat.Chat chat,
                                   final Message message) {

            Log.e(Tag + "Process ", "Message Called");
            String mtime = (String) DateFormat.format("hh:mm aa", Calendar.getInstance().getTime());

            String mdate = (new SimpleDateFormat("MMM d,yyyy")).format(new Date());

            if (message.getBody() != null) {
                ChatMessage chatMessage = new ChatMessage();
                Log.e(Tag + "Xmpp message received", message.toString());
                Log.e(Tag + "Message is", message.getBody().toString());

                if (message.getBody().contains("$***$") && message.getBody().contains("#")) {
                    String[] bodyarray = message.getBody().split("#");
                    if (bodyarray.length > 0) {
                        String temp = bodyarray[0];
                        if (temp.contains("$***$")) {

                            String[] temparray = temp.split(Pattern.quote("$***$"));

                            chatMessage.dateid = temparray[1];
                            String jsonobject = temp.substring(temp.indexOf("{"), (temp.indexOf("}")) + 1);
                            chatMessage.body = jsonobject;

                        }

                        String[] array = message.getFrom().toString().split("/");
                        if (array.length > 0) {
                            chatMessage.sender = array[0];
                        } else {
                            chatMessage.sender = message.getFrom().toString();
                        }

                        chatMessage.receiver = message.getTo().asBareJid().toString();
                        chatMessage.messagetype = 4;
                        chatMessage.mdate = mdate;
                        chatMessage.mtime = mtime;


                    }
                    if (bodyarray.length > 1) {

                        try {
                            JSONObject object = new JSONObject(chatMessage.body);
                            object.put("Request", bodyarray[1]);
                            chatMessage.body = object.toString();
                            MyApplication.mydb.updateChat(chatMessage);

                            if (bodyarray[1].equals("1")) {
                                chatMessage.body = "Request Accepted";
                            } else if (bodyarray[1].equals("0")) {
                                chatMessage.body = "Request Declined";
                            }

                            chatMessage.messagetype = 2;
                            chatMessage.dateid = "";
                            chatMessage.msgid = (int) MyApplication.mydb.insertChat(chatMessage);
                            if (chatListner != null) {
                                chatListner.onChat(chatMessage, false);
                            } else {
                                //not open
                                localChatListner.onLocalChat(chatMessage);
                            }

                        } catch (JSONException e) {
                            Log.e(Tag + "jsonException is", e.getMessage());
                        }
                        Log.e(Tag + "id is : ", MyApplication.mydb.updateChat(chatMessage) + "");
                    } else {

                        try {
                            JSONObject object = new JSONObject(chatMessage.body);
                            object.put("Request", "");
                            chatMessage.body = object.toString();
                            chatMessage.msgid = (int) MyApplication.mydb.insertChat(chatMessage);
                            if (chatListner != null) {
                                chatListner.onChat(chatMessage, true);
                            } else {
                                //not open
                                localChatListner.onLocalChat(chatMessage);
                            }

                        } catch (JSONException e) {
                            Log.e(Tag + "jsonException is", e.getMessage());
                        }


                    }
                } else {
                    String[] array = message.getFrom().toString().split("/");
                    String from;
                    if (array.length > 0) {
                        from = array[0];
                    } else {
                        from = message.getFrom().toString();
                    }
                    chatMessage = new ChatMessage(from, message.getTo().asBareJid().toString(), message.getBody(), 2, mdate, mtime, null, Prefs.getString(RequestParamUtils.USER_ID, ""), false);
                    chatMessage.msgid = (int) MyApplication.mydb.insertChat(chatMessage);
                    if (chatListner != null) {
                        chatListner.onChat(chatMessage, true);
                    } else {
                        //not open
                        localChatListner.onLocalChat(chatMessage);
                    }
                }


            } else {
                ChatStateExtension chatStateExtension = (ChatStateExtension) message.getExtension("http://jabber.org/protocol/chatstates");
                Log.e("Current chat status : ", "" + chatStateExtension.getElementName());
                if (chatStateExtension.getElementName().equals(ChatState.composing + "")) {
                    if (chatStateListner != null) {
                        chatStateListner.OnChatStateChange("typing...", message.getFrom().asBareJid().toString());
                    }
                } else {
                    if (chatStateListner != null) {
                        chatStateListner.OnChatStateChange("online", message.getFrom().asBareJid().toString());
                    }
                }

            }
        }


        @Override
        public void stateChanged(Chat chat, ChatState state, Message message) {
            Log.e("Chat State", "Called");
            if (ChatState.composing.equals(state)) {
                Log.e("Chat State", chat.getParticipant() + " is typing..");
            } else if (ChatState.gone.equals(state)) {
                Log.e("Chat State", chat.getParticipant() + " has left the conversation.");
            } else {
                Log.e("Chat State", chat.getParticipant() + ": " + state.name());
            }
        }


    }

    public class XMPPConnectionListener implements ConnectionListener {
        @Override
        public void connected(final XMPPConnection connection) {

            Log.e(Tag + "Connected", "Connected!");
            connected = true;
            if (!connection.isAuthenticated()) {
                login();
            }
        }

        @Override
        public void connectionClosed() {
            if (isToasted)

                new Handler(Looper.getMainLooper()).post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub

//                        Toast.makeText(context, "ConnectionCLosed!",
//                                Toast.LENGTH_SHORT).show();

                    }
                });
            Log.e(Tag + "xmpp", "ConnectionCLosed!");
            connected = false;
            chat_created = false;
            loggedin = false;
            try {
                Presence presence = new Presence(Presence.Type.unavailable);
                if (connection.isConnected()) {
                    connection.sendStanza(presence);

                    Prefs.putString(RequestParamUtils.XMPPUSERNAME, "");
                    Prefs.putString(RequestParamUtils.XMPPUSERPASSWORD, "");
                }

            } catch (Exception e) {
                Log.e("Exception is ", e.getMessage());
            }
            if (connection.isConnected()) {
                connection.disconnect();
            }

        }

        @Override
        public void connectionClosedOnError(Exception arg0) {
            Log.e(Tag + "connectionClosedOnError", arg0.getMessage());
            connected = false;
            chat_created = false;
            loggedin = false;
            ReconnectionManager reconnectionManager = ReconnectionManager.getInstanceFor(connection);
            reconnectionManager.setEnabledPerDefault(true);
            reconnectionManager.enableAutomaticReconnection();
            try {
                Prefs.putString(RequestParamUtils.XMPPUSERNAME, "");
                Prefs.putString(RequestParamUtils.XMPPUSERPASSWORD, "");
                connection.connect();
            } catch (SmackException e) {
                Log.e(Tag + "SmackException", e.getMessage());
            } catch (IOException e) {
                Log.e(Tag + "IOException", e.getMessage());
            } catch (XMPPException e) {
                Log.e(Tag + "XMPPException", e.getMessage());
            } catch (InterruptedException e) {
                Log.e(Tag + "InterruptedException", e.getMessage());
            }
        }

        @Override
        public void reconnectingIn(int arg0) {

            Log.e(Tag + "xmpp", "Reconnectingin " + arg0);

            loggedin = false;
        }

        @Override
        public void reconnectionFailed(Exception arg0) {
            Log.e(Tag + "reconnectionFailed", arg0.getMessage());
            connected = false;

            chat_created = false;
            loggedin = false;
        }

        @Override
        public void reconnectionSuccessful() {
            Log.e(Tag + "Connected", "Successfully");
            connected = true;

            chat_created = false;
            loggedin = false;
        }


        @Override
        public void authenticated(XMPPConnection arg0, boolean arg1) {
            Log.e(Tag + "xmpp", "Authenticated!");
            loggedin = true;

            ChatManager.getInstanceFor(connection).addChatListener(
                    mChatManagerListener);

            chat_created = false;
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }).start();
        }
    }

}

