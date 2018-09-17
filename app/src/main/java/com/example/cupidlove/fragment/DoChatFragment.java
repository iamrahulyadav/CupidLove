package com.example.cupidlove.fragment;

import android.content.ContentValues;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.adapter.ChatAdapter;
import com.example.cupidlove.adapter.PlacesAdapter;
import com.example.cupidlove.adapter.SelectedPlacesAdapter;
import com.example.cupidlove.customview.edittext.EditTextRegular;
import com.example.cupidlove.customview.seekbar.RangeSeekBar;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.ChatMessage;
import com.example.cupidlove.model.GooglePlaces;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.ChatListner;
import com.example.cupidlove.utils.Config;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.MyApplication;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.example.xmpp.ChatStateListner;
import com.example.xmpp.MyXMPP;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterListener;
import org.jivesoftware.smackx.chatstates.ChatState;
import org.jivesoftware.smackx.iqlast.LastActivityManager;
import org.jivesoftware.smackx.iqlast.packet.LastActivity;
import org.jivesoftware.smackx.receipts.ReceiptReceivedListener;
import org.json.JSONException;
import org.json.JSONObject;
import org.jxmpp.jid.Jid;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kaushal on 29-12-2017.
 */

public class DoChatFragment extends Fragment implements ResponseListener, OnItemClickListner, ReceiptReceivedListener, ChatListner, ChatStateListner {

    //TODO : VAriable Declation
    View view;
    LayoutInflater inflater;
    ViewGroup container;
    public static ChatAdapter chatAdapter;
    PlacesAdapter placesAdapter;
    SelectedPlacesAdapter selectedPlacesAdapter;

    RecyclerView rvPlaces, rvSelectedPlaces;

    AlertDialog alertDialogPlaces, alertDialogSelectedPlaces, alertDialogDate;

    public List<ChatMessage> chatMessagesList = new ArrayList<>();

    LinearLayoutManager mLayoutManager;
    private String toName;

    GooglePlaces googlePlacesRider;
    List<GooglePlaces.Result> googlePlaceData = new ArrayList<>();
    List<GooglePlaces.Result> selectedgooglePlaceData = new ArrayList<>();

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    String typeData, selectedDate, selectedTime;

    TextViewRegular tvSelectDateTime, tvSetDateTime;

    public static String Tag = "Do Chat :-";

    //TODO: Bind The All XML view JAVA File
    @BindView(R.id.rvDoChat)
    RecyclerView rvDoChat;

    @BindView(R.id.llPreferences)
    LinearLayout llPreferences;

    @BindView(R.id.civOne)
    ImageView civOne;

    @BindView(R.id.civTwo)
    ImageView civTwo;

    @BindView(R.id.civThree)
    ImageView civThree;

    @BindView(R.id.civFour)
    ImageView civFour;

    @BindView(R.id.tvOne)
    TextViewRegular tvOne;

    @BindView(R.id.tvTwo)
    TextViewRegular tvTwo;

    @BindView(R.id.tvThree)
    TextViewRegular tvThree;

    @BindView(R.id.tvFour)
    TextViewRegular tvFour;

    @BindView(R.id.etChat)
    EditTextRegular etChat;

    String strPlaceName;
    private boolean typingStarted;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_do_chat, container, false);

        ButterKnife.bind(this, view);
        ((HomeActivity) getActivity()).adView.setVisibility(View.GONE);

        this.inflater = inflater;
        this.container = container;

        //Inflate the fragment
        //Retrieve the value
        if (getArguments() != null) {
            if( getArguments().getString(RequestParamUtils.ID)!=null && ! getArguments().getString(RequestParamUtils.ID).equals("")) {
                Constant.FRIEND_ID = getArguments().getString(RequestParamUtils.ID);
            }

            if( getArguments().getString(RequestParamUtils.XMPPUSERNAME)!=null && ! getArguments().getString(RequestParamUtils.XMPPUSERNAME).equals("")) {
                toName = getArguments().getString(RequestParamUtils.XMPPUSERNAME);
            }else {
                toName = Constant.EJUSERID + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX;
            }


        } else {
            if (toName == null || toName.equals("")) {
                if (((BaseActivity) getContext()).getIntentData() != null) {
                    Constant.FRIEND_ID = ((BaseActivity) getContext()).fid;
                    toName = Constant.EJUSERID + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX;
                }
            }
        }
