package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 19-12-2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.MutualFriend;
import com.example.cupidlove.utils.URLS;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;


public class MutualFriendAdapter extends RecyclerView.Adapter<MutualFriendAdapter.CategoryGridHolder> {

    //TODO: Variable Declaration
    private List<MutualFriend.MutualFriendList> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    ProgressBar loader;

    public MutualFriendAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<MutualFriend.MutualFriendList> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }


    @Override
    public CategoryGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_mutual_friend, parent, false);
        loader = (ProgressBar)itemView.findViewById(R.id.loader);
        // work here if you need to control height of your items
        // keep in mind that parent is RecyclerView in this case
//        height = (parent.getMeasuredHeight() / 2) - 4;
        return new CategoryGridHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryGridHolder holder, final int position) {
//        holder.llMain.getLayoutParams().width = width;
//        holder.llMain.getLayoutParams().height = height;
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        loader.setVisibility(View.VISIBLE);
        holder.tvMutualFriendName.setText(list.get(position).fname + " " + list.get(position).lname);
        Picasso.with(activity).load(new URLS().UPLOAD_URL + list.get(position).profileImage)
                .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                .into(holder.civMutualFriendProfile, new Callback() {
                    @Override
                    public void onSuccess() {
                        loader.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {

                    }
                });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CategoryGridHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.civMutualFriendProfile)
        CircleImageView civMutualFriendProfile;

        @BindView(R.id.tvMutualFriendName)
        TextViewRegular tvMutualFriendName;

        public CategoryGridHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public void getWidthAndHeight() {
        int height_value = activity.getResources().getInteger(R.integer.height);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = height_value;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}