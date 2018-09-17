package com.example.cupidlove.adapter;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.cupidlove.R;
import com.example.cupidlove.utils.URLS;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CustomImageSwipeAdapter extends RecyclerView.Adapter<CustomImageSwipeAdapter.ViewHolder> {

    //TODO: Variable Declaration
    private final Activity mcontx;
    private final ArrayList<String> arrStr;
    private LinearLayout linearLayout;
    //    private ImageView mimgvw;

    private int dotsCount = 0;
    private View mview;
    private int myPos;
    private String userId;

    public CustomImageSwipeAdapter(Activity mContext, ArrayList<String> marr, String s) {
        mcontx = mContext;
        arrStr = marr;
        userId = s;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mview = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scroll_friend_image, parent, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        mview.setLayoutParams(params);
        return new ViewHolder(mview);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        myPos = position;
        setMyPos(myPos);
        holder.setIsRecyclable(false);

        if (linearLayout != null) {
            linearLayout.removeAllViewsInLayout();
        }
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 10, 0, 0);

        if (arrStr != null) {
            dotsCount = arrStr.size();

            for (int i = 0; i < dotsCount; i++) {

                ImageView imageView = new ImageView(mcontx);
                imageView.setLayoutParams(params);

                if (arrStr != null) {

                    Picasso.with(mcontx).load(new URLS().UPLOAD_URL + arrStr.get(position))
                            .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                            .into(holder.mimgvw, new Callback() {
                                @Override
                                public void onSuccess() {
                                    holder.loader.setVisibility(View.GONE);
                                }

                                @Override
                                public void onError() {
                                    holder.loader.setVisibility(View.GONE);
                                }
                            });


                    if (i == position) {
                        imageView.setImageDrawable(mcontx.getResources().getDrawable(R.drawable.circle_view));
                    } else {
                        imageView.setImageDrawable(mcontx.getResources().getDrawable(R.drawable.gray_circle));
                    }
                    linearLayout.addView(imageView);
                }
            }
        }
    }


    @Override
    public int getItemCount() {
        return arrStr.size();
//        return 5;
    }

    public int getPos() {
        return myPos;
    }

    public void setMyPos(int p) {
        myPos = p;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.loader)
        ProgressBar loader;

        @BindView(R.id.image_view)
        ImageView mimgvw;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            mimgvw = (ImageView) itemView.findViewById(R.id.image_view);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.layoutDots);

        }
    }


}


