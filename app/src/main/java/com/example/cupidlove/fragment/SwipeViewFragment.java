package com.example.cupidlove.fragment;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.adapter.SwipeDeckAdapter;
import com.example.cupidlove.customview.swipedeck.SwipeDeck;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.model.UserFilter;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.Utils;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kaushal on 29-12-2017.
 */

public class SwipeViewFragment extends Fragment implements ResponseListener {
    //TODO : VAriable Declaration
    View view;
    LayoutInflater inflater;
    ViewGroup container;

    //TODO: Bind all XML view with java file
    @BindView(R.id.swipe_deck)
    SwipeDeck swipe_deck;

    @BindView(R.id.tvNoMatchFound)
    TextViewRegular tvNoMatchFound;

    @BindView(R.id.llLike)
    ImageView llLike;

    @BindView(R.id.llDislike)
    ImageView llDislike;

    //TODO: Variable declaration
    SwipeDeckAdapter swipeDeckAdapter;
    boolean userfilter = false;
    boolean notification = false;

    List<UserFilter.Body> list = new ArrayList<>();
    List<UserFilter.Body> templist = new ArrayList<>();
    int start = 1;

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private boolean isDialogShow = true;
    boolean loading;

    int swiped_card = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_swipe_view, container, false);
        ButterKnife.bind(this, view);
        this.inflater = inflater;
        ((BaseActivity) getActivity()).settvTitle(getResources().getString(R.string.start_playing));
        ((HomeActivity) getActivity()).adView.setVisibility(View.GONE);

        ((BaseActivity) getActivity()).hideSearch();
        llLike.setVisibility(View.INVISIBLE);
        llDislike.setVisibility(View.INVISIBLE);

        setSwipeDeckAdapter();
//        userUpdateLatLong();
        getUserFilter(1);
        this.container = container;

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.checkLocationService(getActivity());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i("3err", "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If img_user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i("", "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i("", "Permission granted, updates requested, starting location updates");
                Utils.startStep3(getActivity());

            } else {
                // Permission denied.
                Toast.makeText(getActivity(), "Allow permission to access your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void hideLikeDisLikeView() {
        if (templist.size()== 0) {
            llLike.setVisibility(View.INVISIBLE);
            llDislike.setVisibility(View.INVISIBLE);
            tvNoMatchFound.setVisibility(View.VISIBLE);
        } else {
            if (swipeDeckAdapter.getCount() == 0) {
                llLike.setVisibility(View.VISIBLE);
                llDislike.setVisibility(View.VISIBLE);
                tvNoMatchFound.setVisibility(View.INVISIBLE);
            }
        }

    }

    //TODO: Like Button Click
    @OnClick(R.id.llLike)
    public void llLikeClick() {

        String id = ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.FIRST_VISIT, "");

        if (id == "") {
            return;
        }
//        sendNotification(1); // 1 for like
        swipe_deck.swipeTopCardRight(500);
        hideLikeDisLikeView();
    }


    //TODO: dislike button click
    @OnClick(R.id.llDislike)
    public void llDislikeClick() {

        String id = ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.FIRST_VISIT, "");

        if (id == "") {
            return;
        }
