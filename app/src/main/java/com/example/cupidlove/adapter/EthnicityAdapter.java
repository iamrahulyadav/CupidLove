package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 19-12-2017.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class EthnicityAdapter extends RecyclerView.Adapter<EthnicityAdapter.CategoryGridHolder> {

    //TODO : Variable Declaration
    private List<JSONObject> list = new ArrayList<>();
    private List<String> listSelected = new ArrayList<>();
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;
    private SharedPreferences sharedpreferences;

    public EthnicityAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<JSONObject> list, List<String> listSelected) {
        this.list = list;
        this.listSelected = listSelected;
        getWidthAndHeight();
        notifyDataSetChanged();
    }


    @Override
    public CategoryGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_religion, parent, false);

        // work here if you need to control height of your items
        // keep in mind that parent is RecyclerView in this case
        width = (parent.getMeasuredWidth() / 2) - 10;
        return new CategoryGridHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryGridHolder holder, final int position) {
        holder.llMain.getLayoutParams().width = width;
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tvTitle.getBackground() instanceof ColorDrawable) {
//                    holder.tvTitle.setBackgroundResource(R.drawable.email_btn_bg);
                    onItemClickListner.onItemClick(position, "ethnicity", 1);
                } else {
//                    holder.tvTitle.setBackgroundResource(R.color.transparent_white);
                    onItemClickListner.onItemClick(position, "ethnicity", 0);
                }
            }
        });

        try {
            if (listSelected != null) {
                Boolean b = false;
                for (int i = 0; i < listSelected.size(); i++) {
                    if (list.get(position).getString("id").equals(listSelected.get(i))) {
                        b = true;
                        break;
                    }
                }

                if (b) {
                    holder.tvTitle.setBackgroundResource(R.drawable.email_btn_bg);
                } else {
                    holder.tvTitle.setBackgroundResource(R.color.transparent_white);
                }
            }
            holder.tvTitle.setText(list.get(position).getString(Prefs.getString(RequestParamUtils.LANGUAGE, "en")));
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

    }

    public SharedPreferences getPreferences() {
        sharedpreferences = activity.getSharedPreferences(
                Constant.MyPREFERENCES, Context.MODE_PRIVATE);
        return sharedpreferences;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CategoryGridHolder extends RecyclerView.ViewHolder {

        //TODO : Bind The All XML View with JAVA file
        @BindView(R.id.llMain)
        LinearLayout llMain;

        @BindView(R.id.tvTitle)
        TextViewRegular tvTitle;

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