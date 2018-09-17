package com.example.cupidlove.fragment;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.adapter.EthnicityAdapter;
import com.example.cupidlove.adapter.ReligionAdapter;
import com.example.cupidlove.customview.edittext.EditTextRegular;
import com.example.cupidlove.customview.seekbar.RangeSeekBar;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.UserDetail;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Kaushal on 29-12-2017.
 */

public class SettingFragment extends Fragment implements OnItemClickListner, ResponseListener, View.OnTouchListener, View.OnDragListener, View.OnClickListener {

    //TODO : VAriable Declaration

    View view;
    LayoutInflater inflater;
    ViewGroup container;

    //TODO: Bind The all XML view with JAVA File
    @BindView(R.id.civOne)
    CircleImageView civOne;

    @BindView(R.id.civTwo)
    CircleImageView civTwo;

    @BindView(R.id.civThree)
    CircleImageView civThree;

    @BindView(R.id.civFour)
    CircleImageView civFour;

    @BindView(R.id.ivRightQuote)
    ImageView ivRightQuote;

    @BindView(R.id.ivLeftQuote)
    ImageView ivLeftQuote;

    @BindView(R.id.tvOne)
    TextViewRegular tvOne;

    @BindView(R.id.etQuote)
    EditTextRegular etQuote;

    @BindView(R.id.tvTwo)
    TextViewRegular tvTwo;

    @BindView(R.id.tvThree)
    TextViewRegular tvThree;

    @BindView(R.id.tvFour)
    TextViewRegular tvFour;

    @BindView(R.id.rangebarAge)
    RangeSeekBar rangebarAge;

    @BindView(R.id.rangebarDistance)
    RangeSeekBar rangebarDistance;

    @BindView(R.id.ivMale)
    ImageView ivMale;

    @BindView(R.id.ivFemale)
    ImageView ivFemale;

    @BindView(R.id.cbMale)
    CheckBox cbMale;

    @BindView(R.id.cbFemale)
    CheckBox cbFemale;

    @BindView(R.id.rvReligion)
    RecyclerView rvReligion;

    @BindView(R.id.rbReligionNotToSay)
    RadioButton rbReligionNotToSay;

    @BindView(R.id.rbEthnicityNotToSay)
    RadioButton rbEthnicityNotToSay;

    @BindView(R.id.rvEthnicity)
    RecyclerView rvEthnicity;

    @BindView(R.id.tvQuestion)
    TextViewRegular tvQuestion;

    //TODO : VAriable Declaration

    int selectedQuestion = 0;
    String selectedQuestionId = "0";
    List<JSONObject> listQuestion = new ArrayList<>();
    ReligionAdapter religionAdapter;
    EthnicityAdapter ethnicityAdapter;
    String strMinAge, strMaxAge;
    String strMinDistance, strMaxDistance;
    List<String> listSelectedEthnicity = new ArrayList<>();
    List<String> listSelectedReligion = new ArrayList<>();

    List<JSONObject> listEthnicity = new ArrayList<>();
    List<JSONObject> listReligion = new ArrayList<>();
    LinearLayoutManager mLayoutManagerKids;