//        toName = "123_renelle@54.70.184.29";

        ((BaseActivity) getActivity()).showChat();
        ((BaseActivity) getActivity()).llChatClicked(getActivity());

        setDatePreferencesData();

        if (getArguments() != null) {
            showOnline(isRosterOnline());
        }
        setChatListAdapter();

        setChatLister();

        Prefs.putInt(RequestParamUtils.UNREADCHAT, 0);
        Constant.UNREADCHAt = 0;
        setTextChangeListener();
        return view;
    }

    //TODO: set text change listener for chat status
    public void setTextChangeListener() {

        etChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s.toString()) && s.toString().trim().length() == 1) {
                    //Log.i(TAG, “typing started event…”);
                    typingStarted = true;
                    //send typing started status
                    try {
                        ((HomeActivity) getActivity()).getmService().xmpp.sendMessageState(toName, ChatState.composing);
                    } catch (XmppStringprepException e) {
                        Log.e("State Chage Exception", e.getMessage());
                    }
                } else if (s.toString().trim().length() == 0 && typingStarted) {
                    //Log.i(TAG, “typing stopped event…”);
                    typingStarted = false;
                    //send typing stopped status
                    try {
                        ((HomeActivity) getActivity()).getmService().xmpp.sendMessageState(toName, ChatState.paused);
                    } catch (XmppStringprepException e) {
                        Log.e("State Chage Exception", e.getMessage());
                    }

                }

            }
        });
    }

    //TODO: Set chat listener
    public void setChatLister() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (BaseActivity.mXmppService != null) {
                    BaseActivity.mXmppService.xmpp.addChatLister(DoChatFragment.this);
                    BaseActivity.mXmppService.xmpp.addChatStateLister(DoChatFragment.this);
                    getRoster();
                } else {
                    setChatLister();
                }

            }
        }, 100);
    }

    //TODO: Set date Preference
    public void setDatePreferencesData() {
        String pref = ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.DATE_PREF, "");
        if (!pref.equals("")) {
            List<String> items = Arrays.asList(pref.split("\\s*,\\s*"));

            if (items.get(0).equals("1")) {
                //Coffee
                civOne.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_coffee));
                tvOne.setText(getResources().getString(R.string.coffee));
            } else if (items.get(0).equals("2")) {
                //Drink
                civOne.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_drink));
                tvOne.setText(getResources().getString(R.string.drink));
            } else if (items.get(0).equals("3")) {
                //Food
                civOne.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_food));
                tvOne.setText(getResources().getString(R.string.food));
            } else if (items.get(0).equals("4")) {
                //Fun
                civOne.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_fun));
                tvOne.setText(getResources().getString(R.string.fun));
            }
            if (items.get(1).equals("1")) {
                //Coffee
                civTwo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_coffee));
                tvTwo.setText(getResources().getString(R.string.coffee));
            } else if (items.get(1).equals("2")) {
                //Drink
                civTwo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_drink));
                tvTwo.setText(getResources().getString(R.string.drink));
            } else if (items.get(1).equals("3")) {
                //Food
                civTwo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_food));
                tvTwo.setText(getResources().getString(R.string.food));
            } else if (items.get(1).equals("4")) {
                //Fun
                civTwo.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_fun));
                tvTwo.setText(getResources().getString(R.string.fun));
            }
            if (items.get(2).equals("1")) {
                //Coffee
                civThree.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_coffee));
                tvThree.setText(getResources().getString(R.string.coffee));
            } else if (items.get(2).equals("2")) {
                //Drink
                civThree.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_drink));
                tvThree.setText(getResources().getString(R.string.drink));
            } else if (items.get(2).equals("3")) {
                //Food
                civThree.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_food));
                tvThree.setText(getResources().getString(R.string.food));
            } else if (items.get(2).equals("4")) {
                //Fun
                civThree.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_fun));
                tvThree.setText(getResources().getString(R.string.fun));
            }
            if (items.get(3).equals("1")) {
                //Coffee
                civFour.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_coffee));
                tvFour.setText(getResources().getString(R.string.coffee));
            } else if (items.get(3).equals("2")) {
                //Drink
                civFour.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_drink));
                tvFour.setText(getResources().getString(R.string.drink));
            } else if (items.get(3).equals("3")) {
                //Food
                civFour.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_food));
                tvFour.setText(getResources().getString(R.string.food));
            } else if (items.get(3).equals("4")) {
                //Fun
                civFour.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_fun));
                tvFour.setText(getResources().getString(R.string.fun));
            }
        }
    }

    //TODO: show User online or not
    public void showOnline(boolean isOnline) {
        if (isOnline) {
            ((BaseActivity) getActivity()).tvLastSeen.setText("online");
        } else {
            if (getLastSeen() != null) {
                ((BaseActivity) getActivity()).tvLastSeen.setText(getLastSeen());
            } else {
                ((BaseActivity) getActivity()).tvLastSeen.setText(getResources().getString(R.string.offline));

            }

        }
    }

    //TODO: Get Created roster
    public void getRoster() {

        Log.e("Get Roster", "Called");

        if (MyXMPP.connection == null) {
            try {
                MyXMPP.connection.connect();
            } catch (SmackException e) {
                Log.e("SmackException", e.getMessage());
            } catch (IOException e) {
                Log.e("IOException", e.getMessage());
            } catch (XMPPException e) {
                Log.e("XMPPException", e.getMessage());
            } catch (InterruptedException e) {
                Log.e("InterruptedException", e.getMessage());
            }
            getRoster();
        } else {
            Roster roster = Roster.getInstanceFor(MyXMPP.connection);

            roster.addRosterListener(new RosterListener() {
                @Override
                public void entriesAdded(Collection<Jid> addresses) {

                }

                @Override
                public void entriesUpdated(Collection<Jid> addresses) {

                }

                @Override
                public void entriesDeleted(Collection<Jid> addresses) {

                }

                @Override
                public void presenceChanged(final Presence presence) {

                    if (toName.equals(presence.getFrom().asBareJid().toString())) {
                        Log.e(Tag + "Roster", "Presence changed: " + presence.getFrom() + " " + presence);
                        if (((HomeActivity) getActivity()) != null) {
                            ((HomeActivity) getActivity()).runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (presence.isAvailable()) {
                                        if (!((BaseActivity) getActivity()).tvLastSeen.getText().equals("online")) {
                                            showOnline(true);
                                        }

                                    } else {
                                        if (((BaseActivity) getActivity()).tvLastSeen.getText().equals("online")) {
                                            showOnline(false);
                                        }

                                    }

                                }
                            });
                        }
                    }
                }
            });
        }

    }


    //TODO: Get last seen of user
    public String getLastSeen() {

        try {

            if (toName == null) {
                toName = Constant.EJUSERID + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX;
            }

            LastActivityManager lActivityManager = LastActivityManager.getInstanceFor(((HomeActivity) getActivity()).getmService().xmpp.connection);
            LastActivity activity = lActivityManager.getLastActivity(JidCreate.entityBareFrom(toName));
            activity.getIdleTime();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat(" MMM d, yyyy HH:mm aa");
            String dateString = formatter.format(new Date(new Date().getTime() - activity.getIdleTime() * 1000L));
//            Log.e(Tag + "Date String is ", dateString);

            if (dateString != null) {
                return dateString;
            }

        } catch (Exception e) {
            Log.e(Tag + "Exception is  ", e.getMessage());

        }
        return null;
    }

    //TODO: Google place api
    private void myGooglePlaceApi(String strType) {

        googlePlaceData = new ArrayList<>();

        String loc = ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0") + ","
                + ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0");

        if (loc.equals("0,0")) {

        }

        try {
            String type = strType;
            String murl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + loc + "&radius=10000&type=" + strType + "&keyword=" + type + "&key=" +
                    ((BaseActivity) getActivity()).getPreferences().getString(Config.GOOGLE_PLACE_API_KEY, "");
            getPlaces(murl);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    //TODO: Google place api for next page
    private void myGooglePlaceApiNextPage(String strType) {

        String loc = ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0") + ","
                + ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0");

        if (googlePlacesRider.nextPageToken == null || googlePlacesRider.nextPageToken.equals("")) {
            return;
        }
        try {
            String type = strType;
            String murl = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + loc + "&radius=10000&type=" + strType + "&keyword=" + type + "&key=" +
                    ((BaseActivity) getActivity()).getPreferences().getString(Config.GOOGLE_PLACE_API_KEY, "") + "&pagetoken=" + googlePlacesRider.nextPageToken;
            getPlaces(murl);
        } catch (Exception e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

    }

    //TODO: get near place of user
    private void getPlaces(final String mapUrl) {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.get(mapUrl, params, new ResponseHandler(getActivity(), this, "getPlaces"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    //TODO: Send notification of user's message when user offline
    public void sendMessageNotification(String friendId, String message) {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("friendid", friendId);
            params.put("message", message);

            Debug.e(Tag + "sendMessageNotification", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().SEND_MESSAGE, params, new ResponseHandler(getActivity(), this, "chat"));
        } catch (Exception e) {
            Debug.e(Tag + "sendMessageNotification Exception", e.getMessage());
        }
    }

    //TODO: Respone of all Api call
    @Override
    public void onResponse(String response, String methodName) {
        ((BaseActivity) getActivity()).dismissProgress();

        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("getPlaces")) {
            loading = true;

            Log.e(Tag + "response", response);
            //get place
            googlePlacesRider = new Gson().fromJson(
                    response, new TypeToken<GooglePlaces>() {
                    }.getType());

            for (int i = 0; i < googlePlacesRider.results.size(); i++) {
                googlePlaceData.add(googlePlacesRider.results.get(i));
            }
            placesAdapter.addAll(googlePlaceData);
        } else if (methodName.equals("sendMessageNotification")) {
            Log.e(Tag + "response", response);
        }

    }

    //TODO: Set Preferance view for chat
    public void setPreferencesView() {
//        final LinearLayout.LayoutParams paramsImagePhoto = (LinearLayout.LayoutParams) civOne.getLayoutParams();
//        civOne.post(new Runnable() {
//            @Override
//            public void run() {
//                paramsImagePhoto.height = civOne.getWidth();
//                civOne.setLayoutParams(paramsImagePhoto);
//            }
//        });
    }

    //TODO: Set chat visibility
    @OnClick(R.id.etChat)
    public void etChat() {
        llPreferences.setVisibility(View.GONE);
    }

    //TODO: message send click
    @OnClick(R.id.ivSend)
    public void ivSendClick() {
        if (etChat.getText().toString().length() != 0) {
            sendMessage(etChat.getText().toString(), 1);
            llPreferences.setVisibility(View.GONE);
            etChat.setText("");
        }
    }

    //TODO: Preferance click
    @OnClick(R.id.ivPreferences)
    public void ivPreferencesClick() {

//        MyApplication.mydb.deleteChat();
        if (Constant.GOOGLE_PLACE_API_KEY == "") {
            return;
        }
        if (llPreferences.getVisibility() == View.VISIBLE) {
            llPreferences.setVisibility(View.GONE);
        } else {
            llPreferences.setVisibility(View.VISIBLE);
            setPreferencesView();
        }
    }

    //TODO: First Image Click
    @OnClick(R.id.llImageOne)
    public void llImageOneClick() {
        typeData = tvOne.getText().toString();
        setPopupTitle(typeData);
        myGooglePlaceApi(typeData);
        selectPlaceDialog();
    }

    //TODO: Second Image Click
    @OnClick(R.id.llImageTwo)
    public void llImageTwoClick() {
        typeData = tvTwo.getText().toString();
        setPopupTitle(typeData);
        myGooglePlaceApi(typeData);
        selectPlaceDialog();
    }

    //TODO: Third Image click
    @OnClick(R.id.llImageThree)
    public void llImageThreeClick() {
        typeData = tvThree.getText().toString();
        setPopupTitle(typeData);
        myGooglePlaceApi(typeData);
        selectPlaceDialog();
    }

    //TODO: forth image click
    @OnClick(R.id.llImageFour)
    public void llImageFourClick() {
        typeData = tvFour.getText().toString();
        setPopupTitle(typeData);
        myGooglePlaceApi(typeData);
        selectPlaceDialog();
    }

    //TODO: Set popup title for near place
    public void setPopupTitle(String place) {
        if (place.equals(getActivity().getResources().getString(R.string.coffee))) {
            strPlaceName = getActivity().getResources().getString(R.string.cafe_near_by_you);
        } else if (place.equals(getActivity().getResources().getString(R.string.drink))) {
            strPlaceName = getActivity().getResources().getString(R.string.bar_near_by_you);
        } else if (place.equals(getActivity().getResources().getString(R.string.food))) {
            strPlaceName = getActivity().getResources().getString(R.string.restaurant_near_by_you);
        } else if (place.equals(getActivity().getResources().getString(R.string.fun))) {
            strPlaceName = getActivity().getResources().getString(R.string.beach_near_by_you);
        }
    }

    //TODO: send message method for user
    public void sendMessage(String message, int type) {

        String mtime = (String) DateFormat.format("hh:mm aa", Calendar.getInstance().getTime());

        String mdate = (new SimpleDateFormat("MMM d,yyyy")).format(new Date());

        ChatMessage chatMessage = new ChatMessage(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, toName,
                message, type, mdate, mtime, null, ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""), true);
        try {
            if (isRosterOnline()) {
//                sendNotification(chatMessage);
                ((HomeActivity) getActivity()).getmService().xmpp.sendMessage(chatMessage);
            } else {
                ((HomeActivity) getActivity()).getmService().xmpp.sendMessage(chatMessage);
                sendNotification(chatMessage);
            }

        } catch (Exception e) {
            sendNotification(chatMessage);
            Log.e(Tag + "XmppStringprepException", e.getMessage());

        }

        if (chatMessage.messagetype == 3) {
            if (llPreferences.getVisibility() == View.VISIBLE) {
                llPreferences.setVisibility(View.GONE);
            }
        }
    }

    //TODO: send notification api call
    public void sendNotification(ChatMessage chatMessage) {


        String mtime = (String) DateFormat.format("hh:mm aa", Calendar.getInstance().getTime());
        String mdate = (new SimpleDateFormat("MMM d,yyyy")).format(new Date());
        chatMessage.mdate = mdate;
        chatMessage.mtime = mtime;
        final Message message = new Message();
        if (chatMessage.messagetype == 1) {
            message.setBody(chatMessage.body);
        } else if (chatMessage.messagetype == 3) {
            String body = chatMessage.body;
            String id = "";
            try {
                JSONObject jsonObject = new JSONObject(body);
                id = jsonObject.getString("PlaceId");
                chatMessage.dateid = id;
                body = "$***$" + id + "$***$" + chatMessage.body + "#" + jsonObject.getString("Request");
            } catch (JSONException e) {
                Log.e(Tag + "sendNotification", "JSONException" + e.getMessage());
            }
            chatMessage.body = body;
        }
        sendMessageNotification(Constant.FRIEND_ID, chatMessage.body);
//        ((BaseActivity) getActivity()).addChat(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, chatMessage.body, toName, true, this);
    }

    //TODO: set chat adapter
    public void setChatListAdapter() {
        chatAdapter = new ChatAdapter(getActivity(), this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        rvDoChat.setAdapter(chatAdapter);
        rvDoChat.setLayoutManager(mLayoutManager);
        if (MyApplication.mydb.getAllChatMessage(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, toName) != null && MyApplication.mydb.getAllChatMessage(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, toName).size() != 0) {
            chatMessagesList = MyApplication.mydb.getAllChatMessage(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, toName);
            chatAdapter.addAll(chatMessagesList);
            MyApplication.mydb.updateAllToReadChat(chatMessagesList);
            rvDoChat.scrollToPosition(chatAdapter.getItemCount() - 1);
//            mLayoutManager.smoothScrollToPosition(rvDoChat, new RecyclerView.State(), chatAdapter.getItemCount() - 1);
        }

        rvDoChat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    llPreferences.setVisibility(View.GONE);
                }
            }
        });
    }

    //TODO: set Place data for selected image
    @Override
    public void onItemClick(int position, String value, int outerpos) {
        selectedgooglePlaceData = new ArrayList<>();
        selectedgooglePlaceData.add(googlePlaceData.get(position));
        selectedPlaceInfoDialog();
        selectDateDialog();
    }

    //TODO: Selected place dialog
    public void selectPlaceDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_near_by_place, null);
        dialogBuilder.setView(dialogView);

        rvPlaces = (RecyclerView) dialogView.findViewById(R.id.rvPlaces);
        TextViewRegular tvDialogTitle = (TextViewRegular) dialogView.findViewById(R.id.tvDialogTitle);
        ImageView ivCancelDialog = (ImageView) dialogView.findViewById(R.id.ivCancelDialog);
        ImageView ivBackDialog = (ImageView) dialogView.findViewById(R.id.ivBackDialog);

        tvDialogTitle.setText(strPlaceName);
        placesAdapter = new PlacesAdapter(getActivity(), this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPlaces.setLayoutManager(mLayoutManager);
        rvPlaces.setAdapter(placesAdapter);
        rvPlaces.setNestedScrollingEnabled(false);
        rvPlaces.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            loading = false;
                            myGooglePlaceApiNextPage(typeData);
                        }
                    }
                }
            }
        });


        alertDialogPlaces = dialogBuilder.create();
        alertDialogPlaces.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialogPlaces.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogPlaces.show();

        ivCancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogPlaces.dismiss();
            }
        });
        ivBackDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogPlaces.dismiss();
            }
        });
    }

    //TODO: Selected place information dialog
    public void selectedPlaceInfoDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_selected_near_by_place, null);
        dialogBuilder.setView(dialogView);

        rvSelectedPlaces = (RecyclerView) dialogView.findViewById(R.id.rvSelectedPlaces);
        TextViewRegular tvDialogSelectedTitle = (TextViewRegular) dialogView.findViewById(R.id.tvDialogSelectedTitle);
        tvSelectDateTime = (TextViewRegular) dialogView.findViewById(R.id.tvSelectDateTime);
        tvSetDateTime = (TextViewRegular) dialogView.findViewById(R.id.tvSetDateTime);
        tvSetDateTime.setVisibility(View.INVISIBLE);
        ImageView ivCancelSelectedDialog = (ImageView) dialogView.findViewById(R.id.ivCancelSelectedDialog);
        ImageView ivBackSelectedDialog = (ImageView) dialogView.findViewById(R.id.ivBackSelectedDialog);

        tvDialogSelectedTitle.setText(strPlaceName);

        selectedPlacesAdapter = new SelectedPlacesAdapter(getActivity(), this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvSelectedPlaces.setLayoutManager(mLayoutManager);
        rvSelectedPlaces.setAdapter(selectedPlacesAdapter);
        rvSelectedPlaces.setNestedScrollingEnabled(false);
        selectedPlacesAdapter.addAll(selectedgooglePlaceData);

        alertDialogSelectedPlaces = dialogBuilder.create();
        alertDialogSelectedPlaces.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialogSelectedPlaces.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogSelectedPlaces.show();

        tvSelectDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectDateDialog();
            }
        });
        tvSetDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tvSetDateTime.getText().toString().equals(getActivity().getResources().getString(R.string.send_date_request))) {

                    //TODO:SEND DATE REQUEST HERE

                    Log.e(Tag + "tvSelectDateTime ", tvSelectDateTime.getText().toString());
                    Log.e(Tag + "selected place", selectedgooglePlaceData.get(0).name);
                    GooglePlaces.Result place = selectedgooglePlaceData.get(0);

                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("StarRating", place.rating);

                        if (place.photos != null) {
                            if (place.photos.size() > 0) {
                                String strUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + place.photos.get(0).photoReference + "&key=" + ((BaseActivity) getActivity()).getPreferences().getString(Config.GOOGLE_PLACE_API_KEY, "");
                                jsonObject.put("imgPlace", strUrl);
                            }
                        } else {
                            jsonObject.put("imgPlace", "");
                        }


                        jsonObject.put("TimeOfDate", tvSelectDateTime.getText().toString());
                        jsonObject.put("Title", place.name);
                        String distance = String.format("%.2f", distance(place.geometry.location.lat, place.geometry.location.lng)) + " miles";
                        jsonObject.put("Distance", distance);
                        jsonObject.put("PlaceId", place.id);
                        jsonObject.put("Request", "");
                        jsonObject.put("isUpdate", false);
                    } catch (JSONException e) {
                        Log.e(Tag + "JsonException is ", e.getMessage());
                    }

                    sendMessage(jsonObject.toString(), 3);

                    alertDialogSelectedPlaces.dismiss();
                    alertDialogPlaces.dismiss();
                } else {
                    selectDateDialog();
                }
            }
        });
        ivCancelSelectedDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogPlaces.dismiss();
                alertDialogSelectedPlaces.dismiss();
            }
        });
        ivBackSelectedDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogSelectedPlaces.dismiss();
            }
        });
    }

    //TODO: Calculate distance of users
    private double distance(double lat2, double lon2) {
        double lat1;
        double lon1;
        lat1 = Double.parseDouble(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0"));
        lon1 = Double.parseDouble(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0"));

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 0.621371;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    //TODO: select date dialog for date preference
    public void selectDateDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_set_date_time, null);
        dialogBuilder.setView(dialogView);

        TextViewRegular tvDialogDateTitle = (TextViewRegular) dialogView.findViewById(R.id.tvDialogDateTitle);
        ImageView ivCancelDateDialog = (ImageView) dialogView.findViewById(R.id.ivCancelDateDialog);
        ImageView ivBackDateDialog = (ImageView) dialogView.findViewById(R.id.ivBackDateDialog);
        TextViewRegular tvSave = (TextViewRegular) dialogView.findViewById(R.id.tvSave);
        TextViewRegular tvDate = (TextViewRegular) dialogView.findViewById(R.id.tvDate);
        TextViewRegular tvTime = (TextViewRegular) dialogView.findViewById(R.id.tvTime);

        final Switch switchAmPm = (Switch) dialogView.findViewById(R.id.switchAmPm);
        final TextViewRegular etMinute = (TextViewRegular) dialogView.findViewById(R.id.etMinute);
        final TextViewRegular etHour = (TextViewRegular) dialogView.findViewById(R.id.etHour);

        final LinearLayout llTime = (LinearLayout) dialogView.findViewById(R.id.llTime);
        final LinearLayout lltime2 = (LinearLayout) dialogView.findViewById(R.id.lltime2);
        final LinearLayout lldate = (LinearLayout) dialogView.findViewById(R.id.lldate);
        final LinearLayout llCalendar = (LinearLayout) dialogView.findViewById(R.id.llCalendar);
        final CalendarView calendar = (CalendarView) dialogView.findViewById(R.id.calendar);
        final RangeSeekBar seekbarHour = (RangeSeekBar) dialogView.findViewById(R.id.seekbarHour);
        final RangeSeekBar seekbarMinute = (RangeSeekBar) dialogView.findViewById(R.id.seekbarMinute);

        alertDialogDate = dialogBuilder.create();
        alertDialogDate.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
        alertDialogDate.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        alertDialogDate.show();
        llCalendar.setVisibility(View.VISIBLE);
        llTime.setVisibility(View.GONE);

        final Calendar cal = Calendar.getInstance(TimeZone.getDefault());
        int m = cal.get(Calendar.MONTH) + 1;
        selectedDate = cal.get(Calendar.DAY_OF_MONTH) + "/" + m + "/" + cal.get(Calendar.YEAR);

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
//                Toast.makeText(getActivity(),dayOfMonth + "/" + month + "/" + year,4000).show();
                String currentDateTimeString = java.text.DateFormat.getDateTimeInstance().format(new Date());

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                month = month + 1;
                selectedDate = dayOfMonth + "/" + month + "/" + year;

                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date selectDate = dateFormat.parse(selectedDate);
                    Date currentDate = dateFormat.parse(date);

                    if (!selectDate.after(currentDate)) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.selected_date_isnot_prorer), Toast.LENGTH_SHORT).show();
                    }


                } catch (Exception e) {
                    Log.e("Date Parse Exception", e.getMessage());
                }

            }
        });

        if ((int) seekbarHour.getSelectedMaxValue() == 12) {
            seekbarHour.setSelectedMaxValue(0);
        }

        seekbarHour.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {

            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {

                if ((int) maxValue == 11 || (int) maxValue == 12) {
                    seekbarHour.setSelectedMaxValue(12);
                    seekbarHour.defaultColor = getActivity().getResources().getColor(R.color.orange);
//                    seekbarHour.setColorFilter(getActivity().getResources().getColor(R.color.orange));
                } else {
                    seekbarHour.defaultColor = getActivity().getResources().getColor(R.color.grayseekbar);
                }
                if (Constant.isRtl == 1) {
                    etHour.setText(maxValue + "");
                } else {
                    etHour.setText(String.format("%2d", maxValue));
                }

//                strMaxAge= String.valueOf(maxValue);
            }
        });
        seekbarHour.setNotifyWhileDragging(true);

        if ((int) seekbarMinute.getSelectedMaxValue() == 60) {
            seekbarMinute.setSelectedMaxValue(0);
        }

        seekbarMinute.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {

            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {

                if ((int) maxValue == 59 || (int) maxValue == 60) {
                    seekbarMinute.setSelectedMaxValue(59);

                }

                if (Constant.isRtl == 1) {
                    etMinute.setText(maxValue + "");
                } else {
                    etMinute.setText(String.format("%2d", maxValue));
                }
//                strMaxAge= String.valueOf(maxValue);
            }
        });
        seekbarMinute.setNotifyWhileDragging(true);

        SimpleDateFormat formatDate = new SimpleDateFormat("hh:mm a");
        String formattedDate = formatDate.format(new Date()).toString();
        Log.e("Current time ", formattedDate);
        String[] currentTime = formattedDate.split(" ");
        if (currentTime.length > 0) {
            String[] times = currentTime[0].split(":");
            if (times.length == 2) {

                try {
                    seekbarMinute.setSelectedMaxValue(Integer.parseInt(times[1]));
                    seekbarHour.setSelectedMaxValue(Integer.parseInt(times[0]));
                    etMinute.setText(String.format("%2d", Integer.parseInt(times[1])));
                    etHour.setText(String.format("%2d", Integer.parseInt(times[0])));
                } catch (Exception e) {
                    Log.e("Exception is ", e.getMessage());
                }

            }
        }
        if (formattedDate.toLowerCase().contains("am")) {
            switchAmPm.setChecked(false);
        } else {
            switchAmPm.setChecked(true);
        }

        //TODO: Date click
        lldate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lldate.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_dark));
                lltime2.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                llCalendar.setVisibility(View.VISIBLE);
                llTime.setVisibility(View.GONE);
            }
        });

        //TODO: Time click
        lltime2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lldate.setBackgroundColor(getActivity().getResources().getColor(R.color.black));
                lltime2.setBackgroundColor(getActivity().getResources().getColor(R.color.gray_dark));
                llCalendar.setVisibility(View.GONE);
                llTime.setVisibility(View.VISIBLE);
            }
        });

        //TODO: Save detail click
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String hour, minutes, ampm;
                boolean isValid = false;

                if (etHour.getText().toString().equals("")) {
                    if (etMinute.getText().toString().equals("")) {
                        hour = "12";
                        minutes = "0";
                    } else {
                        hour = "12";
                        minutes = etMinute.getText().toString();
                    }
                } else if (etMinute.getText().toString().equals("")) {
                    if (etHour.getText().toString().equals("")) {
                        hour = "12";
                        minutes = "0";
                    } else {
                        hour = etHour.getText().toString();
                        minutes = "0";
                    }
                } else {
                    hour = etHour.getText().toString();
                    minutes = etMinute.getText().toString();
                }

                if (switchAmPm.isChecked()) {
                    ampm = "pm";
                } else {
                    ampm = "am";
                }

                int dd, mm, yyyy;
                String[] dateParts = selectedDate.split("/");
                String day = dateParts[0];
                String month = dateParts[1];
                String year = dateParts[2];

                yyyy = Integer.parseInt(year);
                mm = Integer.parseInt(month);
                dd = Integer.parseInt(day);
                final Calendar cal1 = Calendar.getInstance(TimeZone.getDefault());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US);

                String seleDateTime = selectedDate + " " + hour + ":" + minutes + " " + ampm;
                String date = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US).format(new Date());

                try {
                    Date selectDate = dateFormat.parse(seleDateTime);
                    Date currentDate = dateFormat.parse(date);

                    if (selectDate.after(currentDate)) {
                        isValid = true;
                    } else {
                        isValid = false;
                    }


                } catch (Exception e) {
                    Log.e(Tag + "error", e.getMessage());
                    seleDateTime = selectedDate + " " + hour + ":" + minutes + " " + ampm.charAt(0) + "." + ampm.charAt(1) + ".";

                    try {
                        Date selectDate = dateFormat.parse(seleDateTime);
                        Date currentDate = dateFormat.parse(date);

                        if (selectDate.after(currentDate)) {
                            isValid = true;
                        } else {
                            isValid = false;
                        }


                    } catch (Exception e1) {
                        Log.e(Tag + "error", e1.getMessage());
                    }
                }

                if (isValid) {
                    String date1 = selectedDate + "," + hour + ":" + minutes + " " + ampm.toUpperCase();
                    tvSelectDateTime.setText(date1);
                    tvSetDateTime.setText(getActivity().getResources().getString(R.string.send_date_request));
                    tvSetDateTime.setVisibility(View.VISIBLE);
                    alertDialogDate.dismiss();
                } else {
                    Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.selected_date_time_isnot_prorer), Toast.LENGTH_SHORT).show();
                }
            }

        });

        //TODO: Cancle date dialog
        ivCancelDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialogDate.dismiss();
                alertDialogPlaces.dismiss();
                alertDialogSelectedPlaces.dismiss();
            }
        });

        //TODO: Date dialog back click
        ivBackDateDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogDate.dismiss();
            }
        });
    }

    @Override
    public void onReceiptReceived(Jid fromJid, Jid toJid, String receiptId, Stanza receipt) {
        Log.e(Tag + "On ReceiptReceived ", "Call");
    }

    //TODO: On chat method
    @Override
    public void onChat(ChatMessage chatMessage, boolean isInsert) {

        if (chatMessage.sender.toLowerCase().equals(toName.toLowerCase())) {
            if (isInsert) {
                chatAdapter.addMessage(chatMessage);
            } else {
                chatAdapter.addAll(MyApplication.mydb.getAllChatMessage(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, toName));
            }
            chatMessage.isRead = true;
            MyApplication.mydb.updateChatRead(chatMessage);
            mLayoutManager.smoothScrollToPosition(rvDoChat, new RecyclerView.State(), chatAdapter.getItemCount() - 1);
        } else {
            if (chatMessage.messagetype == 1 || chatMessage.messagetype == 3) {
                if (chatMessage.receiver.toLowerCase().equals(toName.toLowerCase())) {
                    if (isInsert) {
                        chatAdapter.addMessage(chatMessage);
                    } else {
                        chatAdapter.addAll(MyApplication.mydb.getAllChatMessage(Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX, toName));
                    }

                    mLayoutManager.smoothScrollToPosition(rvDoChat, new RecyclerView.State(), chatAdapter.getItemCount() - 1);
                }
                MyApplication.mydb.updateChatRead(chatMessage);
            } else {
                ((HomeActivity) getActivity()).onLocalChat(chatMessage);
            }

        }
    }


    //TODO: Set event in calender
    @Override
    public void setEventInCalender(ChatMessage chatMessage) {
        String date = "";

        try {
            JSONObject jsonObject = new JSONObject(chatMessage.body);
            date = jsonObject.getString("TimeOfDate").replace("\\", "");


            Calendar calendar = Calendar.getInstance();

            if (!date.equals("") || date != null) {
                if (date.contains(",")) {
                    String array[] = date.split(",");
                    String dateString = "/", time = ":";
                    if (array.length > 0)
                        dateString = array[0];

                    if (array.length > 1)
                        time = array[1];


                    String[] dateArray = dateString.split("/");
                    String[] timeArray = time.split(":");

                    if (dateArray.length == 3 && timeArray.length == 2) {
                        calendar.set(Integer.parseInt(dateArray[2].replace(" ", "")), Integer.parseInt(dateArray[1].replace(" ", "")) - 1,
                                Integer.parseInt(dateArray[0].replace(" ", "")), Integer.parseInt(timeArray[0].replace(" ", "")), Integer.parseInt(timeArray[1].substring(0, 2).replace(" ", "")), 0);
                    }

                }
            }


            long startDate = calendar.getTimeInMillis();

            Log.e(Tag + "Start Date", startDate + "");
            String eventUriString = "content://com.android.calendar/events";
            ContentValues eventValues = new ContentValues();
            eventValues.put("calendar_id", 1); // id, We need to choose from
            // our mobile for primary its 1
            eventValues.put("title", "CupidLove:Date with " + Constant.FRIEND_FIRST_NAME + " " + Constant.FRIEND_LAST_NAME);
            eventValues.put("eventLocation", jsonObject.getString("Title"));
            long endDate = startDate + 1000 * 10 * 10; // For next 10min
            Log.e(Tag + "endDate Date", endDate + "");
            eventValues.put("dtstart", startDate);
            eventValues.put("dtend", endDate);
            eventValues.put("eventStatus", 1); // This information is
            eventValues.put("eventTimezone", "UTC/GMT +5:30");
            eventValues.put("hasAlarm", 1); // 0 for false, 1 for true
            eventValues.put(CalendarContract.Events.EVENT_COLOR, R.color.colorPrimary);

            eventValues.put("title", "CupidLove:Date with " + Constant.FRIEND_FIRST_NAME + "" + Constant.FRIEND_LAST_NAME);


            Uri eventUri = getActivity().getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), eventValues);
            long eventID = Long.parseLong(eventUri.getLastPathSegment());
            Log.e(Tag + "event Id is", eventID + "");
        } catch (Exception ex) {
            Log.e(Tag + "Error in  event", ex.getMessage());
        }
    }

    //TODO : check user is online or not
    public boolean isRosterOnline() {

        try {
            if (MyXMPP.connection == null) {
                return false;
            }
            Roster roster = Roster.getInstanceFor(MyXMPP.connection);
            if (toName != null) {
                Presence userFromServer = roster.getPresence(JidCreate.bareFrom(toName.toLowerCase()));
                return userFromServer.isAvailable();
            } else {
                return false;

            }

        } catch (XmppStringprepException e) {
            Log.e("Xmpp :- ", e.getMessage());
            return false;

        }
    }

    //TODO: chat stae change method
    @Override
    public void OnChatStateChange(final String state, final String ejjebredId) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (ejjebredId.toLowerCase().equals(toName.toLowerCase())) {
                        ((BaseActivity) getActivity()).tvLastSeen.setText(state);
                    }
                }
            });

        }
    }
}