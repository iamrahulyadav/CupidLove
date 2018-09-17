package com.example.cupidlove.fragment;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.adapter.ChatListAdapter;
import com.example.cupidlove.customview.edittext.EditTextMedium;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.ChatList;
import com.example.cupidlove.model.Friends;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.MyApplication;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.example.xmpp.MyXMPP;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.cupidlove.fragment.DoChatFragment.Tag;

/**
 * Created by Kaushal on 29-12-2017.
 */

public class ChatListFragment extends Fragment implements OnItemClickListner, ResponseListener {

    // TODO : Variable Declartion
    View view;
    LayoutInflater inflater;
    ViewGroup container;
    ChatListAdapter chatListAdapter;
    android.support.v4.app.FragmentManager fm;

    private List<ChatList.Body> list = new ArrayList<>();

    ChatList chatListRider;
    private Presence presence;
    private Roster roster;


    //TODO: Bind The All XMl View With JAVA File

    @BindView(R.id.rvChatList)
    RecyclerView rvChatList;

    @BindView(R.id.llNoMessage)
    LinearLayout llNoMessage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).settvTitle(getResources().getString(R.string.chat));
        ((HomeActivity) getActivity()).showOrHideAdview();

        getChatList();
//        RosterChange();
        this.inflater = inflater;
        this.container = container;

        setChatListAdapter();
        setSearchTextChangeListener();
        Constant.CHATLISTFRAGMENT = this;
        Constant.chatListFragment = this;
        return view;
    }

    public void setSearchTextChangeListener() {
        ((BaseActivity) getActivity()).tvCancelSearch = (TextViewRegular) getActivity().findViewById(R.id.tvCancelSearch);
        ((BaseActivity) getActivity()).etSearch = (EditTextMedium) getActivity().findViewById(R.id.etSearch);
        ((BaseActivity) getActivity()).tvCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).llSearch.setVisibility(View.GONE);
                ((BaseActivity) getActivity()).etSearch.setText("");
                chatListAdapter.addAll(list);
                getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            }
        });
        ((BaseActivity) getActivity()).etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    ((BaseActivity) getActivity()).etSearch.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(((BaseActivity) getActivity()).etSearch, InputMethodManager.SHOW_IMPLICIT);
                    return true;
                }
                return false;
            }
        });

        ((BaseActivity) getActivity()).etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override


            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {

                    if (((BaseActivity) getActivity()) != null)
                        if (((BaseActivity) getActivity()).etSearch.getText().toString().equals("")) {
                            chatListAdapter.addAll(list);
                        } else {

                            List<ChatList.Body> newList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).fname.toLowerCase().contains(((BaseActivity) getActivity()).etSearch.getText().toString().toLowerCase()) ||
                                        list.get(i).lname.toLowerCase().contains(((BaseActivity) getActivity()).etSearch.getText().toString().toLowerCase())) {
                                    newList.add(list.get(i));
                                }
                            }
                            chatListAdapter.addAll(newList);
                        }
                }
            }
        });
    }

    //TODO: add the chat adapter
    public void setChatListAdapter() {

        chatListAdapter = new ChatListAdapter(getActivity(), this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rvChatList.setLayoutManager(mLayoutManager);
        rvChatList.setAdapter(chatListAdapter);
        rvChatList.setNestedScrollingEnabled(false);
    }


    @Override
    public void onItemClick(int position, String value, int outerpos) {

        if (MyXMPP.connection != null) {
            roster = Roster.getInstanceFor(MyXMPP.connection);
            String s = chatListAdapter.getList().get(position).ejUser + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX;
            Prefs.putString("chatlistname", s);
            if (!isRosterCreated(chatListAdapter.getList().get(position).fname)) {

                createRosterEntry(s, chatListAdapter.getList().get(position).fname);
            }

            //Put the value
            DoChatFragment doChatFragment = new DoChatFragment();
            Bundle args = new Bundle();

            args.putString(RequestParamUtils.ID, list.get(position).fid);

            args.putString(RequestParamUtils.XMPPUSERNAME, chatListAdapter.getList().get(position).ejUser + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX);
            doChatFragment.setArguments(args);

            Constant.FRIEND_USER_ID = chatListRider.body.get(position).fid;
            Constant.FRIEND_LAST_NAME = chatListRider.body.get(position).lname;
            Constant.FRIEND_FIRST_NAME = chatListRider.body.get(position).fname;
            Constant.FRIEND_PROFILE_PICTURE = chatListRider.body.get(position).profileImage;

            ((HomeActivity) getActivity()).loadFragment(doChatFragment, getResources().getString(R.string.do_chat));

        } else {
            ((BaseActivity) getActivity()).doBindService();
        }
    }

    //TODO: create roster entry
    private void createRosterEntry(final String rostername, final String nickName) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                if (!roster.isLoaded()) {
                    try {

                        roster.reloadAndWait();
                    } catch (SmackException.NotLoggedInException e) {
                        Log.e(Tag + "SmackException", e.getMessage());
                    } catch (SmackException.NotConnectedException e) {
                        Log.e(Tag + "NotConnectedException", e.getMessage());
                    } catch (InterruptedException e) {
                        Log.e(Tag + "InterruptedException", e.getMessage());
                    }
                }
                try {
                    roster.createEntry(JidCreate.bareFrom(rostername), nickName, null);
                    Log.e(Tag + "appRoster", "roster created");
                } catch (SmackException.NotLoggedInException e) {
                    Log.e(Tag + "NotLoggedInException", e.getMessage());
                } catch (SmackException.NoResponseException e) {
                    Log.e(Tag + "NoResponseException", e.getMessage());
                } catch (XMPPException.XMPPErrorException e) {
                    Log.e(Tag + "XMPPErrorException", e.getMessage());
                } catch (SmackException.NotConnectedException e) {
                    Log.e(Tag + "NotConnectedException", e.getMessage());
                } catch (InterruptedException e) {
                    Log.e(Tag + "InterruptedException", e.getMessage());
                } catch (XmppStringprepException e) {
                    Log.e(Tag + "XmppStringprepException", e.getMessage());
                }

                return null;
            }
        }.execute();


    }


    //TODO: Check the roster is create or not
    public boolean isRosterCreated(String name) {

        Collection<RosterEntry> entries = roster.getEntries();

        for (RosterEntry entry : entries) {
            Log.e("Roster Entry is ", entry.getName() + " Name");
            if (entry.getName() != null) {
                if (entry.getName().equals(name)) {
                    return true;
                }
            }


        }

        return false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Constant.CHATLISTFRAGMENT = null;
        Constant.chatListFragment = null;
    }

    @Override
    public void onStop() {
        super.onStop();
        Constant.CHATLISTFRAGMENT = null;
        Constant.chatListFragment = null;
    }

    @Override
    public void onStart() {
        super.onStart();
        Constant.CHATLISTFRAGMENT = this;
        Constant.chatListFragment = this;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("On Resume", "called");
        Constant.CHATLISTFRAGMENT = this;
        Constant.chatListFragment = this;
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    chatListAdapter.notifyDataSetChanged();
                }
            });
        }

    }


    //TODO : GET Chat List
    public void getChatList() {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("getChatList", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().CHAT_LIST, params, new ResponseHandler(getActivity(), this, "chat"));
        } catch (Exception e) {
            Debug.e("getChatList Exception", e.getMessage());
        }
    }

    //TODO: Respose of all api call
    @Override
    public void onResponse(String response, String methodName) {


        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("chat")) {

            Log.e("response", response);
            //login
            chatListRider = new Gson().fromJson(
                    response, new TypeToken<ChatList>() {
                    }.getType());

            if (chatListRider.body.size() != 0) {
                //got data of friends
                list.addAll(chatListRider.body);
                if (list.size() == 0) {
                    //No data is there
                    llNoMessage.setVisibility(View.VISIBLE);
                    rvChatList.setVisibility(View.GONE);
                } else {

                    llNoMessage.setVisibility(View.GONE);
                    rvChatList.setVisibility(View.VISIBLE);
                    List<Boolean> onlineList = new ArrayList<>();
                    MyApplication.mydb.deleteFriend();
                    for (int i = 0; i < list.size(); i++) {
//                        onlineList.add(isRosterOnline(list.get(i).ejUser + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX));
                        Friends friends = new Friends(list.get(i).fname, list.get(i).lname,
                                list.get(i).ejUser + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX, list.get(i).profileImage,
                                list.get(i).fid);
                        MyApplication.mydb.insertFriend(friends);
                    }

                    chatListAdapter.addAll(list);
                }


            } else {
                //Something is wrong
                llNoMessage.setVisibility(View.VISIBLE);
                rvChatList.setVisibility(View.GONE);
//                Toast.makeText(getActivity(), chatListRider.message, Toast.LENGTH_SHORT).show();
            }
            ((BaseActivity) getActivity()).dismissProgress();
        }
    }

    public void rosterChage() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatListAdapter.notifyDataSetChanged();
            }
        });

    }


    //TODO : check user is online or not
    public boolean isRosterOnline(String name) {

        try {
            if (MyXMPP.connection == null) {
                return false;
            }
            Roster roster = Roster.getInstanceFor(MyXMPP.connection);
            Presence userFromServer = roster.getPresence(JidCreate.bareFrom(name.toLowerCase()));
            return userFromServer.isAvailable();
        } catch (XmppStringprepException e) {
            Log.e("Xmpp :- ", e.getMessage());
            return false;

        }
    }


}