    int selectedPosition = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_setting, container, false);

        ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).settvTitle(getResources().getString(R.string.my_preferance));
        ((HomeActivity) getActivity()).showOrHideAdview();

        ((BaseActivity) getActivity()).hideSearch();

        Constant.selectedImages = new ArrayList<>();

        getDefaultData();

        this.inflater = inflater;
        this.container = container;
        setPreferencesView();
        setDistanceAgeRangeSlider();
        setEthnicityAdapter();

        setReligionAdapter();
        setTouchListener();

        return view;
    }

    //TODO : Click Next Button
    @OnClick(R.id.tvNext)
    public void tvNextClick() {
        if (etQuote.getText().toString().length() > 100) {
            Toast.makeText(getActivity(), getResources().getString(R.string.answer_long), Toast.LENGTH_SHORT).show();
        } else if (etQuote.getText().toString().length() ==0) {
            Toast.makeText(getActivity(), getResources().getString(R.string.quotes), Toast.LENGTH_SHORT).show();
        } else {
            userPrefencesUpdate();
        }
    }

    //TODO : Click Ethinicity Not To Say Radio Button
    @OnClick(R.id.rbEthnicityNotToSay)
    public void rbEthnicityNotToSayClick() {
        rbEthnicityNotToSay.setChecked(true);
        listSelectedEthnicity.clear();
        ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);
    }

    //TODO : Click Religion Not To Say Radio Button
    @OnClick(R.id.rbReligionNotToSay)
    public void rbReligionNotToSayClick() {
        rbReligionNotToSay.setChecked(true);
        listSelectedReligion.clear();
        religionAdapter.addAll(listReligion, listSelectedReligion);
    }

    //TODO: male image click
    @OnClick(R.id.ivMale)
    public void ivMaleClick() {
        if (cbMale.isChecked()) {
            cbMale.setChecked(false);
            cbFemale.setChecked(true);
        } else {
            cbMale.setChecked(true);
            cbFemale.setChecked(false);
        }

    }

    //TODO: Female image click
    @OnClick(R.id.ivFemale)
    public void ivFemaleClick() {
        if (cbFemale.isChecked()) {
            cbMale.setChecked(true);
            cbFemale.setChecked(false);
        } else {
            cbFemale.setChecked(true);
            cbMale.setChecked(false);
        }

    }

    //TODO: checkbox male click
    @OnCheckedChanged(R.id.cbMale)
    public void cbmaleonCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (!isChecked) {
            cbMale.setChecked(false);
            cbFemale.setChecked(true);
        } else {
            cbMale.setChecked(true);
            cbFemale.setChecked(false);
        }

    }

    //TODO: checkbox female click
    @OnCheckedChanged(R.id.cbFemale)
    public void cbFemaleonCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            cbMale.setChecked(true);
            cbFemale.setChecked(false);
        } else {
            cbFemale.setChecked(true);
            cbMale.setChecked(false);
        }
    }


    //TODO: image touch listner
    public void setTouchListener() {
        civOne.setOnTouchListener(this);
        civTwo.setOnTouchListener(this);
        civThree.setOnTouchListener(this);
        civFour.setOnTouchListener(this);


        civOne.setOnDragListener(this);
        civTwo.setOnDragListener(this);
        civThree.setOnDragListener(this);
        civFour.setOnDragListener(this);
    }

