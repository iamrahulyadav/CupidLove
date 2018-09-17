package com.example.cupidlove.fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.FacebookImagesActivity;
import com.example.cupidlove.activity.GalleryActivity;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.adapter.EthnicityAdapter;
import com.example.cupidlove.adapter.InstaUserImageAdapter;
import com.example.cupidlove.adapter.ReligionAdapter;
import com.example.cupidlove.customview.Crop.Crop;
import com.example.cupidlove.customview.edittext.EditTextMedium;
import com.example.cupidlove.customview.edittext.EditTextRegular;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.helper.InstagramApp;
import com.example.cupidlove.interfaces.OnItemClickListner;
import com.example.cupidlove.model.GalleryImages;
import com.example.cupidlove.model.InstagramImages;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class MyProfileFragment extends Fragment implements OnItemClickListner, ResponseListener, View.OnClickListener {

    View view;
    LayoutInflater inflater;
    ViewGroup container;
    private OnFragmentInteractionListener mListener;

    @BindView(R.id.ivMale)
    ImageView ivMale;

    @BindView(R.id.ivFemale)
    ImageView ivFemale;

    @BindView(R.id.cbMale)
    CheckBox cbMale;

    @BindView(R.id.cbFemale)
    CheckBox cbFemale;

    @BindView(R.id.npHeight)
    NumberPicker npHeight;

    @BindView(R.id.rvReligion)
    RecyclerView rvReligion;

    @BindView(R.id.rvInstaUserImages)
    RecyclerView rvInstaUserImages;

    @BindView(R.id.llPhotoSelection)
    LinearLayout llPhotoSelection;

    @BindView(R.id.ivImageOne)
    ImageView ivImageOne;

    @BindView(R.id.ivImageTwo)
    ImageView ivImageTwo;

    @BindView(R.id.ivImageThree)
    ImageView ivImageThree;

    @BindView(R.id.ivImageFour)
    ImageView ivImageFour;

    @BindView(R.id.ivImageFive)
    ImageView ivImageFive;

    @BindView(R.id.ivImageSix)
    ImageView ivImageSix;

    @BindView(R.id.etAboutMe)
    EditTextRegular etAboutMe;

    @BindView(R.id.llInstaConnected)
    LinearLayout llInstaConnected;

    @BindView(R.id.llInstanotConnected)
    LinearLayout llInstanotConnected;

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

    @BindView(R.id.etBirthdate)
    EditTextMedium etBirthdate;

    @BindView(R.id.rvEthnicity)
    RecyclerView rvEthnicity;

    @BindView(R.id.llInstaView)
    LinearLayout llInstaView;

    ReligionAdapter religionAdapter;
    InstaUserImageAdapter instaUserImageAdapter;
    String AGEUSER;
    Dialog dialog1;
    List<String> listSelectedReligion = new ArrayList<>();
    List<JSONObject> listReligion = new ArrayList<>();
    ArrayList<String> galleryImages = new ArrayList<>();
    List<JSONObject> listEthnicity = new ArrayList<>();
    List<String> listSelectedEthnicity = new ArrayList<>();
    EthnicityAdapter ethnicityAdapter;

    int selectedPosition = 0;

    final String[] height = {"3'0 (92 cm)", "3'1 (94 cm)", "3'2 (97 cm)", "3'3 (99 cm)", "3'4 (102 cm)", "3'5 (104 cm)",
            "3'6 (107 cm)", "3'7 (109 cm)", "3'8 (112 cm)", "3'9 (114 cm)", "3'10 (117 cm)", "3'11 (119 cm)",
            "4'0 (122 cm)", "4'1 (125 cm)", "4'2 (127 cm)", "4'3 (130 cm)", "4'4 (132 cm)", "4'5 (135 cm)",
            "4'6 (137 cm)", "4'7 (140 cm)", "4'8 (142 cm)", "4'9 (145 cm)", "4'10 (147 cm)", "4'11 (150 cm)",
            "5'0 (152 cm)", "5'1 (155 cm)", "5'2 (158 cm)", "5'3 (160 cm)", "5'4 (163 cm)", "5'5 (165 cm)",
            "5'6 (168 cm)", "5'7 (170 cm)", "5'8 (173 cm)", "5'9 (175 cm)", "5'10 (178 cm)", "5'11 (180 cm)",
            "6'0 (183 cm)", "6'1 (185 cm)", "6'2 (188 cm)", "6'3 (191 cm)", "6'4 (193 cm)", "6'5 (196 cm)",
            "6'6 (198 cm)", "6'7 (201 cm)", "6'8 (203 cm)", "6'9 (206 cm)", "6'10 (208 cm)", "6'11 (211 cm)",
            "7'0 (213 cm)"};

    InstagramApp mApp;
    private boolean isFirst = false;
    InstagramImages instagramImagesRider;
    private List<String> instagramImages = new ArrayList<>();
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    DatePickerDialog datePickerDialog;
    String Email = null;
    String VarificationCode = null;
    private boolean allowClose = false;
    ProgressBar loader;
    private Uri mImageCaptureUri;


    public MyProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).settvTitle(getResources().getString(R.string.my_profile));
        ((HomeActivity) getActivity()).showOrHideAdview();

        ((BaseActivity) getActivity()).hideSearch();
        loader = (ProgressBar) view.findViewById(R.id.loader);
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


        String insta_client_id = ((BaseActivity) getActivity()).getPreferences().getString(Config.INSTAGRAM_CLIENT_ID, "");
        String insta_client_secret = ((BaseActivity) getActivity()).getPreferences().getString(Config.INSTAGRAM_CLIENT_SECRET, "");
        String insta_callback_base = ((BaseActivity) getActivity()).getPreferences().getString(Config.INSTAGRAM_CALLBACK_BASE, "");

        if (insta_client_id == "" || insta_client_secret == "" || insta_callback_base == "") {
            llInstaView.setVisibility(View.GONE);
        }

        getDefaultData();

        this.inflater = inflater;
        this.container = container;

        setImageView();
        getRegisterDetail();
        setHeightPicker();
        setReligionAdapter();
        setEthnicityAdapter();
        setInstagramImagesAdapter();


        return view;
    }

    //TODO: resume method
    @Override
    public void onResume() {
        super.onResume();
    }


    //TODO: Instagram connection
    @OnClick(R.id.ivInstaConnect)
    public void ivInstaConnectClick() {
        //login to instagram
        isFirst = false;
        instagramImages = new ArrayList<>();
        instaLogin();
    }

    //TODO: Update Instagram image
    @OnClick(R.id.tvUpdateInstaImages)
    public void tvUpdateInstaImagesClick() {
        //login to instagram
        isFirst = false;
        instagramImages = new ArrayList<>();
        instaLogin();
    }

    //TODO: Next click
    @OnClick(R.id.tvNext)
    public void tvNextClick() {
        if (etEmail.getText().toString().isEmpty()) {
            Toast.makeText(getActivity(), getResources().getString(R.string.enter_email_address), Toast.LENGTH_SHORT).show();
        } else {
            if (etFname.getText().toString().isEmpty()) {
                Toast.makeText(getActivity(), getResources().getString(R.string.enter_firstname), Toast.LENGTH_SHORT).show();
            } else {
                if (etLname.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), getResources().getString(R.string.enter_lastname), Toast.LENGTH_SHORT).show();
                } else {
                    if (etBirthdate.getText().toString().isEmpty()) {
                        Toast.makeText(getActivity(), getResources().getString(R.string.enter_birthday), Toast.LENGTH_SHORT).show();
                    } else {
                        if (etCollage.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), getResources().getString(R.string.enter_collage_name), Toast.LENGTH_SHORT).show();
                        } else {
                            if (etProfession.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), getResources().getString(R.string.enter_profession), Toast.LENGTH_SHORT).show();
                            } else {
                                if (etAboutMe.getText().toString().length() > 500) {
                                    Toast.makeText(getActivity(), R.string.description, Toast.LENGTH_SHORT).show();
                                } else if (listSelectedEthnicity.size() == 0) {
                                    Toast.makeText(getActivity(), R.string.select_ethnicity, Toast.LENGTH_SHORT).show();
                                } else if (listSelectedReligion.size() == 0) {
                                    Toast.makeText(getActivity(), R.string.select_religion, Toast.LENGTH_SHORT).show();
                                } else {
                                    if (etEmail.getText().toString().equals(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.EMAIL, ""))) {
                                        updateuserprofile();
                                    } else {
                                        Email = etEmail.getText().toString();
                                        sendemail();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    //TODO: Image one click
    @OnClick(R.id.llImageOne)
    public void llImageOneClick() {
        selectedPosition = 1;
        CharSequence[] items = {"Gallery", "Take Photo", "Cancel"};
        selectImage(items);
    }

    //TODO: Second Image click
    @OnClick(R.id.llImageTwo)
    public void llImageTwoClick() {
        selectedPosition = 2;
        CharSequence[] items = {"Gallery", "Take Photo", "Delete", "Cancel"};
        selectImage(items);
    }

    //TODO : Third Image click
    @OnClick(R.id.llImageThree)
    public void llImageThreeClick() {
        selectedPosition = 3;
        CharSequence[] items = {"Gallery", "Take Photo", "Delete", "Cancel"};
        selectImage(items);
    }

    //TODO: forth image click
    @OnClick(R.id.llImageFour)
    public void llImageFourClick() {
        selectedPosition = 4;
        CharSequence[] items = {"Gallery", "Take Photo", "Delete", "Cancel"};
        selectImage(items);
    }

    //TODO: fifth image click
    @OnClick(R.id.llImageFive)
    public void llImageFiveClick() {
        selectedPosition = 5;
        CharSequence[] items = {"Gallery", "Take Photo", "Delete", "Cancel"};
        selectImage(items);
    }

    //TODO: sixth image click
    @OnClick(R.id.llImageSix)
    public void llImageSixClick() {
        selectedPosition = 6;
        CharSequence[] items = {"Gallery", "Take Photo", "Delete", "Cancel"};
        selectImage(items);
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


    //TODO: set profile photo for user
    public void setImageView() {
        final LinearLayout.LayoutParams paramsImagePhoto = (LinearLayout.LayoutParams) llPhotoSelection.getLayoutParams();
        llPhotoSelection.post(new Runnable() {
            @Override
            public void run() {
                paramsImagePhoto.height = llPhotoSelection.getWidth();
                llPhotoSelection.setLayoutParams(paramsImagePhoto);
            }
        });
    }

    public void setEthnicityAdapter() {
        ethnicityAdapter = new EthnicityAdapter(getActivity(), this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2);
        rvEthnicity.setLayoutManager(mLayoutManager);
        rvEthnicity.setAdapter(ethnicityAdapter);
        rvEthnicity.setNestedScrollingEnabled(true);
    }

    //TODO: set religion adapter
    public void setReligionAdapter() {
        religionAdapter = new ReligionAdapter(getActivity(), this);
        final GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        rvReligion.setLayoutManager(mLayoutManager);
        rvReligion.setAdapter(religionAdapter);
        rvReligion.setNestedScrollingEnabled(false);
    }

    //TODO: set instagram adapter
    public void setInstagramImagesAdapter() {
        instaUserImageAdapter = new InstaUserImageAdapter(getActivity(), this);
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.HORIZONTAL, false);
        rvInstaUserImages.setLayoutManager(mLayoutManager);
        rvInstaUserImages.setAdapter(instaUserImageAdapter);
        rvInstaUserImages.setNestedScrollingEnabled(false);
        ViewCompat.setNestedScrollingEnabled(rvInstaUserImages, false);
    }

    //TODO: send email for email authentication to verification code
    public void sendemail() {
        try {
            ((BaseActivity) getActivity()).showProgress("");
            RequestParams params = new RequestParams();

            params.put("email", etEmail.getText().toString());

            Debug.e("sendemail", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().SEND_EMAIL, params, new ResponseHandler(getActivity(), this, "sendemail"));
        } catch (Exception e) {
            Debug.e("sendemail Exception", e.getMessage());
        }
    }

    //TODO: send varification code to user email address
    public void sendvarification() {
        try {
            ((BaseActivity) getActivity()).showProgress("");
            RequestParams params = new RequestParams();
            params.put("email", Email);
            params.put("verification_code", VarificationCode);
            Debug.e("sendvarification", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().SEND_VARIFICATION, params, new ResponseHandler(getActivity(), this, "sendvarification"));
        } catch (Exception e) {
            Debug.e("sendvarification Exception", e.getMessage());
        }

    }

    //TODO: show varification dialog
    public void showvarificationDialog() {
        dialog1 = new Dialog(getActivity(), android.R.style.Theme_Light);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.varification_code);
        dialog1.getWindow().setBackgroundDrawableResource(R.color.transparent_white);
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

    //TODO: On item click for religion
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
        }
    }

    //TODO: set height picker
    public void setHeightPicker() {

        //Initializing a new string array with elements

        npHeight.setMinValue(0); //from array first value
        npHeight.setMaxValue(height.length - 1); //to array last value
        npHeight.setDisplayedValues(height);
        npHeight.setWrapSelectorWheel(false);

        npHeight.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });

    }

    //TODO: Delete image api call for gallary
    public void DeleteImage() {

        try {
            ((BaseActivity) getActivity()).showProgress("");
            String val = null;
            if (selectedPosition == 1) {
                val = "img1";
            } else if (selectedPosition == 2) {
                val = "img2";
            } else if (selectedPosition == 3) {
                val = "img3";
            } else if (selectedPosition == 4) {
                val = "img4";
            } else if (selectedPosition == 5) {
                val = "img5";
            } else if (selectedPosition == 6) {
                val = "img6";
            }
            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("user_id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("key", val);
            Debug.e("DeleteImage", params.toString());

            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            AsyncHttpRequest.newRequest().post(new URLS().DELETE_IMAGE, params, new ResponseHandler(getActivity(), this, "DeleteImage"));
        } catch (Exception e) {
            Debug.e("DeleteImage Exception", e.getMessage());
        }

    }

    //TODO : select image dialog
    private void selectImage(final CharSequence[] items) {


        final TextViewRegular title = new TextViewRegular(getActivity());
        title.setText("Add Photo");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setPadding(10, 25, 10, 25);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setCustomTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Facebook")) {

                    //get image from fb
                    Intent intent = new Intent(getActivity(), FacebookImagesActivity.class);
                    intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
                    startActivity(intent);

                } else if (items[item].equals("Take Photo")) {

                    //Camera
                    if (mayRequestPermission()) {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        mImageCaptureUri = Uri.fromFile(new File(Environment
                                .getExternalStorageDirectory(), "tmp_avatar_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".jpg"));

                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        } else {
                            File file = new File(mImageCaptureUri.getPath());
                            Uri photoUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".provider", file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                        }

                        try {
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, 0);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (items[item].equals("Gallery")) {

                    //Gallery
                    if (mayRequestPermissionForExternalStorage()) {
                        Intent intent = new Intent(getActivity(), GalleryActivity.class);
                        intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
                        startActivityForResult(intent, 1);
                    }

                } else if (items[item].equals("Delete")) {

                    DeleteImage();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
//                else if (items[item].equals("Instagram")) {
//
//                    //Intagram
//                    Intent intent = new Intent(getActivity(), InstagramImagesActivity.class);
//                    intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
//                    startActivity(intent);
//
//                }
            }
        });
        builder.show();
    }


    //TODO : Permission for access camera
    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 1212);
            return false;
        }
    }

    //TODO: Permission for access external storage
    private boolean mayRequestPermissionForExternalStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    //TODO: permission result method
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1212) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePicture, 0);//zero can be replaced with any action code

            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.allow_permission_to_access_camera), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(getActivity(), GalleryActivity.class);
                intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
                startActivityForResult(intent, 1);
            } else {
                Toast.makeText(getActivity(), getResources().getString(R.string.allow_permission_to_access_gallery), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO: Activity result for camera capture image

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
//        onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Uri selectedImage = null;
        switch (requestCode) {
            case 0:
                //Camera
                if (resultCode == getActivity().RESULT_OK) {
                    beginCrop(mImageCaptureUri);
                }
                break;
            case 1:
                //Gallery
                if (resultCode == getActivity().RESULT_OK) {
                    selectedImage = Uri.parse(imageReturnedIntent.getExtras().getString("image"));
                    if (selectedImage != null) {
                        beginCrop(selectedImage);
                    }
                }
                break;

            case Crop.REQUEST_CROP:

                Bitmap photo = null;
                if (imageReturnedIntent != null) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    if (extras == null) {
                        photo = extras.getParcelable("data");
                    } else {
                        photo = null;
                        try {
                            photo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), Crop.getOutput(imageReturnedIntent));
                        } catch (IOException e) {
                            Log.e("Image selection ", e.getMessage());
                        }
                    }
                    String path = filePath(photo);
                    if (selectedPosition == 1) {
                        ivImageOne.setImageBitmap(photo);
                        editGalleryImages("img1", path);
                    } else if (selectedPosition == 2) {
                        ivImageTwo.setImageBitmap(photo);
                        editGalleryImages("img2", path);
                    } else if (selectedPosition == 3) {
                        ivImageThree.setImageBitmap(photo);
                        editGalleryImages("img3", path);
                    } else if (selectedPosition == 4) {
                        ivImageFour.setImageBitmap(photo);
                        editGalleryImages("img4", path);
                    } else if (selectedPosition == 5) {
                        ivImageFive.setImageBitmap(photo);
                        editGalleryImages("img5", path);
                    } else if (selectedPosition == 6) {
                        ivImageSix.setImageBitmap(photo);
                        editGalleryImages("img6", path);
                    }
                }


                break;


        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getActivity().getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(1, 1).start(getContext(), MyProfileFragment.this);
    }


    //TODO: Capture iamge result
    private String filePath(Bitmap bitmap) {

        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
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
    }


    @Override
    public void onClick(View v) {

    }

    //TODO: instagram Login
    public void instaLogin() {

        mApp = new InstagramApp(getActivity(), ((BaseActivity) getActivity()).getPreferences().getString(Config.INSTAGRAM_CLIENT_ID, RequestParamUtils.INSTAGRAM_CLIENT_ID),
                ((BaseActivity) getActivity()).getPreferences().getString(Config.INSTAGRAM_CLIENT_SECRET, RequestParamUtils.INSTAGRAM_CLIENT_SECRET),
                ((BaseActivity) getActivity()).getPreferences().getString(Config.INSTAGRAM_CALLBACK_BASE, RequestParamUtils.INSTAGRAM_CALLBACK_URL));
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
                    Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @OnClick(R.id.etEmail)
    public void EmailClick() {
        etEmail.setFocusable(true);
    }

    @OnClick(R.id.etFname)
    public void FnameClick() {
        etFname.setFocusable(true);
    }

    @OnClick(R.id.etLname)
    public void LnameClick() {
        etLname.setFocusable(true);
    }

    @OnClick(R.id.etBirthdate)
    public void BirthdtaeClick() {
        etBirthdate.setFocusable(true);
    }

    @OnClick(R.id.etProfession)
    public void ProffessionClick() {
        etProfession.setFocusable(true);
    }

    @OnClick(R.id.etCollage)
    public void CollageClick() {
        etCollage.setFocusable(true);
    }

    @OnClick(R.id.etAboutMe)
    public void AboutClick() {
        etAboutMe.setFocusable(true);
    }

    //TODO: Get user register detail of user
    public void getRegisterDetail() {

        etEmail.setText(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.EMAIL, ""));
        etFname.setText(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.FIRST_NAME, ""));
        etLname.setText(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LAST_NAME, ""));
        etCollage.setText(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.EDUCATION, ""));
        etProfession.setText(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.PROFETION, ""));
        etBirthdate.setText(((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.BIRTHDATE, ""));
    }

    //TODO: Get language default data
    public void getDefaultData() {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("language", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.LANGUAGE, "en"));

            Debug.e("getDefaultData", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().GET_ALL_STATIC, params, new ResponseHandler(getActivity(), this, "default_data"));
        } catch (Exception e) {
            Debug.e("getDefaultData Exception", e.getMessage());
        }

    }

    //TODO: Get User detail
    public void getUserDetail() {

        try {

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("userid", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("getUserDetail", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().GET_USER_DETAIL, params, new ResponseHandler(getActivity(), this, "get_user_detail"));
        } catch (Exception e) {
            Debug.e("getUserDetail Exception", e.getMessage());
        }

    }

    //TODO: get Insagram image api call
    private void getInstaPhotos(String str) {

        Log.e("InstagramApp", "Connected as " + mApp.getUserName() + " ID " + mApp.getId());
        String instaApi;
        if (str != null) {
            instaApi = str;

        } else if (isFirst == false) {
            isFirst = true;
            instaApi = "https://api.instagram.com/v1/users/" + mApp.getId() + "/media/recent/?access_token=" + mApp.getAccessToken();

        } else {
            updateInstagramImages();
            return;
        }
        Log.w("imagesInsta1", instaApi);

        //api call from here
        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));

            Debug.e("getInstaPhotos", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.get(instaApi, params, new ResponseHandler(getActivity(), this, "instagram_login_images"));

        } catch (Exception e) {
            Debug.e("getInstaPhotos Exception", e.getMessage());
        }

    }

    //TODO : Update insagram image api call
    public void updateInstagramImages() {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));

            params.put("url", instagramImages);//TODO:Pass Instagram image array

            params.put("userid", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("updateInstagramImages", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().SAVE_INSTAGRAM_IMAGES, params, new ResponseHandler(getActivity(), this, "save_instagram_images"));
        } catch (Exception e) {
            Debug.e("updateInstagramImages Exception", e.getMessage());
        }

    }


    //TODO : Get instagram image api call
    public void getInstagramImages() {
        ((BaseActivity) getActivity()).dismissProgress();
        ((BaseActivity) getActivity()).showProgress("");
        try {
            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("friendid", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("userid", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            Debug.e("getInstagramImages", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().GET_INSTAGRAM_IMAGES, params, new ResponseHandler(getActivity(), this, "instagram_images"));
        } catch (Exception e) {
            Debug.e("getInstagramImages Exception", e.getMessage());
        }

    }

    //TODO: Update user profile api call
    public void updateuserprofile() {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("about", etAboutMe.getText().toString());
            params.put("email", etEmail.getText().toString());
            params.put("fname", etFname.getText().toString());
            params.put("lname", etLname.getText().toString());
            params.put("education", etCollage.getText().toString());
            params.put("profession", etProfession.getText().toString());
            params.put("dob", etBirthdate.getText().toString());

            SharedPreferences.Editor pre = ((BaseActivity) getActivity()).getPreferences().edit();
            pre.commit();

            if (cbMale.isChecked()) {
                params.put("gender", "male");
            } else if (cbFemale.isChecked()) {
                params.put("gender", "female");
            }
            params.put("height", height[npHeight.getValue()] + "");

            String strReligion = "";
            for (int i = 0; i < listSelectedReligion.size(); i++) {
                if (i == 0) {
                    strReligion = listSelectedReligion.get(i);
                } else {
                    strReligion = strReligion + "," + listSelectedReligion.get(i);
                }
            }
            params.put("religion", strReligion);

            String strEthnicity = "";
            for (int i = 0; i < listSelectedEthnicity.size(); i++) {
                if (i == 0) {
                    strEthnicity = listSelectedEthnicity.get(i);
                } else {
                    strEthnicity = strEthnicity + "," + listSelectedEthnicity.get(i);
                }
            }
            params.put("ethnicity", strEthnicity);

            Debug.e("updateuserprofile", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().UPDATE_PROFILE, params, new ResponseHandler(getActivity(), this, "updateuserprofile"));
        } catch (Exception e) {
            Debug.e("updateuserprofile Exception", e.getMessage());
        }

    }

    //TODO: edit gallary image api call
    public void editGalleryImages(String key, String imageFile) {

        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));

            String filePath = imageFile;
            String filePath1 = "";


            filePath1 = filePath.replace("file:", "");
            File file = new File(filePath1);
            Log.e("filePath is ", filePath);

            try {
                params.put(key, file, "image/jpg");

            } catch (Exception e) {
                Log.e("FileNotFound", e.getMessage());
            }
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();
            asyncHttpClient.post(new URLS().EDIT_GALLERY_IMAGES, params, new ResponseHandler(getActivity(), this, "editGalleryImages"));
        } catch (Exception e) {
            Debug.e("editGalleryImages Exception", e.getMessage() + " exception");
        }
    }

    //TODO: Get age method  from birthdate
    private String getAge(int year, int month, int day) {
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)) {
            age--;
        }

        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();

        return ageS;
    }

    //TODO: Birthdate click
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
            String[] dateParts = selectedDate.split("-");
            String year = dateParts[0];
            String month = dateParts[1];
            String day = dateParts[2];

            mYear = Integer.parseInt(year);
            mMonth = Integer.parseInt(month) - 1;
            mDay = Integer.parseInt(day);
        }
        DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int selectedYear,
                                  int selectedMonth, int selectedDay) {
            }
        };

        datePickerDialog = new DatePickerDialog(getActivity(), datePickerListener,
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
                        etBirthdate.setText(datePicker.getYear() + "-" + (datePicker.getMonth() + 1) + "-" + datePicker.getDayOfMonth());
                        allowClose = true;

                    } else {
                        allowClose = false;
                        Toast.makeText(getActivity(), R.string.enter_proper_detail, Toast.LENGTH_SHORT).show();
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

    //TODO: response of all api call
    @Override
    public void onResponse(String response, String methodName) {


        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("sendemail")) {
            //register

            Log.e(methodName + " Response is ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    new android.app.AlertDialog.Builder(getActivity())
                            .setTitle(getResources().getText(R.string.app_name))
                            .setMessage(getResources().getString(R.string.send_varification_code))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    showvarificationDialog();

                                }
                            }).show();

                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable t) {
                Debug.e("error", response);
            }
            ((BaseActivity) getActivity()).dismissProgress();
        } else if (methodName.equals("sendvarification")) {
            //register

            Log.e(methodName + " Response is ", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {

                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    updateuserprofile();
                    dialog1.dismiss();

                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (Throwable t) {
                Debug.e("error", response);
            }
        } else if (methodName.equals("DeleteImage")) {
            Log.e("response", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    if (selectedPosition == 1) {
                        ivImageOne.setImageResource(R.drawable.image_not_found);
                    } else if (selectedPosition == 2) {
                        ivImageTwo.setImageResource(R.drawable.image_not_found);
                    } else if (selectedPosition == 3) {
                        ivImageThree.setImageResource(R.drawable.image_not_found);
                    } else if (selectedPosition == 4) {
                        ivImageFour.setImageResource(R.drawable.image_not_found);
                    } else if (selectedPosition == 5) {
                        ivImageFive.setImageResource(R.drawable.image_not_found);
                    } else if (selectedPosition == 6) {
                        ivImageSix.setImageResource(R.drawable.image_not_found);
                    }
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            ((BaseActivity) getActivity()).dismissProgress();

        } else if (methodName.equals("editGalleryImages")) {

            //get all static data
            Log.e("response", response);
            try {
                final GalleryImages galleryImagesRider = new Gson().fromJson(
                        response, new TypeToken<GalleryImages>() {
                        }.getType());

                if (galleryImagesRider.error) {
                    //error
                    Toast.makeText(getActivity(), galleryImagesRider.message, Toast.LENGTH_SHORT).show();
                } else {
                    //updated images
                    Toast.makeText(getActivity(), getResources().getString(R.string.info_updated_successfully), Toast.LENGTH_SHORT).show();

                    SharedPreferences.Editor pre = ((BaseActivity) getActivity()).getPreferences().edit();

                    if (galleryImagesRider.gallary.img1 != null) {
                        pre.putString(RequestParamUtils.PROFILE_IMAGE, galleryImagesRider.gallary.img1 + "");
                    } else if (galleryImagesRider.gallary.img2 != null) {
                        pre.putString(RequestParamUtils.PROFILE_IMAGE, galleryImagesRider.gallary.img2 + "");
                    } else if (galleryImagesRider.gallary.img3 != null) {
                        pre.putString(RequestParamUtils.PROFILE_IMAGE, galleryImagesRider.gallary.img3 + "");
                    } else if (galleryImagesRider.gallary.img4 != null) {
                        pre.putString(RequestParamUtils.PROFILE_IMAGE, galleryImagesRider.gallary.img4 + "");
                    } else if (galleryImagesRider.gallary.img5 != null) {
                        pre.putString(RequestParamUtils.PROFILE_IMAGE, galleryImagesRider.gallary.img5 + "");
                    } else if (galleryImagesRider.gallary.img6 != null) {
                        pre.putString(RequestParamUtils.PROFILE_IMAGE, galleryImagesRider.gallary.img6 + "");
                    }
                    pre.commit();

                    if (galleryImagesRider.gallary.img1 != null) {
                        galleryImages.add(0, new URLS().UPLOAD_URL + galleryImagesRider.gallary.img1);
                    }
                    if (galleryImagesRider.gallary.img2 != null) {
                        galleryImages.add(1, new URLS().UPLOAD_URL + galleryImagesRider.gallary.img2);
                    }
                    if (galleryImagesRider.gallary.img3 != null) {
                        galleryImages.add(2, new URLS().UPLOAD_URL + galleryImagesRider.gallary.img3);
                    }
                    if (galleryImagesRider.gallary.img4 != null) {
                        galleryImages.add(3, new URLS().UPLOAD_URL + galleryImagesRider.gallary.img4);
                    }
                    if (galleryImagesRider.gallary.img5 != null) {
                        galleryImages.add(4, new URLS().UPLOAD_URL + galleryImagesRider.gallary.img5);
                    }
                    if (galleryImagesRider.gallary.img6 != null) {
                        galleryImages.add(5, new URLS().UPLOAD_URL + galleryImagesRider.gallary.img6);
                    }
                }

                ((BaseActivity) getActivity()).dismissProgress();
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            ((BaseActivity) getActivity()).dismissProgress();
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


                getUserDetail();

            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (methodName.equals("get_user_detail")) {

            //get user data
            Log.e("response", response);

            final UserDetail userDetailRider = new Gson().fromJson(
                    response, new TypeToken<UserDetail>() {
                    }.getType());

            setUserData(userDetailRider);
            getInstagramImages();
            ((BaseActivity) getActivity()).dismissProgress();

        } else if (methodName.equals("save_instagram_images")) {
            ((BaseActivity) getActivity()).dismissProgress();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    getInstagramImages();
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
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
            ((BaseActivity) getActivity()).dismissProgress();

        } else if (methodName.equals("updateuserprofile")) {
            ((BaseActivity) getActivity()).dismissProgress();
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done
                    ((BaseActivity) getActivity()).dismissProgress();

                    SharedPreferences.Editor pre = ((BaseActivity) getActivity()).getPreferences().edit();
                    pre.putString(RequestParamUtils.FIRST_NAME, etFname.getText().toString());
                    pre.putString(RequestParamUtils.LAST_NAME, etLname.getText().toString());
                    pre.putString(RequestParamUtils.EMAIL, etEmail.getText().toString());
                    pre.putString(RequestParamUtils.EDUCATION, etCollage.getText().toString());
                    pre.putString(RequestParamUtils.PROFETION, etProfession.getText().toString());
                    pre.putString(RequestParamUtils.BIRTHDATE, etBirthdate.getText().toString());

                    String selectedDate = etBirthdate.getText().toString();
                    String[] dateParts = selectedDate.split("-");
                    int year = Integer.parseInt(dateParts[0]);
                    int month = Integer.parseInt(dateParts[1]);
                    int day = Integer.parseInt(dateParts[2]);
                    pre.putString(RequestParamUtils.AGE, getAge(year, month, day));
                    pre.commit();
                    ((HomeActivity) getActivity()).initDrawer();
                    Toast.makeText(getActivity(), getResources().getString(R.string.info_updated_successfully), Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
            ((BaseActivity) getActivity()).dismissProgress();
        } else if (methodName.equals("instagram_login_images")) {
            ((BaseActivity) getActivity()).dismissProgress();

            //get user data
            Log.e("response", response);

            instagramImagesRider = new Gson().fromJson(
                    response, new TypeToken<InstagramImages>() {
                    }.getType());

            for (int i = 0; i < instagramImagesRider.data.size(); i++) {
                instagramImages.add(instagramImagesRider.data.get(i).images.standardResolution.url);
            }

            if (instagramImagesRider != null) {
                getInstaPhotos(instagramImagesRider.pagination.nextUrl);
            }
            ((BaseActivity) getActivity()).dismissProgress();
        }
    }

    //TODO: diaplay gallary image
    @Nullable
    private String onGalleryImageResult(String data) {

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

    //TODO: set user data
    public void setUserData(UserDetail userDetailRider) {
        if (!userDetailRider.error) {
            galleryImages = new ArrayList<>();
            if (userDetailRider.body.gallary.img1 != null) {
                Picasso.with(getActivity()).load(new URLS().UPLOAD_URL + userDetailRider.body.gallary.img1).error(R.drawable.image_not_found).into(ivImageOne);
                galleryImages.add(0, new URLS().UPLOAD_URL + userDetailRider.body.gallary.img1);
            } else {
                ivImageOne.setImageResource(R.drawable.image_not_found);
                galleryImages.add(0, null);
            }
            if (userDetailRider.body.gallary.img2 != null) {
                Picasso.with(getActivity()).load(new URLS().UPLOAD_URL + userDetailRider.body.gallary.img2).error(R.drawable.image_not_found).into(ivImageTwo);
                galleryImages.add(1, new URLS().UPLOAD_URL + userDetailRider.body.gallary.img2);
            } else {
                ivImageTwo.setImageResource(R.drawable.image_not_found);
                galleryImages.add(1, null);
            }
            if (userDetailRider.body.gallary.img3 != null) {
                Picasso.with(getActivity()).load(new URLS().UPLOAD_URL + userDetailRider.body.gallary.img3).error(R.drawable.image_not_found).into(ivImageThree);
                galleryImages.add(2, new URLS().UPLOAD_URL + userDetailRider.body.gallary.img3);
            } else {
                ivImageThree.setImageResource(R.drawable.image_not_found);
                galleryImages.add(2, null);
            }
            if (userDetailRider.body.gallary.img4 != null) {
                Picasso.with(getActivity()).load(new URLS().UPLOAD_URL + userDetailRider.body.gallary.img4).error(R.drawable.image_not_found).into(ivImageFour);
                galleryImages.add(3, new URLS().UPLOAD_URL + userDetailRider.body.gallary.img4);
            } else {
                ivImageFour.setImageResource(R.drawable.image_not_found);
                galleryImages.add(3, null);
            }
            if (userDetailRider.body.gallary.img5 != null) {
                Picasso.with(getActivity()).load(new URLS().UPLOAD_URL + userDetailRider.body.gallary.img5).error(R.drawable.image_not_found).into(ivImageFive);
                galleryImages.add(4, new URLS().UPLOAD_URL + userDetailRider.body.gallary.img5);
            } else {
                ivImageFive.setImageResource(R.drawable.image_not_found);
                galleryImages.add(4, null);
            }
            if (userDetailRider.body.gallary.img6 != null) {
                Picasso.with(getActivity()).load(new URLS().UPLOAD_URL + userDetailRider.body.gallary.img6).error(R.drawable.image_not_found).into(ivImageSix);
                galleryImages.add(5, new URLS().UPLOAD_URL + userDetailRider.body.gallary.img6);
            } else {
                ivImageSix.setImageResource(R.drawable.image_not_found);
                galleryImages.add(5, null);
            }

            if (userDetailRider.body.gender.toLowerCase().equals("male")) {
                cbFemale.setChecked(false);
                cbMale.setChecked(true);
            } else if (userDetailRider.body.gender.toLowerCase().equals("female")) {
                cbFemale.setChecked(true);
                cbMale.setChecked(false);
            }
            etAboutMe.setText(userDetailRider.body.about);
            etEmail.setText(userDetailRider.body.email);
            etLname.setText(userDetailRider.body.lname);
            etCollage.setText(userDetailRider.body.education);
            etProfession.setText(userDetailRider.body.profession);
            etFname.setText(userDetailRider.body.fname);
            etBirthdate.setText(userDetailRider.body.dob);

            for (int i = 0; i < height.length; i++) {
                if (userDetailRider.body.height.equals(height[i])) {
                    npHeight.setValue(i);
                    break;
                }
            }
            if (userDetailRider.body.ethnicity.equals("0") || userDetailRider.body.ethnicity.equals("") || userDetailRider.body.ethnicity == null) {

            } else {
                List<String> listet = Arrays.asList(userDetailRider.body.ethnicity.split("\\s*,\\s*"));

                for (int i = 0; i < listet.size(); i++) {
                    listSelectedEthnicity.add(listet.get(i));
                }
            }

            if (userDetailRider.body.religion.equals("0") || userDetailRider.body.religion.equals("") || userDetailRider.body.religion == null) {

            } else {
                List<String> listrel = Arrays.asList(userDetailRider.body.religion.split("\\s*,\\s*"));
                for (int i = 0; i < listrel.size(); i++) {
                    listSelectedReligion.add(listrel.get(i));
                }
            }
            ethnicityAdapter.addAll(listEthnicity, listSelectedEthnicity);
            religionAdapter.addAll(listReligion, listSelectedReligion);
        }
    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction(Uri uri);
    }
}
