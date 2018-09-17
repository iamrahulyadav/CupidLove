
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.cupidlove.R;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.InstagramUserImage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class InstaUserImageAdapter extends RecyclerView.Adapter<InstaUserImageAdapter.CategoryGridHolder> {

    //TODO : VAriable Declartion
    private List<InstagramUserImage.InstaImage> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    ProgressBar loader;

    public InstaUserImageAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
        getWidthAndHeight();
    }

    public void addAll(List<InstagramUserImage.InstaImage> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    @Override
    public CategoryGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_instagram_images, parent, false);
        loader = (ProgressBar)itemView.findViewById(R.id.loader);
        // work here if you need to control height of your items
        // keep in mind that parent is RecyclerView in this case
        height = (parent.getMeasuredWidth() / 3);
        width = height;
        return new CategoryGridHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryGridHolder holder, final int position) {
        holder.llMain.getLayoutParams().width = width;
        holder.llMain.getLayoutParams().height = height;
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        loader.setVisibility(View.VISIBLE);
        Picasso.with(activity).load(list.get(position).url).error(R.drawable.image_not_found).into(holder.ivInstaImage, new Callback() {
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

        //TODO : Bind The All XML View With JAVA file
        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.ivInstaImage)
        ImageView ivInstaImage;

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