//    public void setImageView() {
//        final LinearLayout.LayoutParams paramsImagePhoto = (LinearLayout.LayoutParams) llPhotoSelection.getLayoutParams();
//        llPhotoSelection.post(new Runnable() {
//            @Override
//            public void run() {
//                paramsImagePhoto.height = llPhotoSelection.getWidth();
//                llPhotoSelection.setLayoutParams(paramsImagePhoto);
//            }
//        });
//    }

    public void setEthnicityAdapter() {
        ethnicityAdapter = new EthnicityAdapter(getActivity(), this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvEthnicity.setLayoutManager(mLayoutManager);
        rvEthnicity.setAdapter(ethnicityAdapter);
        rvEthnicity.setNestedScrollingEnabled(false);
    }

    //TODO: Set religion adapter
    public void setReligionAdapter() {
        religionAdapter = new ReligionAdapter(getActivity(), this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvReligion.setLayoutManager(mLayoutManager);
        rvReligion.setAdapter(religionAdapter);
        rvReligion.setNestedScrollingEnabled(false);
    }

    //TODO: item click of religion
    @Override
    public void onItemClick(int position, String value, int outerPos) {
        if (value.equals("religion")) {
            //religion clicked
            rbReligionNotToSay.setChecked(false);
            try {
                if (outerPos == 0) {
                    //remove selected
                    listSelectedReligion.remove(listReligion.get(position).getString("id"));
                } else if (outerPos == 1) {
                    //add selected
                    listSelectedReligion.add(listReligion.get(position).getString("id"));
                }
                religionAdapter.addAll(listReligion, listSelectedReligion);
            } catch (Exception e) {
                Log.e("error", e.getMessage() + "");
            }

        }else if (value.equals("ethnicity")) {
            //ethnicity clicked

            rbEthnicityNotToSay.setChecked(false);
            try {
                if (outerPos == 0) {
                    //remove selected
                    listSelectedEthnicity.remove(listEthnicity.get(position).getString("id"));
                } else if (outerPos == 1) {
                    //add selected
                    listSelectedEthnicity.add(listEthnicity.get(position).getString("id"));
                }
                ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);
            } catch (Exception e) {
                Log.e("error", e.getMessage() + "");
            }
        }

    }

    //TODO: left arrow click
    @OnClick(R.id.ivLeftQuote)
    public void ivLeftQuoteClick() {
        etQuote.setText("");
        if (selectedQuestion != 0) {
            try {
                selectedQuestion--;
                String str = listQuestion.get(selectedQuestion).getString("id");
                selectedQuestionId = str;
                tvQuestion.setText(listQuestion.get(selectedQuestion).getString(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
                if (selectedQuestion != listQuestion.size() - 1) {
                    ivRightQuote.setImageResource(R.drawable.ic_arrow_right_orange);
                }
                if (selectedQuestion == 0) {
                    ivLeftQuote.setImageResource(R.drawable.ic_arrow_left);
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else {
            ivLeftQuote.setImageResource(R.drawable.ic_arrow_left);
        }
    }

    //TODO: Right arrow click
    @OnClick(R.id.ivRightQuote)
    public void ivRightQuoteClick() {
        etQuote.setText("");
        if (selectedQuestion != listQuestion.size() - 1) {
            try {
                selectedQuestion++;
                String str = listQuestion.get(selectedQuestion).getString("id");
                selectedQuestionId = str;
                tvQuestion.setText(listQuestion.get(selectedQuestion).getString(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
                if (selectedQuestion == listQuestion.size() - 1) {
                    ivRightQuote.setImageResource(R.drawable.ic_arrow_right);
                }
                if (selectedQuestion != 0) {
                    ivLeftQuote.setImageResource(R.drawable.ic_arrow_left_orange);
                }

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else {
            ivRightQuote.setImageResource(R.drawable.ic_arrow_right);
        }
    }

    //TODO: set Preferance view
    public void setPreferencesView() {
        final LinearLayout.LayoutParams paramsImagePhoto = (LinearLayout.LayoutParams) civOne.getLayoutParams();
        civOne.post(new Runnable() {
            @Override
            public void run() {
                paramsImagePhoto.height = civOne.getWidth();
                civOne.setLayoutParams(paramsImagePhoto);
            }
        });
    }

    //TODO: set distance range
    private void setDistanceAgeRangeSlider() {

        if ((int) rangebarDistance.getAbsoluteMaxValue() == 201) {
            rangebarDistance.setSelectedMaxValue(200);
        }
        if ((int) rangebarAge.getAbsoluteMinValue() == 17) {
            rangebarAge.setSelectedMinValue(18);
        }
        if ((int) rangebarAge.getAbsoluteMaxValue() == 61) {
            rangebarAge.setSelectedMaxValue(60);
        }
        rangebarDistance.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {

                if ((int) maxValue == 200 || (int) maxValue == 201) {
                    rangebarDistance.setSelectedMaxValue(200);
                }
                strMaxDistance = String.valueOf(maxValue);
                strMinDistance = String.valueOf(minValue);
                Log.e("strInstrested", strMaxDistance + " distance  " + strMinDistance);
            }
        });

        rangebarAge.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {

            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Number minValue, Number maxValue) {

                if ((int) minValue == 17 || (int) minValue == 18) {
                    rangebarAge.setSelectedMinValue(18);
                }
                if ((int) maxValue == 61 || (int) maxValue == 60) {
                    rangebarAge.setSelectedMaxValue(60);
                }
                strMaxAge = String.valueOf(maxValue);
                strMinAge = String.valueOf(minValue);
                Log.e("strInstrested", strMaxAge + " Age  " + strMinAge);
            }
        });
        rangebarAge.setNotifyWhileDragging(true);
        rangebarDistance.setNotifyWhileDragging(true);
    }

    //TODO : Set touch listener
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onClick(View v) {

    }

    //TODO: Drage view
    @Override
    public boolean onDrag(final View targetView, DragEvent event) {

        View view = (View) event.getLocalState();
        int action = event.getAction();
        switch (action) {
            case DragEvent.ACTION_DRAG_STARTED:
                break;
            case DragEvent.ACTION_DRAG_ENTERED:
                break;
            case DragEvent.ACTION_DRAG_EXITED:
                break;
            case DragEvent.ACTION_DROP:

                CircleImageView target = (CircleImageView) targetView;
                CircleImageView dragged = (CircleImageView) event.getLocalState();

                boolean swipeText = true;

                Log.i("Target id", "" + target.getId());
                if (swipeText) {

                    Drawable target_draw = target.getDrawable();
                    Drawable dragged_draw = dragged.getDrawable();

                    if (dragged.getId() == civOne.getId()) {
                        Log.e("Coffee Target  ", "Called");

                        if (target.getId() == civTwo.getId()) {
                            setDraggedImagew(tvTwo, tvOne, civTwo);

                        } else if (target.getId() == civThree.getId()) {
                            setDraggedImagew(tvThree, tvOne, civThree);

                        } else if (target.getId() == civFour.getId()) {
                            setDraggedImagew(tvFour, tvOne, civFour);

                        }
                        dragged.setBackgroundResource(0);
                        setDraggedView(tvOne, civOne);

                    } else if (dragged.getId() == civTwo.getId()) {
                        Log.e("Coffee Target  ", "Called");

                        if (target.getId() == civOne.getId()) {
                            setDraggedImagew(tvOne, tvTwo, civOne);

                        } else if (target.getId() == civThree.getId()) {
                            setDraggedImagew(tvThree, tvTwo, civThree);

                        } else if (target.getId() == civFour.getId()) {
                            setDraggedImagew(tvFour, tvTwo, civFour);

                        }
                        dragged.setBackgroundResource(0);
                        setDraggedView(tvTwo, civTwo);

                    } else if (dragged.getId() == civThree.getId()) {
                        Log.e("Coffee Target  ", "Called");

                        if (target.getId() == civOne.getId()) {
                            setDraggedImagew(tvOne, tvThree, civOne);

                        } else if (target.getId() == civTwo.getId()) {
                            setDraggedImagew(tvTwo, tvThree, civTwo);

                        } else if (target.getId() == civFour.getId()) {
                            setDraggedImagew(tvFour, tvThree, civFour);

                        }
                        dragged.setBackgroundResource(0);
                        setDraggedView(tvThree, civThree);

                    } else if (dragged.getId() == civFour.getId()) {
                        Log.e("Coffee Target  ", "Called");

                        if (target.getId() == civOne.getId()) {
                            setDraggedImagew(tvOne, tvFour, civOne);

                        } else if (target.getId() == civTwo.getId()) {
                            setDraggedImagew(tvTwo, tvFour, civTwo);

                        } else if (target.getId() == civThree.getId()) {
                            setDraggedImagew(tvThree, tvFour, civThree);

                        }
                        dragged.setBackgroundResource(0);
                        setDraggedView(tvFour, civFour);

                    }
                }
                view.setVisibility(View.VISIBLE);

                break;

            case DragEvent.ACTION_DRAG_ENDED:
                Log.e(null, "Drag ended");
                //Toast.makeText(this, "end draged", Toast.LENGTH_SHORT).show();
                view.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return true;
    }

    //TODO: Set Dragged view of image
    public void setDraggedView(TextViewRegular tv, CircleImageView circleImageView) {
        if (tv.getText().toString().equals(getResources().getString(R.string.coffee))) {
            circleImageView.setBackgroundResource(R.drawable.ic_coffee);
        } else if (tv.getText().toString().equals(getResources().getString(R.string.drink))) {
            circleImageView.setBackgroundResource(R.drawable.ic_drink);
        } else if (tv.getText().toString().equals(getResources().getString(R.string.food))) {
            circleImageView.setBackgroundResource(R.drawable.ic_food);
        } else if (tv.getText().toString().equals(getResources().getString(R.string.fun))) {
            circleImageView.setBackgroundResource(R.drawable.ic_fun);
        }
    }

    //TODO: Set text of draged view
    public void setDraggedImagew(TextViewRegular tv, TextViewRegular tv2, CircleImageView circleImageView) {

        String str = tv2.getText().toString();
        tv2.setText(tv.getText().toString());
        tv.setText(str);

        if (tv.getText().toString().equals(getResources().getString(R.string.coffee))) {
            circleImageView.setBackgroundResource(R.drawable.ic_coffee);

        } else if (tv.getText().toString().equals(getResources().getString(R.string.drink))) {
            circleImageView.setBackgroundResource(R.drawable.ic_drink);

        } else if (tv.getText().toString().equals(getResources().getString(R.string.food))) {
            circleImageView.setBackgroundResource(R.drawable.ic_food);

        } else if (tv.getText().toString().equals(getResources().getString(R.string.fun))) {
            circleImageView.setBackgroundResource(R.drawable.ic_fun);

        }
    }

    //TODO: Get default language
    public void getDefaultData() {

        try {
            RequestParams params = new RequestParams();
            params.put("language", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LANGUAGE, "en"));

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            ((BaseActivity) getActivity()).showProgress("");
            asyncHttpClient.post(new URLS().GET_ALL_STATIC, params, new ResponseHandler(getActivity(), this, "default_data"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }

    }

    //TODO: Get User Detail
    public void getUserDetail() {

        try {
            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("userid", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("getUserDetail", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            ((BaseActivity) getActivity()).showProgress("");
            asyncHttpClient.post(new URLS().GET_USER_DETAIL, params, new ResponseHandler(getActivity(), this, "get_user_detail"));
        } catch (Exception e) {
            Debug.e("getUserDetail Exception", e.getMessage());
        }

    }

    //TODO: Update user preferance
    public void userPrefencesUpdate() {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            int one = 1, two = 2, three = 3, four = 4;
            if (tvOne.getText().toString().equals(getResources().getString(R.string.coffee))) {
                one = 1;
            } else if (tvOne.getText().toString().equals(getResources().getString(R.string.drink))) {
                one = 2;
            } else if (tvOne.getText().toString().equals(getResources().getString(R.string.food))) {
                one = 3;
            } else if (tvOne.getText().toString().equals(getResources().getString(R.string.fun))) {
                one = 4;
            }
            if (tvTwo.getText().toString().equals(getResources().getString(R.string.coffee))) {
                two = 1;
            } else if (tvTwo.getText().toString().equals(getResources().getString(R.string.drink))) {
                two = 2;
            } else if (tvTwo.getText().toString().equals(getResources().getString(R.string.food))) {
                two = 3;
            } else if (tvTwo.getText().toString().equals(getResources().getString(R.string.fun))) {
                two = 4;
            }
            if (tvThree.getText().toString().equals(getResources().getString(R.string.coffee))) {
                three = 1;
            } else if (tvThree.getText().toString().equals(getResources().getString(R.string.drink))) {
                three = 2;
            } else if (tvThree.getText().toString().equals(getResources().getString(R.string.food))) {
                three = 3;
            } else if (tvThree.getText().toString().equals(getResources().getString(R.string.fun))) {
                three = 4;
            }
            if (tvFour.getText().toString().equals(getResources().getString(R.string.coffee))) {
                four = 1;
            } else if (tvFour.getText().toString().equals(getResources().getString(R.string.drink))) {
                four = 2;
            } else if (tvFour.getText().toString().equals(getResources().getString(R.string.food))) {
                four = 3;
            } else if (tvFour.getText().toString().equals(getResources().getString(R.string.fun))) {
                four = 4;
            }
            String str = one + "," + two + "," + three + "," + four;

            params.put("date_pref", str);

            SharedPreferences.Editor pre = ((BaseActivity) getActivity()).getPreferences().edit();
            pre.putString(RequestParamUtils.DATE_PREF, str);
            pre.commit();

            if (cbMale.isChecked()) {
                params.put("gender_pref", "male");
            } else if (cbFemale.isChecked()) {
                params.put("gender_pref", "female");
            }

            params.put("max_age_pref", rangebarAge.getSelectedMaxValue());
            params.put("min_age_pref", rangebarAge.getSelectedMinValue());
            params.put("max_dist_pref", rangebarDistance.getSelectedMaxValue());
            params.put("min_dist_pref", rangebarDistance.getSelectedMinValue());
            params.put("que_ans", etQuote.getText().toString());
            params.put("que_id", selectedQuestionId);

            String strEthnicity = "";
            if (rbEthnicityNotToSay.isChecked()) {
                params.put("ethnicity_pref", "0");
            } else {
                for (int i = 0; i < listSelectedEthnicity.size(); i++) {
                    if (i == 0) {
                        strEthnicity = listSelectedEthnicity.get(i);
                    } else {
                        strEthnicity = strEthnicity + "," + listSelectedEthnicity.get(i);
                    }
                }
                params.put("ethnicity_pref", strEthnicity);
            }

            String strReligion = "";
            if (rbReligionNotToSay.isChecked()) {
                params.put("religion_pref", "0");
            } else {
                for (int i = 0; i < listSelectedReligion.size(); i++) {
                    if (i == 0) {
                        strReligion = listSelectedReligion.get(i);
                    } else {
                        strReligion = strReligion + "," + listSelectedReligion.get(i);
                    }
                }
                params.put("religion_pref", strReligion);
            }

            Debug.e("userPrefencesUpdate", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().USER_PREFERENCE_UPDATE, params, new ResponseHandler(getActivity(), this, "userPrefencesUpdate"));
        } catch (Exception e) {
            Debug.e("userPrefencesUpdate Exception", e.getMessage());
        }

    }

    //TODO: response of all api call
    @Override
    public void onResponse(String response, String methodName) {


        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("default_data")) {
            //get all static data

            try {
                JSONObject jsonObject = new JSONObject(response);

                JSONArray ethnicity = jsonObject.getJSONArray("ethnicity");
                for (int i = 0; i < ethnicity.length(); i++) {
                    listEthnicity.add(ethnicity.getJSONObject(i));
                }
                ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);

                JSONArray religion = jsonObject.getJSONArray("religion");
                for (int i = 0; i < religion.length(); i++) {
                    listReligion.add(religion.getJSONObject(i));
                }
                religionAdapter.addAll(listReligion, listSelectedReligion);

                JSONArray question = jsonObject.getJSONArray("question");
                for (int i = 0; i < question.length(); i++) {
                    listQuestion.add(question.getJSONObject(i));
                }
                ((BaseActivity) getActivity()).dismissProgress();
                getUserDetail();
                setQuestions();

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }

        } else if (methodName.equals("userPrefencesUpdate")) {
            //register

            Log.e(methodName + " Response is ", response);
            try {

                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {

                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();


                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable t) {
                Debug.e("error", response);
            }
            ((BaseActivity) getActivity()).dismissProgress();
        } else if (methodName.equals("get_user_detail")) {
            //get user data
            Log.e("response", response);

            final UserDetail userDetailRider = new Gson().fromJson(
                    response, new TypeToken<UserDetail>() {
                    }.getType());

            setUserData(userDetailRider);
            ((BaseActivity) getActivity()).dismissProgress();
        }
    }

    //TODO: Set user data
    public void setUserData(UserDetail userDetailRider) {
        SharedPreferences.Editor pre = ((BaseActivity) getActivity()).getPreferences().edit();
        pre.putString(RequestParamUtils.DATE_PREF, userDetailRider.body.datePref);
        pre.commit();

        List<String> items = Arrays.asList(userDetailRider.body.datePref.split("\\s*,\\s*"));

        if (items.get(0).equals("1")) {
            //Coffee
            civOne.setBackgroundResource(R.drawable.ic_coffee);
            tvOne.setText(getResources().getString(R.string.coffee));
        } else if (items.get(0).equals("2")) {
            //Drink
            civOne.setBackgroundResource(R.drawable.ic_drink);
            tvOne.setText(getResources().getString(R.string.drink));
        } else if (items.get(0).equals("3")) {
            //Food
            civOne.setBackgroundResource(R.drawable.ic_food);
            tvOne.setText(getResources().getString(R.string.food));
        } else if (items.get(0).equals("4")) {
            //Fun
            civOne.setBackgroundResource(R.drawable.ic_fun);
            tvOne.setText(getResources().getString(R.string.fun));
        }
        if (items.get(1).equals("1")) {
            //Coffee
            civTwo.setBackgroundResource(R.drawable.ic_coffee);
            tvTwo.setText(getResources().getString(R.string.coffee));
        } else if (items.get(1).equals("2")) {
            //Drink
            civTwo.setBackgroundResource(R.drawable.ic_drink);
            tvTwo.setText(getResources().getString(R.string.drink));
        } else if (items.get(1).equals("3")) {
            //Food
            civTwo.setBackgroundResource(R.drawable.ic_food);
            tvTwo.setText(getResources().getString(R.string.food));
        } else if (items.get(1).equals("4")) {
            //Fun
            civTwo.setBackgroundResource(R.drawable.ic_fun);
            tvTwo.setText(getResources().getString(R.string.fun));
        }
        if (items.get(2).equals("1")) {
            //Coffee
            civThree.setBackgroundResource(R.drawable.ic_coffee);
            tvThree.setText(getResources().getString(R.string.coffee));
        } else if (items.get(2).equals("2")) {
            //Drink
            civThree.setBackgroundResource(R.drawable.ic_drink);
            tvThree.setText(getResources().getString(R.string.drink));
        } else if (items.get(2).equals("3")) {
            //Food
            civThree.setBackgroundResource(R.drawable.ic_food);
            tvThree.setText(getResources().getString(R.string.food));
        } else if (items.get(2).equals("4")) {
            //Fun
            civThree.setBackgroundResource(R.drawable.ic_fun);
            tvThree.setText(getResources().getString(R.string.fun));
        }
        if (items.get(3).equals("1")) {
            //Coffee
            civFour.setBackgroundResource(R.drawable.ic_coffee);
            tvFour.setText(getResources().getString(R.string.coffee));
        } else if (items.get(3).equals("2")) {
            //Drink
            civFour.setBackgroundResource(R.drawable.ic_drink);
            tvFour.setText(getResources().getString(R.string.drink));
        } else if (items.get(3).equals("3")) {
            //Food
            civFour.setBackgroundResource(R.drawable.ic_food);
            tvFour.setText(getResources().getString(R.string.food));
        } else if (items.get(3).equals("4")) {
            //Fun
            civFour.setBackgroundResource(R.drawable.ic_fun);
            tvFour.setText(getResources().getString(R.string.fun));
        }
        rangebarDistance.setSelectedMaxValue(Integer.parseInt(userDetailRider.body.maxDistPref));
        rangebarDistance.setSelectedMinValue(Integer.parseInt(userDetailRider.body.minDistPref));
        rangebarAge.setSelectedMaxValue(Integer.parseInt(userDetailRider.body.maxAgePref));
        rangebarAge.setSelectedMinValue(Integer.parseInt(userDetailRider.body.minAgePref));

        if (userDetailRider.body.genderPref.toLowerCase().equals("male")) {
            cbFemale.setChecked(false);
            cbMale.setChecked(true);
        } else if (userDetailRider.body.genderPref.toLowerCase().equals("female")) {
            cbFemale.setChecked(true);
            cbMale.setChecked(false);
        }

        if (userDetailRider.body.ethnicityPref== null || userDetailRider.body.ethnicityPref.equals("0")) {
            rbEthnicityNotToSay.setChecked(true);
        } else {
            List<String> listet = Arrays.asList(userDetailRider.body.ethnicityPref.split("\\s*,\\s*"));

            for (int i = 0; i < listet.size(); i++) {
                listSelectedEthnicity.add(listet.get(i));
            }
        }

        if (userDetailRider.body.religionPref==null|| userDetailRider.body.religionPref.equals("0")) {
            rbReligionNotToSay.setChecked(true);
        } else {
            List<String> listrel = Arrays.asList(userDetailRider.body.religionPref.split("\\s*,\\s*"));
            for (int i = 0; i < listrel.size(); i++) {
                listSelectedReligion.add(listrel.get(i));
            }
        }
        ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);
        religionAdapter.addAll(listReligion, listSelectedReligion);

        etQuote.setText(userDetailRider.body.queAns);

        for (int i = 0; i < listQuestion.size(); i++) {
            try {
                String str = listQuestion.get(i).getString("id");
                if (userDetailRider.body.queId.equals(str)) {
                    selectedQuestion = i;
                    selectedQuestionId = str;
                    if (selectedQuestion == 0) {
                        ivLeftQuote.setImageResource(R.drawable.ic_arrow_left);
                    } else {
                        ivLeftQuote.setImageResource(R.drawable.ic_arrow_left_orange);
                    }
                    if (selectedQuestion == listQuestion.size() - 1) {
                        ivRightQuote.setImageResource(R.drawable.ic_arrow_right);
                    } else {
                        ivRightQuote.setImageResource(R.drawable.ic_arrow_right_orange);
                    }
                    tvQuestion.setText(listQuestion.get(selectedQuestion).getString(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
                    break;
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

    //TODO: Set Question
    public void setQuestions() {
        try {
            tvQuestion.setText(listQuestion.get(selectedQuestion).getString(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }


}
