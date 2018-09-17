package com.example.cupidlove.activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;
import com.example.cupidlove.adapter.EthnicityAdapter;
import com.example.cupidlove.adapter.ReligionAdapter;
import com.example.cupidlove.customview.seekbar.RangeSeekBar;
import com.example.cupidlove.interfaces.OnItemClickListner;

import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

import com.example.cupidlove.utils.BaseSignUpActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DiscoveryPreferencesActivity extends BaseSignUpActivity implements OnItemClickListner, ResponseListener {

    //TODO: Bind The All XML View With Java File
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

    @BindView(R.id.rvEthnicity)
    RecyclerView rvEthnicity;

    @BindView(R.id.rbReligionNotToSay)
    RadioButton rbReligionNotToSay;

    @BindView(R.id.rbEthnicityNotToSay)
    RadioButton rbEthnicityNotToSay;

    //TODO: Variable Declaration
    String strMinAge, strMaxAge;
    String strMinDistance, strMaxDistance;
    List<String> listSelectedEthnicity = new ArrayList<>();
    List<String> listSelectedReligion = new ArrayList<>();

    List<JSONObject> listEthnicity = new ArrayList<>();
    List<JSONObject> listReligion = new ArrayList<>();


    ReligionAdapter religionAdapter;
    EthnicityAdapter ethnicityAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discovery_preferences);
        ButterKnife.bind(this);
        settvTitle(getResources().getString(R.string.discovery_preferance));
        cbMale.setChecked(true);
        setScreenLayoutDirection();
        cbFemale.setChecked(false);
        setRange();
        hideSearch();
        hideMenu();
        setGenderPreference();
        setReligionAdapter();
        setEthnicityAdapter();
        getDefaultData();
    }

    //TODO: Update The User Detail When User Click On Done Button
    @OnClick(R.id.tvDone)
    public void tvDoneClick() {

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

    //TODO: Onclick Of Ethinicity  Prefer Not To Say
    @OnClick(R.id.rbEthnicityNotToSay)
    public void rbEthnicityNotToSayClick() {
        listSelectedEthnicity.clear();
        ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);
    }

    //TODO: Onclick of Religion Prefer Not To Say
    @OnClick(R.id.rbReligionNotToSay)
    public void rbReligionNotToSayClick() {
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


    //TODO: Set Gender Preferance
    public void setGenderPreference() {

    }

    //TODO: Set The RAnge Of The User Distance
    private void setRange() {
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

        //TODO: Set The RAnge Of The Age
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


    //TODO: Update The User  Detail When Click On Done Button
    public void userPrefencesUpdate() {

        try {
            showProgress("");

            SharedPreferences.Editor pre = getPreferences().edit();

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("about", getPreferences().getString(RequestParamUtils.ABOUT, ""));
            params.put("date_pref", getPreferences().getString(RequestParamUtils.DATE_PREF, ""));
            params.put("ethnicity", getPreferences().getString(RequestParamUtils.ETHNICITY, ""));
            params.put("height", getPreferences().getString(RequestParamUtils.HEIGHT, ""));
            params.put("kids", getPreferences().getString(RequestParamUtils.KIDS, ""));
            params.put("que_ans", getPreferences().getString(RequestParamUtils.QUE_ANS, ""));
            params.put("que_id", getPreferences().getString(RequestParamUtils.QUE_ID, ""));
            params.put("religion", getPreferences().getString(RequestParamUtils.RELIGION, ""));

            if (cbMale.isChecked()) {
                params.put("gender_pref", "male");
            } else if (cbFemale.isChecked()) {
                params.put("gender_pref", "female");
            }
            params.put("max_age_pref", rangebarAge.getSelectedMaxValue() + "");
            params.put("max_dist_pref", rangebarDistance.getSelectedMaxValue() + "");
            params.put("min_age_pref", rangebarAge.getSelectedMinValue() + "");
            params.put("min_dist_pref", rangebarDistance.getSelectedMinValue() + "");
            String strEthnicity = "";
            if (rbEthnicityNotToSay.isChecked()) {
                params.put("ethnicity_pref", "0");
                pre.putString(RequestParamUtils.ETHNICITY, "0");
            } else {
                for (int i = 0; i < listSelectedEthnicity.size(); i++) {
                    if (i == 0) {
                        strEthnicity = listSelectedEthnicity.get(i);
                    } else {
                        strEthnicity = strEthnicity + "," + listSelectedEthnicity.get(i);
                    }
                }
                params.put("ethnicity_pref", strEthnicity);
                pre.putString(RequestParamUtils.ETHNICITY, strEthnicity);
            }
            String strReligion = "";
            if (rbReligionNotToSay.isChecked()) {
                params.put("religion_pref", "0");
                pre.putString(RequestParamUtils.RELIGION, "0");
            } else {
                for (int i = 0; i < listSelectedReligion.size(); i++) {
                    if (i == 0) {
                        strReligion = listSelectedReligion.get(i);
                    } else {
                        strReligion = strReligion + "," + listSelectedReligion.get(i);
                    }
                }
                params.put("religion_pref", strReligion);
                pre.putString(RequestParamUtils.RELIGION, strReligion);
            }
            pre.commit();

            Debug.e("userPrefencesUpdate", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().USER_PREFERENCE_UPDATE, params, new ResponseHandler(this, this, "userPrefencesUpdate"));
        } catch (Exception e) {
            Debug.e("userPrefencesUpdate Exception", e.getMessage());
        }

    }

    //TODO: Set Response When Detail Update
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

                    Toast.makeText(this, getResources().getString(R.string.info_updated_successfully), Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor pre = getPreferences().edit();
                    pre.putString(RequestParamUtils.REGISTER, "");
                    pre.putString(RequestParamUtils.FIRST_VISIT, "");
                    pre.commit();


                    Intent intent = new Intent(this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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


            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

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

        } else if (value.equals("ethnicity")) {
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
}