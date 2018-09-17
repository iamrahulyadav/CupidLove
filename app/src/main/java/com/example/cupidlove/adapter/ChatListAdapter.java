package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 28-12-2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.ChatList;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.MyApplication;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.xmpp.MyXMPP;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jxmpp.jid.impl.JidCreate;
import org.jxmpp.stringprep.XmppStringprepException;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 17-11-2017.
 */

public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.RecentViewHolder> {

    //TODO : Variable Declaration
    private List<ChatList.Body> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    private List<Boolean> online = new ArrayList<>();

    public ChatListAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<ChatList.Body> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }

    public void addAll(List<ChatList.Body> list, List<Boolean> online) {
        this.list = list;
        this.online = online;
        getWidthAndHeight();
        notifyDataSetChanged();
    }

    public List<Boolean> getOnLineList() {
        return online;
    }

    public List<ChatList.Body> getList() {
        return list;
    }


    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chatlist, parent, false);

        return new RecentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListner.onItemClick(position, "CupidLove", list.size());
            }
        });
        holder.tvName.setText(list.get(position).fname + " " + list.get(position).lname);
        Picasso.with(activity).load(new URLS().UPLOAD_URL + list.get(position).profileImage).placeholder(R.drawable.no_image_profile).error(R.drawable.no_image_profile).into(holder.civProfile, new Callback() {
            @Override
            public void onSuccess() {
                holder.loader.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                holder.loader.setVisibility(View.GONE);
            }
        });

        String receiverId = list.get(position).ejUser + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX;
        String senderid = Prefs.getString(RequestParamUtils.XMPPUSERNAME, "") + Constant.NAME_POSTFIX;

        if (MyApplication.mydb.getLastchatMessage(senderid, receiverId, ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.USER_ID, "")) != null) {
            holder.tvLastMessage.setText(MyApplication.mydb.getLastchatMessage(senderid, receiverId, ((BaseActivity) activity).getPreferences().getString(RequestParamUtils.USER_ID, "")));
        }

        String count = MyApplication.mydb.getUnReadMessage(senderid, receiverId);
        if (count.equals("0")) {
            holder.tvNotificationCount.setVisibility(View.GONE);
        } else {
            holder.tvNotificationCount.setText(count);
            holder.tvNotificationCount.setVisibility(View.VISIBLE);
        }
        if (isRosterOnline(list.get(position).ejUser + Constant.XMPP_USERNAME_POSTFIX + Constant.NAME_POSTFIX)) {
            holder.ivOnline.setImageResource(R.drawable.online);
            holder.civProfile.setBorderColor(activity.getResources().getColor(R.color.greend));
        } else {
            holder.ivOnline.setImageResource(R.drawable.offline);
            holder.civProfile.setBorderColor(activity.getResources().getColor(R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.ivOnline)
        ImageView ivOnline;

        @BindView(R.id.civProfile)
        CircleImageView civProfile;

        @BindView(R.id.tvName)
        TextViewRegular tvName;

        @BindView(R.id.tvLastMessage)
        TextViewRegular tvLastMessage;

        @BindView(R.id.loader)
        ProgressBar loader;

        @BindView(R.id.tvNotificationCount)
        TextViewRegular tvNotificationCount;


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

    //TODO : check user is online or not
    public boolean isRosterOnline(String name) {

        try {
            if (MyXMPP.connection == null) {
                return false;
            }
            Roster roster = Roster.getInstanceFor(MyXMPP.connection);
            Presence userFromServer = roster.getPresence(JidCreate.bareFrom(name.toLowerCase()));
            Log.e(name,userFromServer.isAvailable()+"");
            return userFromServer.isAvailable();

        } catch (XmppStringprepException e) {
            Log.e("Xmpp :- ", e.getMessage());
            return false;

        }
    }
}
