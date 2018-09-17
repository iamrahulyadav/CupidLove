package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 28-12-2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Config;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by UV on 29-Nov-16.
 */
public class SideMenuAdapter extends BaseAdapter {

    //TODO : VAriable Declaration
    private Context context;
    private List<String> list = new ArrayList<>();
    private List<String> listImages = new ArrayList<>();
    private LayoutInflater inflater;
    private int seprater, count;
    private int intImages[] = {R.drawable.ic_play, R.drawable.ic_send_message, R.drawable.ic_notification,R.drawable.profile, R.drawable.ic_settings, R.drawable.ic_location_white, R.drawable.remove_ad};

    public SideMenuAdapter(Context context) {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        String in_app_purchase = ((BaseActivity) context).getPreferences().getString(Config.INAPPPURCHASE, null);

        if (in_app_purchase != null && !in_app_purchase.isEmpty()) {
            if (((BaseActivity) context).getPreferences().getBoolean(RequestParamUtils.ADENABLED, false)) {
                list = Arrays.asList(
                        context.getResources().getString(R.string.start_playing),
                        context.getResources().getString(R.string.chat),
                        context.getResources().getString(R.string.notification),
                        context.getResources().getString(R.string.my_profile),
                        context.getResources().getString(R.string.my_preferance),
                        context.getResources().getString(R.string.location));
            } else {
                list = Arrays.asList(
                        context.getResources().getString(R.string.start_playing),
                        context.getResources().getString(R.string.chat),
                        context.getResources().getString(R.string.notification),
                        context.getResources().getString(R.string.my_profile),
                        context.getResources().getString(R.string.my_preferance),
                        context.getResources().getString(R.string.location),
                        context.getResources().getString(R.string.remove_ads));
            }
        } else {
            list = Arrays.asList(
                    context.getResources().getString(R.string.start_playing),
                    context.getResources().getString(R.string.chat),
                    context.getResources().getString(R.string.notification),
                    context.getResources().getString(R.string.my_profile),
                    context.getResources().getString(R.string.my_preferance),
                    context.getResources().getString(R.string.location));
        }

        notifyDataSetChanged();
    }

    public void addAll(int count) {
        this.count = count;
        notifyDataSetChanged();
    }



    public List<String> getDrawerList() {
        return list;
    }

    public void setSepreter(int seprater) {
        this.seprater = seprater;
    }

    public int getSeprater() {
        return this.seprater;
    }

    public List<String> getList() {
        return this.list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        NavigationDrawerViewHolder listViewHolder;
        listViewHolder = new NavigationDrawerViewHolder();
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_nav, viewGroup, false);

            listViewHolder.tvTitle = (TextViewRegular) convertView.findViewById(R.id.tvTitle);
            listViewHolder.llMain = convertView.findViewById(R.id.llMain);
            listViewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
            listViewHolder.tvNotificationCount = (TextViewRegular) convertView.findViewById(R.id.tvNotificationCount);
            convertView.setTag(listViewHolder);

        } else {
            listViewHolder = (NavigationDrawerViewHolder) convertView.getTag();
        }

//        int height = (viewGroup.getMeasuredHeight() / 5) - 5;
//        listViewHolder.llMain.getLayoutParams().height = height;

        if (list.get(i).equals(context.getResources().getString(R.string.notification))) {
            if (count != 0) {
                listViewHolder.tvNotificationCount.setVisibility(View.VISIBLE);
                listViewHolder.tvNotificationCount.setText(count + "");
            } else {
                listViewHolder.tvNotificationCount.setVisibility(View.GONE);
            }
        } else if (list.get(i).equals(context.getResources().getString(R.string.chat))) {
            if ( Constant.UNREADCHAt!= 0) {
                listViewHolder.tvNotificationCount.setVisibility(View.VISIBLE);
                listViewHolder.tvNotificationCount.setText(Prefs.getInt(RequestParamUtils.UNREADCHAT, 0) + "");
            } else {
                listViewHolder.tvNotificationCount.setVisibility(View.GONE);
            }
        } else {
            listViewHolder.tvNotificationCount.setVisibility(View.GONE);
        }
        listViewHolder.tvTitle.setText(list.get(i));
        listViewHolder.ivIcon.setImageResource(intImages[i]);
        return convertView;
    }
}

class NavigationDrawerViewHolder {
    TextViewRegular tvTitle;
    ImageView ivIcon;
    LinearLayout llMain;
    TextViewRegular tvNotificationCount;
}