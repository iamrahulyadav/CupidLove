package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 28-12-2017.
 */

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.GooglePlaces;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 17-11-2017.
 */

public class SelectedPlacesAdapter extends RecyclerView.Adapter<SelectedPlacesAdapter.RecentViewHolder> {

    //TODO : Variable Declaration
    private List<GooglePlaces.Result> list = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    ProgressBar loader;

    public SelectedPlacesAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<GooglePlaces.Result> list) {
        this.list = list;
        getWidthAndHeight();
        notifyDataSetChanged();
    }


    @Override
    public RecentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_places, parent, false);
            loader  = (ProgressBar)itemView.findViewById(R.id.loader);
        return new RecentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecentViewHolder holder, final int position) {
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        holder.tvRandomColor.setBackgroundColor(color);
        loader.setVisibility(View.VISIBLE);
        if (list.get(position).photos != null && list.get(position).photos.size() != 0) {
            String strUrl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + list.get(position).photos.get(0).photoReference + "&key=" + Constant.GOOGLE_PLACE_API_KEY;
            Picasso.with(activity).load(strUrl).error(R.drawable.image_not_found).into(holder.ivPlaceImage, new Callback() {

                @Override
                public void onSuccess() {
                  loader.setVisibility(View.GONE);
                }

                @Override
                public void onError() {

                }
            });
        } else {
            holder.ivPlaceImage.setImageResource(R.drawable.image_not_found);
        }
        holder.tvPlaceTitle.setText(list.get(position).name + "");

        String distance = String.format("%.2f", distance(list.get(position).geometry.location.lat, list.get(position).geometry.location.lng)) + " miles";
        holder.tvPlaceDistance.setText(distance);

        if (list.get(position).rating <= 1) {
            holder.ivStarOne.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarTwo.setImageResource(R.drawable.ic_star_blank);
            holder.ivStarThree.setImageResource(R.drawable.ic_star_blank);
            holder.ivStarFour.setImageResource(R.drawable.ic_star_blank);
            holder.ivStarFive.setImageResource(R.drawable.ic_star_blank);
        } else if (list.get(position).rating <= 2) {
            holder.ivStarOne.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarTwo.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarThree.setImageResource(R.drawable.ic_star_blank);
            holder.ivStarFour.setImageResource(R.drawable.ic_star_blank);
            holder.ivStarFive.setImageResource(R.drawable.ic_star_blank);
        } else if (list.get(position).rating <= 3) {
            holder.ivStarOne.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarTwo.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarThree.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarFour.setImageResource(R.drawable.ic_star_blank);
            holder.ivStarFive.setImageResource(R.drawable.ic_star_blank);
        } else if (list.get(position).rating <= 4) {
            holder.ivStarOne.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarTwo.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarThree.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarFour.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarFive.setImageResource(R.drawable.ic_star_blank);
        } else if (list.get(position).rating <= 5) {
            holder.ivStarOne.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarTwo.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarThree.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarFour.setImageResource(R.drawable.ic_star_filled);
            holder.ivStarFive.setImageResource(R.drawable.ic_star_filled);
        }

    }

    private double distance(double lat2, double lon2) {
        double lat1;
        double lon1;
        lat1 = Double.parseDouble(((BaseActivity) activity).getPreferences().getString(RequestParamUtils.LATITUDE, "0"));
        lon1 = Double.parseDouble(((BaseActivity) activity).getPreferences().getString(RequestParamUtils.LONGITUDE, "0"));

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 0.621371;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class RecentViewHolder extends RecyclerView.ViewHolder {

        //TODO : Bind The All XML View With JAVA File
        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.tvRandomColor)
        TextView tvRandomColor;

        @BindView(R.id.ivPlaceImage)
        ImageView ivPlaceImage;

        @BindView(R.id.tvPlaceTitle)
        TextViewRegular tvPlaceTitle;

        @BindView(R.id.tvPlaceDistance)
        TextViewRegular tvPlaceDistance;

        @BindView(R.id.ivStarOne)
        ImageView ivStarOne;

        @BindView(R.id.ivStarTwo)
        ImageView ivStarTwo;

        @BindView(R.id.ivStarThree)
        ImageView ivStarThree;

        @BindView(R.id.ivStarFour)
        ImageView ivStarFour;

        @BindView(R.id.ivStarFive)
        ImageView ivStarFive;

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
}
