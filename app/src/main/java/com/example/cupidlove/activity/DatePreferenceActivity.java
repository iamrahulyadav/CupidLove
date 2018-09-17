package com.example.cupidlove.activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.adapter.EthnicityAdapter;
import com.example.cupidlove.adapter.KidsAdapter;
import com.example.cupidlove.adapter.ReligionAdapter;
import com.example.cupidlove.customview.edittext.EditTextRegular;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.utils.BaseSignUpActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DatePreferenceActivity extends BaseSignUpActivity implements OnItemClickListner, ResponseListener {

    //TODO: Bind the all view of XML with JAVA file

    @BindView(R.id.npHeight)
    NumberPicker npHeight;

    @BindView(R.id.rvReligion)
    RecyclerView rvReligion;

    @BindView(R.id.rvEthnicity)
    RecyclerView rvEthnicity;

    @BindView(R.id.rvKids)
    RecyclerView rvKids;

    @BindView(R.id.radioKids)
    RadioGroup radioKids;

    @BindView(R.id.radioNone)
    RadioButton radioNone;

    @BindView(R.id.radioOneDay)
    RadioButton radioOneDay;

    @BindView(R.id.radioDontWantKids)
    RadioButton radioDontWantKids;

    @BindView(R.id.ivRightQuote)
    ImageView ivRightQuote;

    @BindView(R.id.ivLeftQuote)
    ImageView ivLeftQuote;

    @BindView(R.id.tvQuestion)
    TextViewRegular tvQuestion;

    @BindView(R.id.etQuote)
    EditTextRegular etQuote;

    @BindView(R.id.ivLeftKids)
    ImageView ivLeftKids;

    @BindView(R.id.ivRightKids)
    ImageView ivRightKids;

    //TODO: Variable Declaration

    ReligionAdapter religionAdapter;
    EthnicityAdapter ethnicityAdapter;
    KidsAdapter kidsAdapter;

    String strMinAge, strMaxAge;
    String strMinDistance, strMaxDistance;

    int selectedQuestion = 0;
    String selectedKid = "8";
    String selectedQuestionId = "0";

    List<JSONObject> listQuestion = new ArrayList<>();
    List<String> listSelectedEthnicity = new ArrayList<>();
    List<String> listSelectedReligion = new ArrayList<>();

    List<JSONObject> listEthnicity = new ArrayList<>();
    List<JSONObject> listReligion = new ArrayList<>();

    List<String> listKids = new ArrayList<>();

    LinearLayoutManager mLayoutManagerKids;

    final String[] height = {"3'0 (92 cm)", "3'1 (94 cm)", "3'2 (97 cm)", "3'3 (99 cm)", "3'4 (102 cm)", "3'5 (104 cm)",
            "3'6 (107 cm)", "3'7 (109 cm)", "3'8 (112 cm)", "3'9 (114 cm)", "3'10 (117 cm)", "3'11 (119 cm)",
            "4'0 (122 cm)", "4'1 (125 cm)", "4'2 (127 cm)", "4'3 (130 cm)", "4'4 (132 cm)", "4'5 (135 cm)",
            "4'6 (137 cm)", "4'7 (140 cm)", "4'8 (142 cm)", "4'9 (145 cm)", "4'10 (147 cm)", "4'11 (150 cm)",
            "5'0 (152 cm)", "5'1 (155 cm)", "5'2 (158 cm)", "5'3 (160 cm)", "5'4 (163 cm)", "5'5 (165 cm)",
            "5'6 (168 cm)", "5'7 (170 cm)", "5'8 (173 cm)", "5'9 (175 cm)", "5'10 (178 cm)", "5'11 (180 cm)",
            "6'0 (183 cm)", "6'1 (185 cm)", "6'2 (188 cm)", "6'3 (191 cm)", "6'4 (193 cm)", "6'5 (196 cm)",
            "6'6 (198 cm)", "6'7 (201 cm)", "6'8 (203 cm)", "6'9 (206 cm)", "6'10 (208 cm)", "6'11 (211 cm)",
            "7'0 (213 cm)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_preference);
        ButterKnife.bind(this);

        buildGoogleApiClient();

        settvTitle(getResources().getString(R.string.my_profile));
        listKids.addAll(Arrays.asList("1", "2", "3", "4", "5", "6", "7"));
        setScreenLayoutDirection();
        getDefaultData();
        hideMenu();
        hideSearch();
        setHeightPicker();
        setReligionAdapter();
        setEthnicityAdapter();
        setKidsAdapter();
    }


    //TODO: Onselect None Of Kids
    @OnClick(R.id.radioNone)
    public void radioNoneClick() {
        selectedKid = "8";
        radioNone.setChecked(true);
        kidsAdapter.addAll(listKids, selectedKid);
    }

    //TODO: Onselect One Kids
    @OnClick(R.id.radioOneDay)
    public void radioOneDayClick() {
        selectedKid = "9";
        radioOneDay.setChecked(true);
        kidsAdapter.addAll(listKids, selectedKid);
    }

    //TODO: OnSelect I Don't Want Kids
    @OnClick(R.id.radioDontWantKids)
    public void radioDontWantKidsClick() {
        selectedKid = "10";
        radioDontWantKids.setChecked(true);
        kidsAdapter.addAll(listKids, selectedKid);
    }

    //TODO: When Click Left Arrow on Kids
    @OnClick(R.id.ivLeftKids)
    public void ivLeftKidsClick() {
        ivRightKids.setImageResource(R.drawable.ic_arrow_right_orange);
        int currentFirstVisible = mLayoutManagerKids.findFirstVisibleItemPosition();
        if (currentFirstVisible == 0) {
            ivLeftKids.setImageResource(R.drawable.ic_arrow_left);
        } else {
            ivLeftKids.setImageResource(R.drawable.ic_arrow_left_orange);
            mLayoutManagerKids.scrollToPositionWithOffset(0, 0);
        }
        if (mLayoutManagerKids.findFirstVisibleItemPosition() == 0) {
            ivLeftKids.setImageResource(R.drawable.ic_arrow_left);
        } else {
            ivLeftKids.setImageResource(R.drawable.ic_arrow_left_orange);
        }
    }

    //TODO: When Click Right Arrow on kids
    @OnClick(R.id.ivRightKids)
    public void ivRightKidsClick() {
        ivRightKids.setImageResource(R.drawable.ic_arrow_left_orange);
        int currentLastVisible = mLayoutManagerKids.findLastVisibleItemPosition();
        if (currentLastVisible == rvKids.getAdapter().getItemCount()) {
            ivRightKids.setImageResource(R.drawable.ic_arrow_right);
        } else {
            ivRightKids.setImageResource(R.drawable.ic_arrow_right_orange);
            mLayoutManagerKids.scrollToPositionWithOffset(currentLastVisible++, 0);
        }
        if (mLayoutManagerKids.findLastVisibleItemPosition() == rvKids.getAdapter().getItemCount()) {
            ivRightKids.setImageResource(R.drawable.ic_arrow_right);
        } else {
            ivRightKids.setImageResource(R.drawable.ic_arrow_right_orange);
        }
    }

    //TODO: Click Left Arror On Quote
    @OnClick(R.id.ivLeftQuote)
    public void ivLeftQuoteClick() {

        if (selectedQuestion != 0) {
            try {
                selectedQuestion--;
                String str = listQuestion.get(selectedQuestion).getString("id");
                selectedQuestionId = str;
                tvQuestion.setText(listQuestion.get(selectedQuestion).getString(getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
                if (selectedQuestion != listQuestion.size() - 1) {
                    ivRightQuote.setImageResource(R.drawable.ic_arrow_right_orange);
                }
                if (selectedQuestion == 0) {
                    ivLeftQuote.setImageResource(R.drawable.ic_arrow_left);
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            etQuote.setText("");
        } else {
            ivLeftQuote.setImageResource(R.drawable.ic_arrow_left);
        }
    }

    //TODO: Click Right Arror On Quote
    @OnClick(R.id.ivRightQuote)
    public void ivRightQuoteClick() {

        if (selectedQuestion != listQuestion.size() - 1) {
            try {
                selectedQuestion++;
                String str = listQuestion.get(selectedQuestion).getString("id");
                selectedQuestionId = str;
                tvQuestion.setText(listQuestion.get(selectedQuestion).getString(getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
                if (selectedQuestion == listQuestion.size() - 1) {
                    ivRightQuote.setImageResource(R.drawable.ic_arrow_right);
                }
                if (selectedQuestion != 0) {
                    ivLeftQuote.setImageResource(R.drawable.ic_arrow_left_orange);
                }

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            etQuote.setText("");
        } else {
            ivRightQuote.setImageResource(R.drawable.ic_arrow_right);
        }
    }


    //TODO: Click On Next Button
    @OnClick(R.id.tvNext)
    public void tvNextClick() {
        if (etQuote.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.answer_of_question), Toast.LENGTH_SHORT).show();
            return;
        } else if (listSelectedReligion.size() == 0) {
            Toast.makeText(this, R.string.select_religion, Toast.LENGTH_LONG).show();
            return;
        } else if (listSelectedEthnicity.size() == 0) {
            Toast.makeText(this, R.string.select_ethnicity, Toast.LENGTH_LONG).show();
            return;
        }
        userPrefencesUpdate();

    }


    //TODO: Set Adepter when Religion selected
    public void setReligionAdapter() {
        religionAdapter = new ReligionAdapter(this, this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 3);
        rvReligion.setLayoutManager(mLayoutManager);
        rvReligion.setAdapter(religionAdapter);
        rvReligion.setNestedScrollingEnabled(false);
    }

    //TODO: Set Adepeter When Ethinicity selected
    public void setEthnicityAdapter() {
        ethnicityAdapter = new EthnicityAdapter(this, this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        rvEthnicity.setLayoutManager(mLayoutManager);
        rvEthnicity.setAdapter(ethnicityAdapter);
        rvEthnicity.setNestedScrollingEnabled(false);
    }

    //TODO: Set Adepter When Kids selected
    public void setKidsAdapter() {
        kidsAdapter = new KidsAdapter(this, this);
        mLayoutManagerKids = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvKids.setLayoutManager(mLayoutManagerKids);
        rvKids.setAdapter(kidsAdapter);
        rvKids.setNestedScrollingEnabled(true);
    }


    //TODO: Select Any Item
    @Override
    public void onItemClick(int position, String value, int outerPos) {
        if (value.equals("religion")) {
            //religion clicked
            try {
                if (outerPos == 0) {
                    //remove selected
                    listSelectedReligion.remove(listReligion.get(position).getString("id"));
                } else if (outerPos == 1) {
                    //add selected
                    if (!listSelectedReligion.isEmpty()) {
                        listSelectedReligion.clear();
                    }
                    listSelectedReligion.add(listReligion.get(position).getString("id"));
                }

                religionAdapter.addAll(listReligion, listSelectedReligion);
            } catch (Exception e) {
                Log.e("error", e.getMessage() + "");
            }

        } else if (value.equals("ethnicity")) {
            //ethnicity clicked


            try {
                if (outerPos == 0) {
                    //remove selected
                    listSelectedEthnicity.remove(listEthnicity.get(position).getString("id"));
                } else if (outerPos == 1) {
                    //add selected
                    if (!listSelectedEthnicity.isEmpty()) {
                        listSelectedEthnicity.clear();
                    }
                    listSelectedEthnicity.add(listEthnicity.get(position).getString("id"));
                }
                ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);
            } catch (Exception e) {
                Log.e("error", e.getMessage() + "");
            }
        } else if (value.equals("kids")) {
            selectedKid = listKids.get(position);
            kidsAdapter.addAll(listKids, selectedKid);
            radioNone.setChecked(false);
            radioOneDay.setChecked(false);
            radioDontWantKids.setChecked(false);
        }

    }

    //TODO: Set The Hight of The User
    public void setHeightPicker() {
        //Set TextView text color

        //Initializing a new string array with elements
        npHeight.setMinValue(0); //from array first value
        npHeight.setMaxValue(height.length - 1); //to array last value
        npHeight.setDisplayedValues(height);
        npHeight.setWrapSelectorWheel(false);

        if (Prefs.getString(RequestParamUtils.GENDER, "").equals("male")) {
            npHeight.setValue(34);
        } else {
            npHeight.setValue(25);
        }

        npHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                //Display the newly selected value from picker
//                tv.setText("Selected value : " + values[newVal]);
            }
        });
    }

    //TODO: Set The Default Value
    public void getDefaultData() {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("language", Prefs.getString(RequestParamUtils.LANGUAGE, "en"));

            Debug.e("getDefaultData", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().GET_ALL_STATIC, params, new ResponseHandler(this, this, "default_data"));
        } catch (Exception e) {
            Debug.e("getDefaultData Exception", e.getMessage());
        }

    }

    //TODO: Upadte The User Detail When User Change The Detail
    public void userPrefencesUpdate() {

        try {
            showProgress("");

            SharedPreferences.Editor pre = getPreferences().edit();

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));

            params.put("about", "About Me");
            params.put("date_pref", "1,2,3,4");
            String strEthnicity = "";

            for (int i = 0; i < listSelectedEthnicity.size(); i++) {
                if (i == 0) {
                    strEthnicity = listSelectedEthnicity.get(i);
                } else {
                    strEthnicity = strEthnicity + "," + listSelectedEthnicity.get(i);
                }
            }
            params.put("ethnicity", strEthnicity);
            pre.putString(RequestParamUtils.ETHNICITY, strEthnicity);

            params.put("gender_pref", "female");
            params.put("height", height[npHeight.getValue()] + "");
            pre.putString(RequestParamUtils.HEIGHT, height[npHeight.getValue()] + "");

            params.put("kids", selectedKid);
            pre.putString(RequestParamUtils.KIDS, selectedKid);

            params.put("max_age_pref", "60");
            params.put("min_age_pref", "18");
            params.put("max_dist_pref", "200");
            params.put("min_dist_pref", "0");
            params.put("que_ans", etQuote.getText().toString());
            pre.putString(RequestParamUtils.QUE_ANS, etQuote.getText().toString());

            params.put("que_id", selectedQuestionId);
            pre.putString(RequestParamUtils.QUE_ID, selectedQuestionId);

            String strReligion = "";

            for (int i = 0; i < listSelectedReligion.size(); i++) {
                if (i == 0) {
                    strReligion = listSelectedReligion.get(i);
                } else {
                    strReligion = strReligion + "," + listSelectedReligion.get(i);
                }
            }
            params.put("religion", strReligion);
            pre.putString(RequestParamUtils.RELIGION, strReligion);


            pre.commit();

            Debug.e("userPrefencesUpdate", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().USER_PREFERENCE_UPDATE, params, new ResponseHandler(this, this, "userPrefencesUpdate"));
        } catch (Exception e) {
            Debug.e("userPrefencesUpdate Exception", e.getMessage());
        }

    }

    //TODO : Response OF Api call
    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();

        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("userPrefencesUpdate")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done

                    SharedPreferences.Editor pre = getPreferences().edit();
                    pre.putString(RequestParamUtils.REGISTER, "uploadphoto");
                    pre.commit();

                    Toast.makeText(this, getResources().getString(R.string.info_updated_successfully), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, UploadPhotosActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
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
                setDefaultData();
                setQuestions();

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

    //TODO: Set The Default Data Kids, Ethinicity, Religion
    public void setDefaultData() {

        try {
            selectedQuestion = 0;
            selectedQuestionId = listQuestion.get(0).getString("id");
            ivLeftQuote.setImageResource(R.drawable.ic_arrow_left);
            ivRightQuote.setImageResource(R.drawable.ic_arrow_right_orange);
            tvQuestion.setText(listQuestion.get(0).getString(getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }

        ivLeftKids.setImageResource(R.drawable.ic_arrow_left);
        ivRightKids.setImageResource(R.drawable.ic_arrow_right_orange);


        ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);
        religionAdapter.addAll(listReligion, listSelectedReligion);

        //none
        radioNone.setChecked(true);
        kidsAdapter.addAll(listKids, selectedKid);
    }

    //TODO: Set The Questions
    public void setQuestions() {
        try {
            tvQuestion.setText(listQuestion.get(selectedQuestion).getString(getPreferences().getString(RequestParamUtils.LANGUAGE, "en")));
        } catch (Exception e) {
            Log.e("error", e.getMessage());
        }
    }

}
