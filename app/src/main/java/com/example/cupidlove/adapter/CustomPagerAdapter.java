package com.example.cupidlove.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.cupidlove.R;
import com.example.cupidlove.activity.FriendProfileActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class CustomPagerAdapter extends PagerAdapter {

    //TODO : VAriable Declaration
    private final Context mContext;
    private final ArrayList<String> arrStr;
    private LinearLayout linearLayout;
    //    private ImageView mimgvw;
    ImageView mimgvw, imgReportUser;
    private int dotsCount = 0;
    private View mview;
    private int myPos;
    private String userId;
    LayoutInflater mLayoutInflater;
    ProgressBar loader;


    public CustomPagerAdapter(Context context, ArrayList<String> marr, String s) {
        this.mContext = context;
        arrStr = marr;
        userId = s;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return arrStr.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.item_scroll_friend_image, container, false);
        loader = (ProgressBar) itemView.findViewById(R.id.loader);
        mimgvw = (ImageView) itemView.findViewById(R.id.image_view);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.layoutDots);

        myPos = position;
        Log.e("Position", "" + position);
        setMyPos(myPos);

        if (linearLayout != null) {
            linearLayout.removeAllViewsInLayout();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);

        if (arrStr != null) {


            if (arrStr != null) {
                loader.setVisibility(View.VISIBLE);
                Picasso.with(mContext).load(new URLS().UPLOAD_URL + arrStr.get(position))
                        .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                        .into(mimgvw, new Callback() {
                            @Override
                            public void onSuccess() {
                                loader.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError() {

                            }
                        });

                mimgvw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Intent intent = new Intent(mContext, FriendProfileActivity.class);
                        intent.putExtra(RequestParamUtils.ID, userId + "");
                        mContext.startActivity(intent);
                    }
                });
            }
        }
//        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
//        imageView.setImageResource(arrStr.get(position));
//
        container.addView(itemView);

        return itemView;
    }

    public void setMyPos(int p) {
        myPos = p;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
}