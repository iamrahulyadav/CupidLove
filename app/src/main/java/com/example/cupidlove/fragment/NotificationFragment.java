package com.example.cupidlove.fragment;

import android.content.Context;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.adapter.NotificationAdapter;
import com.example.cupidlove.customview.edittext.EditTextMedium;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.Notification;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Kaushal on 29-12-2017.
 */

public class NotificationFragment extends Fragment implements OnItemClickListner, ResponseListener {

    //TODO : Variable Declaration
    View view;
    LayoutInflater inflater;
    ViewGroup container;
    NotificationAdapter notificationAdapter;
    private List<Notification.Body> list = new ArrayList<>();

    //TODO : Bind The All XML View With JAVA File
    @BindView(R.id.rvNotification)
    RecyclerView rvNotification;

    @BindView(R.id.llNoNotification)
    LinearLayout llNoNotification;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notification, container, false);

        ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).settvTitle(getResources().getString(R.string.notification));
        ((HomeActivity) getActivity()).showOrHideAdview();

        getNotification();

        this.inflater = inflater;
        this.container = container;
        setNotificationAdapter();
        setSearchTextChangeListener();

        return view;
    }

    //TODO: set search text change listener
    public void setSearchTextChangeListener() {
        ((BaseActivity) getActivity()).tvCancelSearch = (TextViewRegular) getActivity().findViewById(R.id.tvCancelSearch);
        ((BaseActivity) getActivity()).etSearch = (EditTextMedium) getActivity().findViewById(R.id.etSearch);
        ((BaseActivity) getActivity()).tvCancelSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) getActivity()).llSearch.setVisibility(View.GONE);
                notificationAdapter.addAll(list);
                ((BaseActivity) getActivity()).etSearch.setText("");
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
                            notificationAdapter.addAll(list);
                        } else {

                            List<Notification.Body> newList = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).fname.toLowerCase().contains(((BaseActivity) getActivity()).etSearch.getText().toString().toLowerCase()) ||
                                        list.get(i).lname.toLowerCase().contains(((BaseActivity) getActivity()).etSearch.getText().toString().toLowerCase())) {
                                    newList.add(list.get(i));
                                }
                            }
                            notificationAdapter.addAll(newList);
                        }
                }
            }
        });
    }

    //TODO : set Notification Adapter
    public void setNotificationAdapter() {

        notificationAdapter = new NotificationAdapter(getActivity(), this);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvNotification.setLayoutManager(mLayoutManager);
        rvNotification.setAdapter(notificationAdapter);
        rvNotification.setNestedScrollingEnabled(false);
    }

    //TODO: Item click
    @Override
    public void onItemClick(int position, String value, int outerpos) {
        list.remove(position);
        if (list.size() == 0) {
            rvNotification.setVisibility(View.GONE);
            llNoNotification.setVisibility(View.VISIBLE);
        }
        notificationAdapter.addAll(list);
    }

    //TODo : GET Notification method
    public void getNotification() {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("getNotification", params.toString());
//            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            AsyncHttpRequest.newRequest().post(new URLS().GET_NOTIFICATION, params, new ResponseHandler(getActivity(), this, "notification"));
        } catch (Exception e) {
            Debug.e("getNotification Exception", e.getMessage());
        }

    }

    //TODO: response of all api call
    @Override
    public void onResponse(String response, String methodName) {

        if (response == null || response.equals("")) {
            ((BaseActivity) getActivity()).dismissProgress();
            return;
        } else if (methodName.equals("notification")) {

            Log.e("response", response);
            //login
            final Notification notificationRider = new Gson().fromJson(
                    response, new TypeToken<Notification>() {
                    }.getType());
            if (notificationRider != null) {
                if (!notificationRider.error) {
                    //got data of friends
                    if (notificationRider.body != null) {
                        list.addAll(notificationRider.body);
                        if (list.size() == 0) {
                            //No data is there
                            llNoNotification.setVisibility(View.VISIBLE);
                            rvNotification.setVisibility(View.GONE);
                        } else {
                            llNoNotification.setVisibility(View.GONE);
                            rvNotification.setVisibility(View.VISIBLE);
                            notificationAdapter.addAll(list);
                        }
                    } else {
                        llNoNotification.setVisibility(View.VISIBLE);
                        rvNotification.setVisibility(View.GONE);
                    }
                } else {
                    //Something is wrong
                    Toast.makeText(getActivity(), notificationRider.message, Toast.LENGTH_SHORT).show();
                }
            } else {
                //Something is wrong
                llNoNotification.setVisibility(View.VISIBLE);
                rvNotification.setVisibility(View.GONE);
                Toast.makeText(getActivity(), notificationRider.message, Toast.LENGTH_SHORT).show();
            }
            ((BaseActivity) getActivity()).dismissProgress();
        }
    }

}
