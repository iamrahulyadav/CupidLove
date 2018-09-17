package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 27-12-2017.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.FriendProfileActivity;
import com.example.cupidlove.activity.ItsMatchActivity;
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
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 17-11-2017.
 */

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.RecentViewHolder> implements ResponseListener {

    //TODO : VAriable Declaration
    private List<Notification.Body> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    int selectedLike;
    ProgressBar loader;

    public NotificationAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<Notification.Body> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }


    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);

        return new RecentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder holder, final int position) {
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FriendProfileActivity.class);
                intent.putExtra(RequestParamUtils.ID, list.get(position).sendUserId);
                activity.startActivity(intent);
            }
        });
        holder.flLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Like
                likeClicked(position);
            }
        });
        holder.flDislike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Dislike
                onItemClickListner.onItemClick(position, "CupidLove", list.size());
            }
        });
        Picasso.with(activity).load(new URLS().UPLOAD_URL + list.get(position).profileImage).error(R.drawable.no_image_profile).into(holder.civProfile, new Callback() {
            @Override
            public void onSuccess() {
                holder.loader.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.loader.setVisibility(View.GONE);
            }
        });
        holder.tvName.setText(list.get(position).fname + " " + list.get(position).lname);

        float number = list.get(position).distance;
        int i = Math.round(number);

        String str;

        if (i == 0 || i == 1) {
            str = "Just a mile away";
        } else {
            str = i + " mile away";
        }
        holder.tvDistance.setText(str);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        //TODO : Bind All XML View With JAVA File
        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.flLike)
        FrameLayout flLike;

        @BindView(R.id.flDislike)
        FrameLayout flDislike;

        @BindView(R.id.civProfile)
        CircleImageView civProfile;

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        @BindView(R.id.tvDistance)
        TextViewRegular tvDistance;

        @BindView(R.id.loader)
        ProgressBar loader;

        public RecentViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void getWidthAndHeight() {
        int height_value = activity.getResources().getInteger(R.integer.height);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        width = displayMetrics.widthPixels / 2 - height_value * 2;
        height = width / 2 + height_value;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    public void likeClicked(int position) {

        try {
            ((BaseActivity) activity).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("approved", "1");
            params.put("send_user_id", list.get(position).sendUserId + "");

            selectedLike = position;

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().APPROVE_NOTIFICATION, params, new ResponseHandler(activity, this, "like"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }


    }

    @Override
    public void onResponse(String response, String methodName) {
        ((BaseActivity) activity).dismissProgress();

        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("like")) {

            Log.e("response", response);
            //login

            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getBoolean("error")) {
                    //error
                    Toast.makeText(activity, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(activity, ItsMatchActivity.class);
                    intent.putExtra(RequestParamUtils.FRIEND_USER_ID, list.get(selectedLike).sendUserId);
                    intent.putExtra(RequestParamUtils.FRIEND_FIRST_NAME, list.get(selectedLike).fname);
                    intent.putExtra(RequestParamUtils.FRIEND_LAST_NAME, list.get(selectedLike).lname);
                    intent.putExtra(RequestParamUtils.FRIEND_PROFILE_PICTURE, list.get(selectedLike).profileImage);
                    intent.putExtra(RequestParamUtils.FRIEND_EJABBERED_ID, list.get(selectedLike).ejUser);

                    onItemClickListner.onItemClick(selectedLike, "CupidLove", list.size());

                    activity.startActivity(intent);
                }

            } catch (Exception e) {
                Log.e("error", response);
            }
        }
    }


}
