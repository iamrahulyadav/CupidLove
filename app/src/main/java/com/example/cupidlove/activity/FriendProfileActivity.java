package com.example.cupidlove.activity;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.adapter.InstaUserImageAdapter;
import com.example.cupidlove.adapter.ShowEthnicityAdapter;
import com.example.cupidlove.adapter.ShowReligionAdapter;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.InstagramUserImage;
import com.example.cupidlove.model.UserDetail;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Config;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FriendProfileActivity extends BaseActivity implements OnItemClickListner, ResponseListener {

    //TODO : Bind The All XML View With the JAVA File
    @BindView(R.id.rvReligion)
    RecyclerView rvReligion;

    @BindView(R.id.rvEthnicity)
    RecyclerView rvEthnicity;

    @BindView(R.id.rvInstaUserImages)
    RecyclerView rvInstaUserImages;

    @BindView(R.id.vpProfileImages)
    ViewPager vpProfileImages;

    @BindView(R.id.llDots)
    LinearLayout llDots;

    @BindView(R.id.tvQuote)
    TextViewRegular tvQuote;

    @BindView(R.id.tvQuoteAns)
    TextViewRegular tvQuoteAns;

    @BindView(R.id.tvAboutMe)
    TextViewRegular tvAboutMe;

    @BindView(R.id.ivAge)
    ImageView ivAge;

    @BindView(R.id.tvAge)
    TextViewRegular tvAge;

    @BindView(R.id.ivHeight)
    ImageView ivHeight;

    @BindView(R.id.tvHeight)
    TextViewRegular tvHeight;

    @BindView(R.id.ivGraduate)
    ImageView ivGraduate;

    @BindView(R.id.tvGraduate)
    TextViewRegular tvGraduate;

    @BindView(R.id.ivProfession)
    ImageView ivProfession;

    @BindView(R.id.tvProfession)
    TextViewRegular tvProfession;

    @BindView(R.id.ivBaby)
    ImageView ivBaby;

    @BindView(R.id.friendprofilename)
    TextViewRegular friendprofilename;

    @BindView(R.id.tvBaby)
    TextViewRegular tvBaby;

    @BindView(R.id.llInstanotConnected)
    LinearLayout llInstanotConnected;

    @BindView(R.id.llInstaConnected)
    LinearLayout llInstaConnected;

    @BindView(R.id.llEthnicity)
    LinearLayout llEthnicity;

    @BindView(R.id.llReligion)
    LinearLayout llReligion;

    @BindView(R.id.llInstaView)
    LinearLayout llInstaView;

    @BindView(R.id.nsvFriendProfile)
    NestedScrollView nsvFriendProfile;


    //TODO : VAriable Declaration
    private MyViewPagerAdapter myViewPagerAdapter;
    private int currentPosition;

    ShowReligionAdapter showReligionAdapter;
    ShowEthnicityAdapter showEthnicityAdapter;
    InstaUserImageAdapter instaUserImageAdapter;

    List<String> listImages = new ArrayList<>();
    List<JSONObject> listQuestion = new ArrayList<>();
    List<JSONObject> listEthnicity = new ArrayList<>();
    List<JSONObject> listReligion = new ArrayList<>();
    ProgressBar loader;
    UserDetail userDetailRider;
    String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        friendId = getIntent().getStringExtra(RequestParamUtils.ID);
        getDefaultData();
        loader = (ProgressBar) findViewById(R.id.loader);
        setEthnicityAdapter();
        setReligionAdapter();
        setInstagramImagesAdapter();

        String insta_client_id = getPreferences().getString(Config.INSTAGRAM_CLIENT_ID, "");
        String insta_client_secret = getPreferences().getString(Config.INSTAGRAM_CLIENT_SECRET, "");
        String insta_callback_base = getPreferences().getString(Config.INSTAGRAM_CALLBACK_BASE, "");
        if (insta_client_id == "" || insta_client_secret == "" || insta_callback_base == "")
        {
            llInstaView.setVisibility(View.GONE);
        }


        nsvFriendProfile.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                if (scrollY > oldScrollY) {
                    Log.i("", "Scroll DOWN");
//                    vpProfileImages.getLayoutParams().height = vpProfileImages.getLayoutParams().height + scrollY;
                }

                if (scrollY < oldScrollY) {
                    Log.i("", "Scroll UP");
//                    vpProfileImages.getLayoutParams().height = vpProfileImages.getLayoutParams().height - scrollY;
                }

                if (scrollY == 0) {
                    Log.i("", "TOP SCROLL");
//                    vpProfileImages.getLayoutParams().height = vpProfileImages.getLayoutParams().height - scrollY;
                }

                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    Log.i("", "BOTTOM SCROLL");
                }
            }
        });
    }

    //TODO: Back button click
    @OnClick(R.id.back)
    public void backClick() {
        onBackPressed();
    }

    //TODO : Instagram Adapter
    public void setInstagramImagesAdapter() {
        instaUserImageAdapter = new InstaUserImageAdapter(this, this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false);
        rvInstaUserImages.setLayoutManager(mLayoutManager);
        rvInstaUserImages.setAdapter(instaUserImageAdapter);
        rvInstaUserImages.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvInstaUserImages, false);
    }

    //TODO : Set Religion adapter
    public void setReligionAdapter() {
        showReligionAdapter = new ShowReligionAdapter(this, this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        rvReligion.setLayoutManager(mLayoutManager);
        rvReligion.setAdapter(showReligionAdapter);
        rvReligion.setNestedScrollingEnabled(true);
    }

    //TODO : Set Ethnicity Adapter
    public void setEthnicityAdapter() {
        showEthnicityAdapter = new ShowEthnicityAdapter(this, this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvEthnicity.setLayoutManager(mLayoutManager);
        rvEthnicity.setAdapter(showEthnicityAdapter);
        rvEthnicity.setNestedScrollingEnabled(true);
    }

    //TODO: Item selection click
    @Override
    public void onItemClick(int position, String value, int outerpos) {

    }

    //TODO: Autoscroll method
    public void autoScroll() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (currentPosition == myViewPagerAdapter.getCount() - 1) {
                            currentPosition = 0;
                        } else {
                            currentPosition = currentPosition + 1;
                        }
                        vpProfileImages.setCurrentItem(currentPosition);
                        addBottomDots(currentPosition);
                        autoScroll();
                    }
                }, 1000);

            }
        }, 1000);
    }

    //TODO: set adapter view for images
    private void setView() {

        addBottomDots(0);
        myViewPagerAdapter = new MyViewPagerAdapter();
        vpProfileImages.setAdapter(myViewPagerAdapter);

        vpProfileImages.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //TODO : Add Bottoms Dots
    private void addBottomDots(int currentPage) {
        llDots.removeAllViews();
        TextView[] dots = new TextView[listImages.size()];

        if (dots.length <= 1) {
            return;
        }
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.transparent_white));
            llDots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(getResources().getColor(R.color.white));
    }

    //TODO: Add pager adapter for facebook images
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.item_friend_image, container, false);
            ImageView imageView = view.findViewById(R.id.ivImage);
            loader.setVisibility(View.VISIBLE);
            String strUrl = new URLS().UPLOAD_URL + listImages.get(position);
            Picasso.with(FriendProfileActivity.this)
                    .load(strUrl)
                    .error(R.drawable.com_facebook_profile_picture_blank_portrait)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            loader.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {

                        }
                    });

            container.addView(view);

            return view;
        }

        @Override
        public int getCount() {
            return listImages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    //TODO : Get user Detail
    public void getUserDetail() {
        try {


            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
                params.put("userid", friendId);

            Debug.e("getUserDetail", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().GET_USER_DETAIL, params, new ResponseHandler(this, this, "get_user_detail"));
        } catch (Exception e) {
            Debug.e("getUserDetail Exception", e.getMessage());
        }

    }

    //TODO : Get Default Data
    public void getDefaultData() {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("language", getPreferences().getString(RequestParamUtils.LANGUAGE, "en"));
            Debug.e("getDefaultData", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().GET_ALL_STATIC, params, new ResponseHandler(this, this, "default_data"));
        } catch (Exception e) {
            Debug.e("getDefaultData Exception", e.getMessage());
        }

    }

    //TODO : get Instagram Images
    public void getInstagramImages() {

        try {


            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("friendid", userDetailRider.body.id);
            params.put("userid", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            Debug.e("getInstagramImages", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().GET_INSTAGRAM_IMAGES, params, new ResponseHandler(this, this, "instagram_images"));
        } catch (Exception e) {
            Debug.e("getInstagramImages Exception", e.getMessage());
        }

    }


    //TODO: response of api
    @Override
    public void onResponse(String response, String methodName) {


        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("get_user_detail")) {

            //get user data
            Log.e("response", response);

            userDetailRider = new Gson().fromJson(
                    response, new TypeToken<UserDetail>() {
                    }.getType());

            getInstagramImages();
            setData();
            dismissProgress();

//            setUserData(userDetailRider);
//            getInstagramImages();

        } else if (methodName.equals("default_data")) {

            //get all static data

            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONArray ethnicity = jsonObject.getJSONArray("ethnicity");
                for (int i = 0; i < ethnicity.length(); i++) {
                    listEthnicity.add(ethnicity.getJSONObject(i));
                }

                JSONArray religion = jsonObject.getJSONArray("religion");
                for (int i = 0; i < religion.length(); i++) {
                    listReligion.add(religion.getJSONObject(i));
                }

                JSONArray question = jsonObject.getJSONArray("question");
                for (int i = 0; i < question.length(); i++) {
                    listQuestion.add(question.getJSONObject(i));
                }

                getUserDetail();

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("instagram_images")) {

            final InstagramUserImage instagramUserImageRider = new Gson().fromJson(
                    response, new TypeToken<InstagramUserImage>() {
                    }.getType());

            if (!instagramUserImageRider.error) {
                if (instagramUserImageRider.instaImages != null) {
                    llInstaConnected.setVisibility(View.VISIBLE);
                    llInstanotConnected.setVisibility(View.GONE);
                    instaUserImageAdapter.addAll(instagramUserImageRider.instaImages);
                } else {
                    llInstaConnected.setVisibility(View.GONE);
                    llInstanotConnected.setVisibility(View.VISIBLE);
                }
            } else {
                llInstaConnected.setVisibility(View.GONE);
                llInstanotConnected.setVisibility(View.VISIBLE);
            }

        }
    }

    //TODO: set detail of user's
    public void setData() {

        if (userDetailRider.body.gallary.img1 != null) {
            listImages.add(userDetailRider.body.gallary.img1);
        }
        if (userDetailRider.body.gallary.img2 != null) {
            listImages.add(userDetailRider.body.gallary.img2);
        }
        if (userDetailRider.body.gallary.img3 != null) {
            listImages.add(userDetailRider.body.gallary.img3);
        }
        if (userDetailRider.body.gallary.img4 != null) {
            listImages.add(userDetailRider.body.gallary.img4);
        }
        if (userDetailRider.body.gallary.img5 != null) {
            listImages.add(userDetailRider.body.gallary.img5);
        }
        if (userDetailRider.body.gallary.img6 != null) {
            listImages.add(userDetailRider.body.gallary.img6);
        }
        setView();

        for (int i = 0; i < listQuestion.size(); i++) {
            try {
                String str = listQuestion.get(i).getString("id");
                if (userDetailRider.body.queId.equals(str)) {
                    tvQuote.setText(listQuestion.get(i).getString(getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
                    break;
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }

        tvQuoteAns.setText(userDetailRider.body.queAns);
        tvAboutMe.setText(userDetailRider.body.about);
        friendprofilename.setText(userDetailRider.body.fname + " " + userDetailRider.body.lname);
        tvAge.setText(userDetailRider.body.age + "");
        if (tvAge.getText().toString().equals("")) {
            tvAge.setVisibility(View.GONE);
            ivAge.setVisibility(View.GONE);
        }
        if (userDetailRider.body.height != null) {
            tvHeight.setText(userDetailRider.body.height);
        } else {
            tvHeight.setVisibility(View.GONE);
            ivHeight.setVisibility(View.GONE);
        }

        if (userDetailRider.body.education != null) {
            tvGraduate.setText(userDetailRider.body.education);
        } else {
            tvGraduate.setVisibility(View.GONE);
            ivGraduate.setVisibility(View.GONE);
        }
        if (userDetailRider.body.profession != null) {
            tvProfession.setText(userDetailRider.body.profession);
        } else {
            tvProfession.setVisibility(View.GONE);
            ivProfession.setVisibility(View.GONE);
        }
        if (userDetailRider.body.kids != null) {
            if (userDetailRider.body.kids.equals("8")) {
                //none
                tvBaby.setText(getResources().getString(R.string.none));
            } else if (userDetailRider.body.kids.equals("9")) {
                //one day
                tvBaby.setText(getResources().getString(R.string.one_day));
            } else if (userDetailRider.body.kids.equals("10")) {
                //I dont want kids
                tvBaby.setText(getResources().getString(R.string.i_dont_want_kids));
            } else {
                tvBaby.setText(userDetailRider.body.kids);
            }
        } else {
            tvBaby.setVisibility(View.GONE);
            ivBaby.setVisibility(View.GONE);
        }


        if (!userDetailRider.body.ethnicity.equals("0")) {

            List<JSONObject> listSelectedEthnicity = new ArrayList<>();
            List<String> listData = Arrays.asList(userDetailRider.body.ethnicity.split("\\s*,\\s*"));
            for (int i = 0; i < listEthnicity.size(); i++) {
                for (int j = 0; j < listData.size(); j++) {
                    try {
                        if (listEthnicity.get(i).getString("id").equals(listData.get(j))) {
                            listSelectedEthnicity.add(listEthnicity.get(i));
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }
            }
            showEthnicityAdapter.addAll(listSelectedEthnicity);
        } else {
            llEthnicity.setVisibility(View.GONE);
        }


        if (!userDetailRider.body.religion.equals("0")) {
            List<JSONObject> listSelectedReligion = new ArrayList<>();
            List<String> listData2 = Arrays.asList(userDetailRider.body.religion.split("\\s*,\\s*"));
            for (int i = 0; i < listReligion.size(); i++) {
                for (int j = 0; j < listData2.size(); j++) {
                    try {
                        if (listReligion.get(i).getString("id").equals(listData2.get(j))) {
                            listSelectedReligion.add(listReligion.get(i));
                        }
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }
            }
            showReligionAdapter.addAll(listSelectedReligion);
        } else {
            llReligion.setVisibility(View.GONE);
        }
    }

    //TODO: set destroy method
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
