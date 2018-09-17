package com.example.cupidlove.adapter;

/**
 * Created by Kaushal on 19-12-2017.
 */

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class KidsAdapter extends RecyclerView.Adapter<KidsAdapter.CategoryGridHolder> {

    //TODO : Variable Declaration
    private List<String> list = new ArrayList<>();
    private String selectedValue;
    private Activity activity;
    private OnItemClickListner onItemClickListner;
    private int width = 0, height = 0;

    public KidsAdapter(Activity activity, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.onItemClickListner = onItemClickListner;
    }

    public void addAll(List<String> list, String selectedValue) {
        this.list = list;
        this.selectedValue = selectedValue;
        getWidthAndHeight();
        notifyDataSetChanged();
    }

    @Override
    public CategoryGridHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_kids, parent, false);

        return new CategoryGridHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CategoryGridHolder holder, final int position) {
        holder.llMain.getLayoutParams().width = width;
        holder.llMain.getLayoutParams().height = height;
        holder.llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListner.onItemClick(position,"kids", list.size());
            }
        });

        holder.tvTitle.setText(list.get(position));
        if (list.get(position).equals(selectedValue)) {
            //selected kid
            holder.tvTitle.setBackgroundResource(R.drawable.email_btn_bg);
        } else {
            //non selected kid
            holder.tvTitle.setBackgroundResource(R.drawable.kids_border);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CategoryGridHolder extends RecyclerView.ViewHolder {

        //TODO: Bind The All XML view With JAVA file
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
        int dp = (int) activity.getResources().getDimension(R.dimen.value_30) + 10;
        width = dp;
        height = dp;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }
}