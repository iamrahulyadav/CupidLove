package com.example.cupidlove.activity;

import com.example.cupidlove.utils.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.pixplicity.easyprefs.library.Prefs;
import com.example.cupidlove.R;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.cupidlove.customview.edittext.EditTextMedium;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.model.Register;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Constant;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.Utils;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;
import com.example.xmpp.MyXmppService;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends BaseActivity implements ResponseListener {

    //TODO: Bind All XML View With JAVA file
    @BindView(R.id.etBirthdate)
    EditTextMedium etBirthdate;

    @BindView(R.id.tvSignUp)
    TextViewRegular tvSignUp;

    @BindView(R.id.etEmail)
    EditTextMedium etEmail;

    @BindView(R.id.etFname)
    EditTextMedium etFname;

    @BindView(R.id.etLname)
    EditTextMedium etLname;

    @BindView(R.id.etCollage)
    EditTextMedium etCollage;

    @BindView(R.id.etProfession)
    EditTextMedium etProfession;

    @BindView(R.id.etPassword)
    EditTextMedium etPassword;

    @BindView(R.id.etConfirmPassword)
    EditTextMedium etConfirmPassword;

    @BindView(R.id.cbTermsandCondition)
    CheckBox cbTermsandCondition;

    @BindView(R.id.radioFemale)
    RadioButton radioFemale;

    @BindView(R.id.radioMale)
    RadioButton radioMale;


    //TODO : Variable Declaration
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog datePickerDialog;
    Dialog dialog1;
    String Email = null;
    String VarificationCode = null;
    private boolean allowClose = false;

    //TODo: create filter for edittext
    private String blockCharacterSet = "@()_+=-/~#^|$%&*!1234567890";
    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.special_char_not_allowed), Toast.LENGTH_LONG).show();
                return "";
            }
            return null;
        }


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        setChangeListener();
        etFname.setFilters(new InputFilter[]{filter});
        etLname.setFilters(new InputFilter[]{filter});

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };
        int textColor = Color.parseColor("#FFFFFF");
        isFullButton();
    }


    //TODO : Clcick On Terms And Privacy
    @OnClick(R.id.tvTermsandPrivacy)
    public void tvTermsandPrivacyClick() {
        //terms and condiotions and privacy policy url load
        if(getPreferences().getString(Config.TERMS_CONDITION,null)!=null) {
            Intent intent = new Intent(this, WebviewActivity.class);
            startActivity(intent);
        }

    }

    //TODO : Click On SignIn Click
    @OnClick(R.id.tvSignin)
    public void tvSigninClick() {
        finish();
    }

    //TODO : Click On SignUp Click
    @OnClick(R.id.tvSignUp)
    public void tvSignUpClick() {

        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.enter_email_address), Toast.LENGTH_SHORT).show();
        } else {
            if (Utils.isValidEmail(etEmail.getText().toString())) {
                if (etFname.getText().toString().isEmpty()) {
                    Toast.makeText(this, getResources().getString(R.string.enter_firstname), Toast.LENGTH_SHORT).show();
                } else {
                    if (etLname.getText().toString().isEmpty()) {
                        Toast.makeText(this, getResources().getString(R.string.enter_lastname), Toast.LENGTH_SHORT).show();
                    } else {
                        if (etBirthdate.getText().toString().isEmpty()) {
                            Toast.makeText(this, getResources().getString(R.string.enter_birthday), Toast.LENGTH_SHORT).show();
                        } else {
                            if (etBirthdate.getText().toString().isEmpty()) {
                                Toast.makeText(this, getResources().getString(R.string.enter_birthday), Toast.LENGTH_SHORT).show();
                            } else {
                                if (etCollage.getText().toString().isEmpty()) {
                                    Toast.makeText(this, getResources().getString(R.string.enter_collage_name), Toast.LENGTH_SHORT).show();
                                } else {
                                    if (etProfession.getText().toString().isEmpty()) {
                                        Toast.makeText(this, getResources().getString(R.string.enter_profession), Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (etPassword.getText().toString().isEmpty()) {
                                            Toast.makeText(this, getResources().getString(R.string.enter_password), Toast.LENGTH_SHORT).show();
                                        } else {
                                            if (etPassword.getText().length() < 6) {
                                                Toast.makeText(this, getResources().getString(R.string.valid_password), Toast.LENGTH_SHORT).show();
                                            } else {
                                                if (etConfirmPassword.getText().toString().isEmpty()) {
                                                    Toast.makeText(this, getResources().getString(R.string.enter_confirm_password), Toast.LENGTH_SHORT).show();
                                                } else {
                                                    if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {

                                                        if (cbTermsandCondition.isChecked()) {
                                                            Email = etEmail.getText().toString();
                                                            sendemail();

                                                        } else {
                                                            Toast.makeText(this, getResources().getString(R.string.accept_terms_and_condition), Toast.LENGTH_SHORT).show();
                                                        }
                                                    } else {
                                                        Toast.makeText(this, getResources().getString(R.string.password_and_confirm_password_not_matched), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, getResources().getString(R.string.enter_valid_email_address), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO: set text change listener for user first and last name
    public void setChangeListener() {
        etFname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
            }


            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (etFname.getText().toString().contains(" ")) {
//                    etFname.setText(etFname.getText().toString().replaceAll(" " , ""));
                    etFname.setText(etFname.getText().toString().replace(etFname.getText().toString().substring(etFname.getText().toString().length() - 1), ""));
                    etFname.setSelection(etFname.getText().length());

                    Toast.makeText(getApplicationContext(), "Spaces Not Allowed", Toast.LENGTH_LONG).show();
                }
            }
        });
        etLname.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
            }


            @Override
            public void beforeTextChanged(CharSequence s, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                if (etLname.getText().toString().contains(" ")) {
                    etLname.setText(etLname.getText().toString().replaceAll(" ", ""));
                    etLname.setSelection(etLname.getText().length());

                    Toast.makeText(getApplicationContext(), "Spaces Not Allowed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //TODO : CLick On Birthdate Set The Birth Date
    @OnClick(R.id.etBirthdate)
    public void etBirthdateClick() {
        //select date
        final Calendar c = Calendar.getInstance();
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);

        final int minYear = mYear - 18;
        final int minMonth = mMonth;
        final int minDay = mDay;

        if (etBirthdate.getText().toString().equals("")) {
            mYear = minYear;
            mMonth = minMonth;
            mDay = minDay;
        } else {
            String selectedDate = etBirthdate.getText().toString();
            String[] dateParts = selectedDate.split("/");
            String day = dateParts[0];
            String month = dateParts[1];
            String year = dateParts[2];

            mYear = Integer.parseInt(year);
            mMonth = Integer.parseInt(month) - 1;
            mDay = Integer.parseInt(day);
        }
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
            }
        };

        datePickerDialog = new DatePickerDialog(
                SignUpActivity.this, datePickerListener,
                mYear, mMonth, mDay) {
            @Override
            public void onBackPressed() {
                allowClose = true;
                super.onBackPressed();
            }

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {

                    DatePicker datePicker = datePickerDialog
                            .getDatePicker();

                    if (datePicker.getYear() < minYear || datePicker.getMonth() < minMonth && datePicker.getYear() == minYear ||
                            datePicker.getDayOfMonth() <= minDay && datePicker.getYear() == minYear && datePicker.getMonth() == minMonth) {

                        datePicker.updateDate(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
                        etBirthdate.setText(datePicker.getDayOfMonth() + "/" + (datePicker.getMonth() + 1) + "/" + datePicker.getYear());
                        allowClose = true;

                    } else {
                        allowClose = false;
                        Toast.makeText(SignUpActivity.this, R.string.enter_proper_detail, Toast.LENGTH_SHORT).show();
                        return;
                    }
                } else {
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        allowClose = true;
                    }
                }
                super.onClick(dialog, which);
            }

            @Override
            public void dismiss() {
                if (allowClose) {
                    super.dismiss();
                }
            }

        };

        datePickerDialog.setCancelable(false);
        datePickerDialog.show();

    }

    //TODO: Update Date Lable
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        etBirthdate.setText(sdf.format(myCalendar.getTime()));
    }

    //TODO : Set The Button Size
    public void isFullButton() {
        if (Constant.IS_FULL_BUTTON_SHOW) {
            tvSignUp.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
        } else {
            tvSignUp.setLayoutParams((new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)));
        }
    }

    //TODO: method send verification code to user email for email authentication
    public void sendemail() {
        try {
            showProgress("");
            RequestParams params = new RequestParams();

            params.put("email", etEmail.getText().toString());

            Debug.e("sendemail", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().SEND_EMAIL, params, new ResponseHandler(SignUpActivity.this, this, "sendemail"));
        } catch (Exception e) {
            Debug.e("sendemail C Exception", e.getMessage());
        }
    }

    //TODO: Send verification code method when user register
    public void sendvarification() {
        try {
            showProgress("");
            RequestParams params = new RequestParams();
            params.put("email", Email);
            params.put("verification_code", VarificationCode);
            Debug.e("sendvarification", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().SEND_VARIFICATION, params, new ResponseHandler(SignUpActivity.this, this, "sendvarification"));
        } catch (Exception e) {
            Debug.e("sendvarification Exception", e.getMessage());
        }
    }

    //TODO : User Register When Want To Login
    public void userRegister() {

        try {
            showProgress("");
            RequestParams params = new RequestParams();
            params.put("about", "About me");
            params.put("access_location", "1");
            params.put("date_pref", "1,2,3,4");
            params.put("device", "android");

            String refreshToken = FirebaseInstanceId.getInstance().getToken();

            SharedPreferences.Editor pre = getPreferences().edit();
            pre.putString(RequestParamUtils.DEVICE_TOKEN, refreshToken);
            pre.commit();

            params.put("device_token", refreshToken);
            params.put("dob", etBirthdate.getText().toString());

            if (etCollage.getText().toString().equals("")) {
                params.put("education", "-");
            } else {
                params.put("education", etCollage.getText().toString());
            }
            params.put("email", etEmail.getText().toString());
            params.put("ethnicity", "0");
            params.put("fname", etFname.getText().toString());

            if (radioMale.isChecked()) {
                params.put("gender", "male");
                params.put("gender_pref", "female");
            } else if (radioFemale.isChecked()) {
                params.put("gender", "female");
                params.put("gender_pref", "male");
            }
            params.put("height", "5'0 (152 cm)");
            params.put("kids", "None");
            params.put("lname", etLname.getText().toString());
            params.put("location_lat", getPreferences().getString(RequestParamUtils.LATITUDE, ""));
            params.put("location_long", getPreferences().getString(RequestParamUtils.LONGITUDE, ""));
            params.put("max_age_pref", "60");
            params.put("max_dist_pref", "200");
            params.put("min_age_pref", "18");
            params.put("min_dist_pref", "0");
            params.put("password", etPassword.getText().toString());
            if (etCollage.getText().toString().equals("")) {
                params.put("profession", "-");
            } else {
                params.put("profession", etProfession.getText().toString());
            }
            params.put("profile_image", "");
            params.put("que_ans", "ANSWER");
            params.put("que_id", "1");
            params.put("religion", "0");
            params.put("status", "0");

            Debug.e("userRegister", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().REGISTER, params, new ResponseHandler(SignUpActivity.this, this, "register"));
        } catch (Exception e) {
            Debug.e("userRegister Exception", e.getMessage());
        }

    }

    //TODO: Show varification dialog methood
    public void showvarificationDialog() {
        dialog1 = new Dialog(SignUpActivity.this, android.R.style.Theme_Light);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.varification_code);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.blackHint);
        dialog1.findViewById(R.id.sendvarification).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextMedium e1 = dialog1.findViewById(R.id.varificationcode);
                VarificationCode = e1.getText().toString();
                sendvarification();
            }
        });
        dialog1.show();
    }

    //TODO: Register Response
    @Override
    public void onResponse(String response, String methodName) {
        dismissProgress();

        if (response == null || response.equals("")) {

        } else if (methodName.equals("sendemail")) {
            //register

            Log.e(methodName + " Response is ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    new AlertDialog.Builder(this)
                            .setTitle(getResources().getText(R.string.app_name))
                            .setMessage(getResources().getString(R.string.send_varification_code))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showvarificationDialog();

                                }
                            }).show();

                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable t) {
                Debug.e("error", response);
            }
        } else if (methodName.equals("sendvarification")) {
            //register

            Log.e(methodName + " Response is ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {

                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    userRegister();

                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable t) {
                Debug.e("error", response);
            }
            dialog1.dismiss();
        } else if (methodName.equals("register")) {
            //register

            Log.e("dict", response);

            final Register registerRider = new Gson().fromJson(
                    response, new TypeToken<Register>() {
                    }.getType());

            if (!registerRider.error) {
                //sign up Successfully
                SharedPreferences.Editor pre = getPreferences().edit();
                pre.putString(RequestParamUtils.USER_DATA, response);

                pre.putString(RequestParamUtils.AUTH_TOKEN, registerRider.body.authToken + "");
                pre.putString(RequestParamUtils.USER_ID, registerRider.body.id + "");
                pre.putString(RequestParamUtils.FIRST_NAME, registerRider.body.fname + "");
                pre.putString(RequestParamUtils.LAST_NAME, registerRider.body.lname + "");
                pre.putString(RequestParamUtils.EMAIL, registerRider.body.email + "");
                pre.putString(RequestParamUtils.EDUCATION, registerRider.body.education + "");
                pre.putString(RequestParamUtils.PROFETION, registerRider.body.profession + "");
                pre.putString(RequestParamUtils.BIRTHDATE, registerRider.body.dob + "");
                pre.putString(RequestParamUtils.AGE, registerRider.body.age + "");
                Prefs.putString(RequestParamUtils.USER_ID, registerRider.body.id + "");
                Prefs.putString(RequestParamUtils.FIRST_NAME, registerRider.body.ejUser + "");
                Prefs.putString(RequestParamUtils.GENDER, registerRider.body.gender + "");
                Prefs.putString(RequestParamUtils.ROSTER_NICK_NAME, registerRider.body.fname + "");
//                Prefs.putString(RequestParamUtils.XMPPUSERNAME, registerRider.body.id + "_" + registerRider.body.fname);
//                Prefs.putString(RequestParamUtils.XMPPUSERPASSWORD, "potenza@123");
                pre.putString(RequestParamUtils.REGISTER, "registerdatepref");
                pre.commit();

                doBindService();
                Intent intent = new Intent(this, DatePreferenceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(this, registerRider.message, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public MyXmppService getmService() {
        return mXmppService;
    }
}
