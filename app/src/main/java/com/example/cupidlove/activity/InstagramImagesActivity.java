package com.example.cupidlove.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewBold;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.helper.InstagramApp;
import com.example.cupidlove.model.InstagramImages;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InstagramImagesActivity extends BaseActivity implements ResponseListener {

    //TODO: Bind All XML View With JAVA File
    @BindView(R.id.rvGallery)
    RecyclerView rvGallery;

    @BindView(R.id.tvTitle)
    TextViewBold tvTitle;

    //TODO: Variable Declaration
    private InstagramApp mApp;
    private boolean isFirst = false;
    InstagramImageAdapter instagramImageAdapter;
    InstagramImages instagramImagesRider;

    private List<InstagramImages.Datum> galleryImages = new ArrayList<>();
    private ArrayList<String> selectedImages = new ArrayList<>();

    int selectedPosition;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instagram_images);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        selectedPosition = getIntent().getIntExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, 0);

        instaLogin();
        setInstagramImageAdapter();
    }

    //TODO: Instagram Login User
    public void instaLogin() {
        mApp = new InstagramApp(this, RequestParamUtils.INSTAGRAM_CLIENT_ID,
                RequestParamUtils.INSTAGRAM_CLIENT_SECRET, RequestParamUtils.INSTAGRAM_CALLBACK_URL);
        if (mApp.hasAccessToken()) {
            Log.e("done", mApp.getName());
            getInstaPhotos(null);
        } else {

            mApp.setListener(new InstagramApp.OAuthAuthenticationListener() {

                @Override
                public void onSuccess() {
                    //login done
                    getInstaPhotos(null);
                }

                @Override
                public void onFail(String error) {
                    Toast.makeText(InstagramImagesActivity.this, error, Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    //TODO: GET the Instagram Image Which Upload By User
    private void getInstaPhotos(String str) {

        Log.e("InstagramApp", "Connected as " + mApp.getUserName() + " ID " + mApp.getId());
        String instaApi;
        if (str != null) {
            instaApi = str;

        } else if (isFirst == false) {
            isFirst = true;
            instaApi = "https://api.instagram.com/v1/users/" + mApp.getId() + "/media/recent/?access_token=" + mApp.getAccessToken();

        } else {
            return;
        }
        Log.w("imagesInsta1", instaApi);

        //api call from here
        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.get(instaApi, params, new ResponseHandler(this, this, "instagram_images"));

        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }

    }

    //TODO: Response of all api call
    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();

        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("instagram_images")) {

            loading = true;

            //get user data
            Log.e("response", response);

            instagramImagesRider = new Gson().fromJson(
                    response, new TypeToken<InstagramImages>() {
                    }.getType());

            for (int i = 0; i < instagramImagesRider.data.size(); i++) {
                galleryImages.add(instagramImagesRider.data.get(i));
            }
            instagramImageAdapter.addAll();

        }
    }

    //TODO: Done button Click When User Select images
    @OnClick(R.id.tvDone)
    public void tvDoneClick() {
        if (selectedPosition == 0) {
            Constant.selectedImages.addAll(selectedImages);
        } else {
            for (int i = 0; i < Constant.selectedImages.size(); i++) {
                if (i == selectedPosition - 1) {
                    Constant.selectedImages.remove(selectedPosition - 1);
                    Constant.selectedImages.add(selectedPosition - 1, selectedImages.get(0));
                }
            }
        }

        finish();
    }

    //TODO: Instagram Adapter for images Selection
    public void setInstagramImageAdapter() {

        instagramImageAdapter = new InstagramImageAdapter(this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        rvGallery.setLayoutManager(mLayoutManager);
        rvGallery.setAdapter(instagramImageAdapter);
        rvGallery.setNestedScrollingEnabled(false);
        instagramImageAdapter.addAll();
        rvGallery.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) //check for scroll down
                {
                    visibleItemCount = mLayoutManager.getChildCount();
                    totalItemCount = mLayoutManager.getItemCount();
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                loading = false;
                                getInstaPhotos(instagramImagesRider.pagination.nextUrl);
                        }
                    }
                }
            }
        });

    }

    //TODO: Instagram Adapter Bind with Recyclerview
    class InstagramImageAdapter extends RecyclerView.Adapter<InstagramImageAdapter.SpecialOfferViewHolder> {

        private Activity activity;
        private int width = 0, height = 0;

        public InstagramImageAdapter(Activity activity) {
            this.activity = activity;
        }

        public void addAll() {
            notifyDataSetChanged();
        }

        @Override
        public InstagramImageAdapter.SpecialOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gallery, parent, false);
            width = (parent.getMeasuredWidth() / 3) - 10;
            height = (parent.getMeasuredWidth() / 3) - 10;
            return new InstagramImageAdapter.SpecialOfferViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(InstagramImageAdapter.SpecialOfferViewHolder holder, final int position) {
            holder.llMain.getLayoutParams().width = width;
            holder.llMain.getLayoutParams().height = height;
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = 0; //pos 0 then add or 1 then remove
                    if (selectedImages != null) {
                        for (int i = 0; i < selectedImages.size(); i++) {
                            if (selectedImages.get(i).equals(galleryImages.get(position).images.standardResolution.url)) {
                                pos = 1;
                                break;
                            }
                        }
                    }

                    if (pos == 0) {
                        if (selectedPosition != 0) {
                            if (selectedImages.size() != 1) {
                                selectedImages.add(galleryImages.get(position).images.standardResolution.url);
                            } else {
                                Toast.makeText(activity, getResources().getString(R.string.select_one_image), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (selectedImages.size() == 6) {
                                Toast.makeText(activity, getResources().getString(R.string.select_six_image), Toast.LENGTH_SHORT).show();
                            } else {
                                selectedImages.add(galleryImages.get(position).images.standardResolution.url);
                            }
                        }
                    } else {
                        selectedImages.remove(galleryImages.get(position).images.standardResolution.url);
                    }
                    notifyItemChanged(position);

                    for (int i = 0; i < selectedImages.size(); i++) {
                        for (int j = 0; j < galleryImages.size(); j++) {
                            if (selectedImages.get(i).equals(galleryImages.get(j).images.standardResolution.url)) {
                                notifyItemChanged(j);
                            }
                        }
                    }
                }
            });

            if (selectedImages != null) {
                for (int i = 0; i < selectedImages.size(); i++) {
                    if (selectedImages.get(i).equals(galleryImages.get(position).images.standardResolution.url)) {

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

            Picasso.with(activity).
                    load(galleryImages.get(position).images.standardResolution.url).
                    error(R.drawable.side_menu_logo).
                    into(holder.ivImageView);
        }

        @Override
        public int getItemCount() {
            return galleryImages.size();
        }

        //TODO : Special Offer set On HOlder
        public class SpecialOfferViewHolder extends RecyclerView.ViewHolder {

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
