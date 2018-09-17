package com.example.cupidlove.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.Nullable;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.textview.TextViewBold;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.model.FacebookImages;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FacebookImagesActivity extends BaseActivity implements ResponseListener {

    //TODO: Bind All XML View With JAVA file
    @BindView(R.id.rvGallery)
    RecyclerView rvGallery;

    @BindView(R.id.tvTitle)
    TextViewBold tvTitle;

    //TODO: VAriable Declaration
    FacebookImageAdapter facebookImageAdapter;
    private List<FacebookImages.Datum> galleryImages = new ArrayList<>();
    private ArrayList<String> selectedImages = new ArrayList<>();
    int selectedPosition;
    CallbackManager callbackManager;
    FacebookImages facebookImagesRider;

    int pastVisiblesItems, visibleItemCount, totalItemCount;
    private boolean loading = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_images);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        selectedPosition = getIntent().getIntExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, 0);
        tvTitle.setText(getResources().getString(R.string.fb_images));
        showProgress("");
        loginWithFB();
        if (AccessToken.getCurrentAccessToken() == null) {
            //fb login
            LoginManager.getInstance().logInWithReadPermissions(
                    this,
                    Arrays.asList("user_photos", "email")
            );

        } else {
            //get photo from fb
            getFacebookImages();
//            Intent intent = new Intent(this, FacebookImagesActivity.class);
//            intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
//            startActivity(intent);
        }

        setFacebookImageAdapter();
    }

    //TODO: When Usre Login With FaceBook
    public void loginWithFB() {
        callbackManager = CallbackManager.Factory.create();

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                String accessToken = loginResult.getAccessToken()
                        .getToken();
                Log.i("accessToken", accessToken);

                //get photo from fb
                getFacebookImages();
            }

            @Override
            public void onCancel() {
                // App code
                finish();
                Toast.makeText(FacebookImagesActivity.this, getResources().getString(R.string.login_canceled), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                finish();
                Toast.makeText(FacebookImagesActivity.this, getResources().getString(R.string.something_went_wrong_try_after_somtime), Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        callbackManager.onActivityResult(requestCode, resultCode, imageReturnedIntent);
    }

    //TODO: When User Want To Upload Photo With FaceBook
    public void getFacebookImages() {

        if (facebookImagesRider != null) {
            if (facebookImagesRider.paging.next != null) {
                getFBimages();
            }
            return;
        }

        GraphRequest request = new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/photos/uploaded",
                null,
                HttpMethod.GET,
                new GraphRequest.Callback() {
                    public void onCompleted(final GraphResponse response) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                setResponseData(response.getJSONObject().toString() + "");
                            }
                        });
            /* handle the result */
                    }
                }
        );
        Bundle parameters = new Bundle();
        parameters.putString("fields", "id,picture.width(1024).height(1024),name,count");
        request.setParameters(parameters);
        request.executeAsync();


    }

    //TODO: Set the Response when user select  the Photos with Facebook
    public void setResponseData(String Response) {

        try {
            Log.e("Facebook Album Respons", Response);

            facebookImagesRider = new Gson().fromJson(
                    Response, new TypeToken<FacebookImages>() {
                    }.getType());
            galleryImages = new ArrayList<>();

            galleryImages.addAll(facebookImagesRider.data);
            facebookImageAdapter.addAll();
            dismissProgress();

        } catch (Exception e) {
            Log.e("Exception is ", e.getMessage());

        }
    }

    //TODO: When User Click On Done Button
    @OnClick(R.id.tvDone)
    public void tvDoneClick() {
        String path = onCaptureImageResult(selectedImages.get(0));

        Intent myIntent = new Intent();
        myIntent.putExtra("image", "file:" +path);
        setResult(RESULT_OK, myIntent);
        finish();


        finish();
    }

    //TODO: When User Want to click Photos
    @Nullable
    private String onCaptureImageResult(String data) {

        try {
            URL url = new URL(data);
            Bitmap thumbnail = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
            File destination = new File(Environment.getExternalStorageDirectory(),
                    System.currentTimeMillis() + ".jpg");
            FileOutputStream fo;
            try {
                destination.createNewFile();
                fo = new FileOutputStream(destination);
                fo.write(bytes.toByteArray());
                fo.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String path = "file:" + destination;
            return path;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
    }

    //TODO: Set The FaceBook Image Adapter
    public void setFacebookImageAdapter() {

        facebookImageAdapter = new FacebookImageAdapter(this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        rvGallery.setLayoutManager(mLayoutManager);
        rvGallery.setAdapter(facebookImageAdapter);
        rvGallery.setNestedScrollingEnabled(false);
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
                            getFacebookImages();
                        }
                    }
                }
            }
        });
        facebookImageAdapter.addAll();
    }

    //TODO: Bind The View with Facebook Image Adapter
    class FacebookImageAdapter extends RecyclerView.Adapter<FacebookImageAdapter.SpecialOfferViewHolder> {

        private Activity activity;
        private int width = 0, height = 0;

        public FacebookImageAdapter(Activity activity) {
            this.activity = activity;
        }

        public void addAll() {
            notifyDataSetChanged();
        }

        @Override
        public FacebookImageAdapter.SpecialOfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_gallery, parent, false);
            width = (parent.getMeasuredWidth() / 3) - 10;
            height = (parent.getMeasuredWidth() / 3) - 10;
            return new FacebookImageAdapter.SpecialOfferViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(FacebookImageAdapter.SpecialOfferViewHolder holder, final int position) {
            holder.llMain.getLayoutParams().width = width;
            holder.llMain.getLayoutParams().height = height;
            holder.llMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = 0; //pos 0 then add or 1 then remove
                    if (selectedImages != null) {
                        for (int i = 0; i < selectedImages.size(); i++) {
                            if (selectedImages.get(i).equals(galleryImages.get(position).picture)) {
                                pos = 1;
                                break;
                            }
                        }
                    }

                    if (pos == 0) {
                        if (selectedPosition != 0) {
                            if (selectedImages.size() != 1) {
                                selectedImages.add(galleryImages.get(position).picture);
                            } else {
                                Toast.makeText(activity, getResources().getString(R.string.select_one_image), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (selectedImages.size() == 6) {
                                Toast.makeText(activity, getResources().getString(R.string.select_six_image), Toast.LENGTH_SHORT).show();
                            } else {
                                selectedImages.add(galleryImages.get(position).picture);
                            }
                        }
                    } else {
                        selectedImages.remove(galleryImages.get(position).picture);
                    }
                    notifyItemChanged(position);

                    for (int i = 0; i < selectedImages.size(); i++) {
                        for (int j = 0; j < galleryImages.size(); j++) {
                            if (selectedImages.get(i).equals(galleryImages.get(j).picture)) {
                                notifyItemChanged(j);
                            }
                        }
                    }
                }
            });

            if (selectedImages != null) {
                for (int i = 0; i < selectedImages.size(); i++) {
                    if (selectedImages.get(i).equals(galleryImages.get(position).picture)) {

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
                    load(galleryImages.get(position).picture).
                    error(R.drawable.side_menu_logo).
                    into(holder.ivImageView);
        }

        @Override
        public int getItemCount() {
            return galleryImages.size();
        }

        //TODO: Set The Special Offer of the User
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

    //TODO: Get images from facebook
    public void getFBimages() {

        try {
            showProgress("");

            RequestParams params = new RequestParams();

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.get(facebookImagesRider.paging.next, params, new ResponseHandler(this, this, "fbimages"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }

    }

    //TODO: response from api
    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();

        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("fbimages")) {
            loading = true;
            try {
                Log.e("Facebook Album Respons", response);

                facebookImagesRider = new Gson().fromJson(
                        response, new TypeToken<FacebookImages>() {
                        }.getType());

                for (int i = 0; i < facebookImagesRider.data.size(); i++) {
                    galleryImages.add(facebookImagesRider.data.get(i));
                }

                facebookImageAdapter.addAll();
                dismissProgress();

            } catch (Exception e) {
                Log.e("Exception is ", e.getMessage());

            }
        }
    }

}
