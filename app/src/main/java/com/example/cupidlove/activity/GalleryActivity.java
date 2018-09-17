package com.example.cupidlove.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewBold;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.utils.BaseSignUpActivity;
import com.example.cupidlove.utils.RequestParamUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GalleryActivity extends BaseSignUpActivity {

    //TODO:  Bind The All XML view With JAVA file
    @BindView(R.id.rvGallery)
    RecyclerView rvGallery;

    @BindView(R.id.tvTitle)
    TextViewBold tvTitle;

    //TODO: Variable Declaration
    GalleryAdapter galleryAdapter;
    private ArrayList<String> galleryImages = new ArrayList<>();
    private ArrayList<String> selectedImages = new ArrayList<>();
    ArrayList<String> listOfAllImages = new ArrayList<String>();

    int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        selectedPosition = getIntent().getIntExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, 0);

        tvTitle.setText(getResources().getString(R.string.gallery));

        setGalleryAdapter();
    }

    //TODO: set Done BUtton Click For gallary Image selection
    @OnClick(R.id.tvDone)
    public void tvDoneClick() {


    }


    //TODO: Set The Gallary Adapter
    public void setGalleryAdapter() {

        galleryAdapter = new GalleryAdapter(this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        rvGallery.setLayoutManager(mLayoutManager);
        rvGallery.setAdapter(galleryAdapter);
        rvGallery.setNestedScrollingEnabled(false);
        galleryImages = getAllShownImagesPath();
        galleryAdapter.addAll();
    }


    private ArrayList<String> getAllShownImagesPath() {
        new getDataFromGallery().execute();
//        Uri uri;
//        Cursor cursor;
//        int column_index_data, column_index_folder_name;
//        ArrayList<String> listOfAllImages = new ArrayList<String>();
//        String absolutePathOfImage = null;
//        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
//
//        String[] projection = {MediaStore.MediaColumns.DATA,
//                MediaStore.Images.Media.BUCKET_DISPLAY_NAME};
//
//        cursor = this.getContentResolver().query(uri, projection, null,
//                null, null);
//
//        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
//        column_index_folder_name = cursor
//                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
//        while (cursor.moveToNext()) {
//            absolutePathOfImage = cursor.getString(column_index_data);
//
//            listOfAllImages.add(absolutePathOfImage);
//        }
        return listOfAllImages;
    }

    //TODO : When Select Images From Gallary
    private class getDataFromGallery extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress("");
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Uri uri;
            Cursor cursor;
            int column_index_data, column_index_folder_name;

            String absolutePathOfImage = null;
            uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

            String[] projection = new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME, MediaStore.Images.Media.DATA};
            cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    null, null, MediaStore.Images.Media.DATE_ADDED);

            if (cursor == null) {
                return null;
            }
            if (cursor.moveToLast()) {
                do {
                    long id = cursor.getLong(cursor.getColumnIndex(projection[0]));
                    String name = cursor.getString(cursor.getColumnIndex(projection[2]));
                    if (!name.endsWith("gif")) {
                        listOfAllImages.add(name);
                    }

                } while (cursor.moveToPrevious());
            }
            cursor.close();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dismissProgress();
        }
    }

    //TODO: create gallery adapter
    class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.SpecialOfferViewHolder> {

        private Activity activity;
        private int width = 0, height = 0;

        public GalleryAdapter(Activity activity) {
            this.activity = activity;
        }

        public void addAll() {
            notifyDataSetChanged();
        }

        @Override
        public GalleryAdapter.SpecialOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gallery, parent, false);
            width = (parent.getMeasuredWidth() / 3) - 10;
            return new GalleryAdapter.SpecialOfferViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(GalleryAdapter.SpecialOfferViewHolder holder, final int position) {
            holder.llMain.getLayoutParams().height = width;
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    //select all images
                    int pos = 0; //pos 0 then add or 1 then remove
                    if (selectedImages != null) {
                        for (int i = 0; i < selectedImages.size(); i++) {
                            if (selectedImages.get(i).equals(galleryImages.get(position))) {
                                pos = 1;
                                break;
                            }
                        }
                    }


                    if (pos == 0) {
                        if (selectedPosition != 0) {
                            if (selectedImages.size() != 1) {
                                selectedImages.add(galleryImages.get(position));
                            } else {
                                Toast.makeText(activity, getResources().getString(R.string.select_one_image), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (selectedImages.size() == 6) {
                                Toast.makeText(activity, getResources().getString(R.string.select_six_image), Toast.LENGTH_SHORT).show();
                            } else {
                                selectedImages.add(galleryImages.get(position));
                            }
                        }
                    } else if (pos == 1) {
                        selectedImages.remove(galleryImages.get(position));
                    }
//                    notifyDataSetChanged();

                    notifyItemChanged(position);

                    for (int i = 0; i < selectedImages.size(); i++) {
                        for (int j = 0; j < galleryImages.size(); j++) {
                            if (selectedImages.get(i).equals(galleryImages.get(j))) {
                                notifyItemChanged(j);
                            }
                        }
                    }

                    Intent myIntent = new Intent();
                    myIntent.putExtra("image", "file:" + galleryImages.get(position));
                    setResult(RESULT_OK, myIntent);
                    finish();
                }

            });

            if (selectedImages != null) {
                for (int i = 0; i < selectedImages.size(); i++) {
                    if (selectedImages.get(i).equals(galleryImages.get(position))) {

                        holder.tvNumber.setVisibility(View.VISIBLE);
                        holder.ivImageSelected.setVisibility(View.VISIBLE);
                        if (i == 0) {
                            //first image
                            holder.tvNumber.setText("1");
                        } else if (i == 1) {
                            //second image
                            holder.tvNumber.setText("2");
                        } else if (i == 2) {
                            //third image
                            holder.tvNumber.setText("3");
                        } else if (i == 3) {
                            //fourth image
                            holder.tvNumber.setText("4");
                        } else if (i == 4) {
                            //fifth image
                            holder.tvNumber.setText("5");
                        } else if (i == 5) {
                            //sixth image
                            holder.tvNumber.setText("6");
                        }
                        break;
                    } else {
                        holder.tvNumber.setVisibility(View.GONE);
                        holder.ivImageSelected.setVisibility(View.GONE);
                    }
                }
            } else {
                holder.tvNumber.setVisibility(View.GONE);
                holder.ivImageSelected.setVisibility(View.GONE);
            }

            Glide.with(activity).
                    load("file:" + galleryImages.get(position)).
                    error(R.drawable.side_menu_logo).
                    override(200, 200).
                    centerCrop().
                    into(holder.ivImageView);


        }

        @Override
        public int getItemCount() {
            return galleryImages.size();
        }

        public class SpecialOfferViewHolder extends RecyclerView.ViewHolder {

            //TODO : Bind All XMl View with JAVA File
            @BindView(R.id.llMain)
            LinearLayout llMain;

            @BindView(R.id.ivImageView)
            ImageView ivImageView;

            @BindView(R.id.ivImageSelected)
            ImageView ivImageSelected;

            @BindView(R.id.tvNumber)
            TextViewRegular tvNumber;

            public SpecialOfferViewHolder(View view) {
                super(view);
                ButterKnife.bind(this, view);
            }
        }

        public void getWidthAndHeight() {
            int height_value = activity.getResources().getInteger(R.integer.five);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            width = displayMetrics.widthPixels / 2 - 15;
            height = width / 2;
        }

        @Override
        public int getItemViewType(int position) {
            return super.getItemViewType(position);
        }
    }

}