//        sendNotification(0); // 0 for dislike
        swipe_deck.swipeTopCardLeft(500);
        hideLikeDisLikeView();
    }

    //TODO: Set Swipe Adapter
    public void setSwipeDeckAdapter() {

        swipeDeckAdapter = new SwipeDeckAdapter(getActivity());
        swipe_deck.setAdapter(swipeDeckAdapter);
        swipe_deck.setLeftImage(R.id.left_image);
        swipe_deck.setRightImage(R.id.right_image);

        swipe_deck.setCallback(new SwipeDeck.SwipeDeckCallback() {
            @Override
            public void cardSwipedLeft(long itemId) {
                Log.e("MainActivity", "card was swiped left, position in adapter: " + itemId);
                //left swipe for dislike

                sendNotification(0); // 0 for dislike

                swiped_card++;
                if (swiped_card == Constant.ad_After_Card) {
                    swiped_card = 0;
                    if (((HomeActivity) getActivity()).mRewardedVideoAd.isLoaded()) {
                        ((HomeActivity) getActivity()).mRewardedVideoAd.show();
                    }
                }
                hideLikeDisLikeView();
            }

            @Override
            public void cardSwipedRight(long itemId) {

                Log.e("MainActivity", "card was swiped right, position in adapter: " + itemId);
                //right swipe for like
                sendNotification(1); // 1 for like
                swiped_card++;
                if (swiped_card == Constant.ad_After_Card) {
                    swiped_card = 0;
                    if (((HomeActivity) getActivity()).mRewardedVideoAd.isLoaded()) {
                        ((HomeActivity) getActivity()).mRewardedVideoAd.show();
                    }
                }
                hideLikeDisLikeView();
            }

            @Override
            public boolean isDragEnabled(long itemId) {
                return true;
            }
        });
    }

    //TODO: Get User filter
    public void getUserFilter(int index) {
        String lat, lng;

        if (((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0") == null ||
                ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0").equals("null") ||
                ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0").equals("")) {
            lat = Constant.LAT + "";
        } else {
            lat = ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0");
        }
        if (((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0") == null ||
                ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0").equals("null") ||
                ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0").equals("")) {
            lng = Constant.LNG + "";
        } else {
            lng = ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0");
        }
        try {
            ((BaseActivity) getActivity()).showProgress("");
            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("location_lat", lat);
            params.put("location_long", lng);
            params.put("start", index + "");

            Debug.e("getUserFilter", params.toString());
            loading = true;
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().USER_FILTER, params, new ResponseHandler(getActivity(), this, "user_filter"));
        } catch (Exception e) {
            Debug.e("getUserFilter Exception", e.getMessage());
        }
    }

    //TODO: update latitude and longitude api call
    public void userUpdateLatLong() {

        try {
            RequestParams params = new RequestParams();

            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("location_lat", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LATITUDE, "0"));
            params.put("location_long", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LONGITUDE, "0"));

            Debug.e("userUpdateLatLong", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().USER_UPDATE_LAT_LONG, params, new ResponseHandler(getActivity(), this, "userUpdateLatLong"));
        } catch (Exception e) {
            Debug.e("userUpdateLatLong Exception", e.getMessage());
        }
    }

    //TODO: send Notification Api call
    public void sendNotification(int option) {

        try {
//            ((BaseActivity) getActivity()).showProgress("");
            RequestParams params = new RequestParams();

            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("receive_user_id", templist.get(0).id);
            params.put("status", option + "");

            templist.remove(0);
            notification = true;
            Debug.e("sendNotification", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().SEND_NOTIFICATION, params, new ResponseHandler(getActivity(), this, "sendnotification"));

        } catch (Exception e) {
            Debug.e("sendNotification Exception", e.getMessage());
        }

    }

    //TODO: Response of all api call
    @Override
    public void onResponse(String response, String methodName) {

        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("user_filter")) {
            Log.e(methodName + "Response is ", response);

            //userfilter
            loading = false;
            final UserFilter userFilterRider = new Gson().fromJson(
                    response, new TypeToken<UserFilter>() {
                    }.getType());

            if (!userFilterRider.error) {
                llLike.setVisibility(View.VISIBLE);
                llDislike.setVisibility(View.VISIBLE);


                List<String> id = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    id.add(list.get(i).id);
                }
                for (int i = 0; i < userFilterRider.body.size(); i++) {
                    list.add(userFilterRider.body.get(i));
                    templist.add(userFilterRider.body.get(i));
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeDeckAdapter.AddAll(list);
                        ((BaseActivity) getActivity()).dismissProgress();
                    }
                });


            } else {
//                llLike.setVisibility(View.INVISIBLE);
//                llDislike.setVisibility(View.INVISIBLE);
                tvNoMatchFound.setVisibility(View.VISIBLE);
                ((BaseActivity) getActivity()).dismissProgress();
            }

            hideLikeDisLikeView();

            isDialogShow = false;

        } else if (methodName.equals("userUpdateLatLong")) {
            Log.e(methodName + "Response is ", response);

            //update lat long


            getUserFilter(1); //get user filter data

            try {
                JSONObject jsonObject = new JSONObject(response);

                if (jsonObject.getBoolean("error")) {
                    //error
                } else {
                    //lat long updated
                }

            } catch (JSONException e) {
                Log.e("error", e.getMessage());
            }

        } else if (methodName.equals("sendnotification")) {
            Log.e(methodName + "Response is ", response);

            //send notification for like and dislike
            if (!loading) {
                if (templist.size() < 2) {
                    getUserFilter(2);

                } else {
                    ((BaseActivity) getActivity()).dismissProgress();
                }
            }

        }
    }


}
