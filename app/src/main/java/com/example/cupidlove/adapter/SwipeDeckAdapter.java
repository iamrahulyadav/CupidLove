package com.example.cupidlove.adapter;

/**
 * Created by Ashvini.Potenza on 4/7/2018.
 */


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.FriendProfileActivity;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.customview.MyDisabledRecyclerView;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.MutualFriend;
import com.example.cupidlove.model.UserFilter;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kaushal on 30-12-2017.
 */

public class SwipeDeckAdapter extends BaseAdapter implements OnItemClickListner, ResponseListener {

    //TODO : Variable Declartion
    private List<UserFilter.Body> data = new ArrayList<>();
    private Activity activity;

    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int currentPage = PAGE_START;
    private boolean isAppInstall = false;
    LinearLayoutManager manager;
    private static int firstVisibleInListview;
    LinearLayout countDotId;
    int item;
    int isEnd = 0;
    int currentPosition = 0;
    private int swipePotion;

    MutualFriendAdapter mutualFriendAdapter;
    int finalImageCount;

    public SwipeDeckAdapter(Activity activity) {
        this.data = data;
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return data.size();
    }

    public void AddAll(List<UserFilter.Body> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public List<UserFilter.Body> getList() {
        return data;
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        swipePotion = position;

        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // normally use a viewholder
            v = inflater.inflate(R.layout.layout_card, parent, false);
        }
        FrameLayout flMutualFriends = (FrameLayout) v.findViewById(R.id.flMutualFriends);
//        ImageView img_report_user = (ImageView) v.findViewById(R.id.img_report_user);
        TextViewRegular tvNameAge = (TextViewRegular) v.findViewById(R.id.tvNameAge);
        TextViewRegular tvLocationName = (TextViewRegular) v.findViewById(R.id.tvLocationName);
        TextViewRegular tvCollage = (TextViewRegular) v.findViewById(R.id.tvCollage);
        TextViewRegular tvProfession = (TextViewRegular) v.findViewById(R.id.tvProfession);
        TextViewRegular tvMutualFriendCount = (TextViewRegular) v.findViewById(R.id.tvMutualFriendCount);
        FrameLayout flReportUser = (FrameLayout) v.findViewById(R.id.flReportUser);
        countDotId = (LinearLayout) v.findViewById(R.id.countDotId);
        LinearLayout llTopHint = v.findViewById(R.id.llTopHint);
        LinearLayout llNextHint = v.findViewById(R.id.llNextHint);
        final LinearLayout llProfileHint = v.findViewById(R.id.llProfileHint);
        final LinearLayout llImageHint = v.findViewById(R.id.llImageHint);
        final PageIndicatorView pageIndicatorView = v.findViewById(R.id.pageIndicatorView);


        int imageCount = 0;
        final ArrayList<String> list = new ArrayList<>();

        if (data.get(position).gallary.img1 != null && !data.get(position).gallary.img1.equals("")) {
            list.add(data.get(position).gallary.img1);
            imageCount = 1;
        }
        if (data.get(position).gallary.img2 != null && !data.get(position).gallary.img2.equals("")) {
            list.add(data.get(position).gallary.img2);
            imageCount = 2;
        }
        if (data.get(position).gallary.img3 != null && !data.get(position).gallary.img3.equals("")) {
            list.add(data.get(position).gallary.img3);
            imageCount = 3;
        }
        if (data.get(position).gallary.img4 != null && !data.get(position).gallary.img4.equals("")) {
            list.add(data.get(position).gallary.img4);
            imageCount = 4;
        }
        if (data.get(position).gallary.img5 != null && !data.get(position).gallary.img5.equals("")) {
            list.add(data.get(position).gallary.img5);
            imageCount = 5;
        }
        if (data.get(position).gallary.img6 != null && !data.get(position).gallary.img6.equals("")) {
            list.add(data.get(position).gallary.img6);
            imageCount = 6;
        }
        pageIndicatorView.setCount(list.size());

        pageIndicatorView.setSelection(0);
        final MyDisabledRecyclerView mrecy = v.findViewById(R.id.recy_mode);
        manager = new LinearLayoutManager(activity);
        manager.canScrollVertically();
        manager.setSmoothScrollbarEnabled(true);
        mrecy.setLayoutManager(manager);
        mrecy.setNestedScrollingEnabled(false);
        final CustomImageSwipeAdapter customImageSwipeAdapter = new CustomImageSwipeAdapter(activity, list, data.get(position).id);
        mrecy.setAdapter(customImageSwipeAdapter);
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(mrecy);
        item = 0;

        tvNameAge.setText(data.get(position).fname + " " + data.get(position).lname + ", " + data.get(position).age);

        float number = Float.parseFloat(data.get(position).distance);
        int i = Math.round(number);

        String str;

        if (i == 0 || i == 1) {
            str = "Just a mile away";
        } else {
            str = i + " mile away";
        }
        tvLocationName.setText(str);
        tvCollage.setText(data.get(position).education + "");
        tvProfession.setText(data.get(position).profession + "");
        tvMutualFriendCount.setText(data.get(position).mutualFriend + "");

        if (data.get(position).mutualFriend != 0)

        {
            flMutualFriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String id = ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.FIRST_VISIT, "");

                    if (id == "") {
                        return;
                    }
                    mutualFriend(data.get(position).id);
                    mutualFriendDialog();
                }
            });
        }
        pageIndicatorView.setSelection(0);

        finalImageCount = mrecy.getAdapter().getItemCount();


        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Constant.ISTOPCLICK ==1 ) {
                    if (currentPosition != 0) {
                        currentPosition--;
                        mrecy.smoothScrollToPosition(currentPosition);
                        pageIndicatorView.setSelection(currentPosition);
                    }
                } else   if (Constant.ISTOPCLICK ==2 ){
                    if (currentPosition != mrecy.getAdapter().getItemCount() - 1) {
                        currentPosition++;
                        mrecy.smoothScrollToPosition(currentPosition);
                        pageIndicatorView.setSelection(currentPosition);
                    }
                }else if(Constant.ISTOPCLICK == 3) {
                    Intent intent = new Intent(activity, FriendProfileActivity.class);
                    intent.putExtra(RequestParamUtils.ID, data.get(position).id);
                    activity.startActivity(intent);
                }

                Log.e("Onclick ", "called  by Lopa Dholariya" + position);
            }
        });

        //TODO: Report user click
        flReportUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.FIRST_VISIT, "");

                if (id == "") {
                    return;
                }
                //block User api call
                try {

                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
                    alertDialog.setTitle(activity.getResources().getString(R.string.app_name));
                    alertDialog.setMessage("Do You Want To Report This User?");
                    alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            ((HomeActivity) activity).reportUser(data.get(position).id);
                        }
                    });
                    alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDialog.show();


                } catch (Exception e) {
                    Log.e("Exception is ", e.getMessage());
                }

            }
        });

        String hint = ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.FIRST_VISIT, "");
        boolean value = false;

        if (hint == "") {
            value = true;
        }

        if (value == true && position ==0) {
            llImageHint.setVisibility(View.VISIBLE);
            llProfileHint.setVisibility(View.VISIBLE);
            llTopHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (currentPosition != 0) {
                        currentPosition--;
                        mrecy.smoothScrollToPosition(currentPosition);
                        pageIndicatorView.setSelection(currentPosition);
                    }

                    SharedPreferences.Editor pre = ((BaseActivity) activity).getPreferences().edit();
                    pre.putString(RequestParamUtils.FIRST_VISIT, "done");
                    pre.commit();
                    llImageHint.setVisibility(View.GONE);
                    llProfileHint.setVisibility(View.GONE);

                }
            });

            llNextHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentPosition != mrecy.getAdapter().getItemCount() - 1) {
                        currentPosition++;
                        mrecy.smoothScrollToPosition(currentPosition);
                        pageIndicatorView.setSelection(currentPosition);
                    }

                    SharedPreferences.Editor pre = ((BaseActivity) activity).getPreferences().edit();
                    pre.putString(RequestParamUtils.FIRST_VISIT, "done");
                    pre.commit();
                    llImageHint.setVisibility(View.GONE);
                    llProfileHint.setVisibility(View.GONE);
                }
            });

            llProfileHint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, FriendProfileActivity.class);
                    intent.putExtra(RequestParamUtils.ID, data.get(position).id);
                    activity.startActivity(intent);
                    SharedPreferences.Editor pre = ((BaseActivity) activity).getPreferences().edit();
                    pre.putString(RequestParamUtils.FIRST_VISIT, "done");
                    pre.commit();
                    llImageHint.setVisibility(View.GONE);
                    llProfileHint.setVisibility(View.GONE);
                }
            });
        } else {
            llImageHint.setVisibility(View.GONE);
            llProfileHint.setVisibility(View.GONE);
        }
        return v;
    }

    //TODO: mutual friend display dialog
    public void mutualFriendDialog() {
        Dialog mutualdialog = new Dialog(activity);
        mutualdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mutualdialog.setContentView(R.layout.dialog_mutual_friend);
        RecyclerView rvMutualFriend = (RecyclerView) mutualdialog.findViewById(R.id.rvMutualFriend);

        mutualFriendAdapter = new MutualFriendAdapter(activity, this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(activity, 2, GridLayoutManager.HORIZONTAL, false);
        rvMutualFriend.setLayoutManager(mLayoutManager);
        rvMutualFriend.setAdapter(mutualFriendAdapter);
        rvMutualFriend.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvMutualFriend, false);

        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.copyFrom(mutualdialog.getWindow().getAttributes());
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mutualdialog.show();
        mutualdialog.getWindow().setAttributes(params);
    }

    @Override
    public void onItemClick(int position, String value, int outerpos) {

    }

    //TODO: mutual friend api call
    public void mutualFriend(String id) {
        try {
            ((BaseActivity) activity).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("receive_user_id", id);

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().MUTUAL_FRIEND, params, new ResponseHandler(activity, this, "mutualFriend"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    //TODO: response of api call
    @Override
    public void onResponse(String response, String methodName) {
        ((BaseActivity) activity).dismissProgress();

        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("mutualFriend")) {

            Log.e("response", response);
            //login
            MutualFriend mutualFriendRider = new Gson().fromJson(
                    response, new TypeToken<MutualFriend>() {
                    }.getType());
            if (!mutualFriendRider.error) {
                mutualFriendAdapter.addAll(mutualFriendRider.mutualFriendList);
            }
        }
    }


}