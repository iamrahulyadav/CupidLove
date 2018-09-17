package com.example.cupidlove.activity;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.Crop.Crop;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.model.GalleryImages;
import com.example.cupidlove.utils.BaseSignUpActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;


public class UploadPhotosActivity extends BaseSignUpActivity implements ResponseListener {


    //TODO: Bind The All XML View With JAVA file
    @BindView(R.id.llPhotoSelection)
    LinearLayout llPhotoSelection;

    @BindView(R.id.llImageOne)
    LinearLayout llImageOne;

    @BindView(R.id.llImageTwo)
    LinearLayout llImageTwo;

    @BindView(R.id.llImageThree)
    LinearLayout llImageThree;

    @BindView(R.id.llImageFour)
    LinearLayout llImageFour;

    @BindView(R.id.llImageFive)
    LinearLayout llImageFive;

    @BindView(R.id.llImageSix)
    LinearLayout llImageSix;

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

    @BindView(R.id.civEditOne)
    CircleImageView civEditOne;

    @BindView(R.id.civEditTwo)
    CircleImageView civEditTwo;

    @BindView(R.id.civEditThree)
    CircleImageView civEditThree;

    @BindView(R.id.civEditFour)
    CircleImageView civEditFour;

    @BindView(R.id.civEditFive)
    CircleImageView civEditFive;

    @BindView(R.id.civEditSix)
    CircleImageView civEditSix;

    @BindView(R.id.llGallaryImport)
    LinearLayout llGallaryImport;

    @BindView(R.id.llNext)
    LinearLayout llNext;

    @BindView(R.id.tvInfo)
    TextViewRegular tvInfo;

    @BindView(R.id.llImportData)
    LinearLayout llImportData;

    //TODO : Variable Declartion
    int selectedPosition = 1;
    private Uri mImageCaptureUri;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photos);
        ButterKnife.bind(this);
        setImageView();
    }

    //TODO: resume method
    @Override
    protected void onResume() {
        super.onResume();
    }

    //TODO : Click On Next Button
    @OnClick(R.id.tvNext)
    public void tvNextClick() {
        Intent intent = new Intent(this, UserDatePreferencesActivity.class);
        startActivity(intent);
    }

    //TODO : Select One Image
    @OnClick(R.id.llImageOne)
    public void llImageOneClick() {
        if (count != 0) {
            selectedPosition = 1;
            selectImage();
        }
    }

    //TODO: Setect Second Image
    @OnClick(R.id.llImageTwo)
    public void llImageTwoClick() {
        if (count != 0) {
            selectedPosition = 2;
            selectImage();
        }
    }

    //TODO: Select Third Image
    @OnClick(R.id.llImageThree)
    public void llImageThreeClick() {
        if (count != 0) {
            selectedPosition = 3;
            selectImage();
        }
    }

    //TODO : Select Forth Image
    @OnClick(R.id.llImageFour)
    public void llImageFourClick() {
        if (count != 0) {
            selectedPosition = 4;
            selectImage();
        }
    }

    //TODO : Select Fifth Image
    @OnClick(R.id.llImageFive)
    public void llImageFiveClick() {
        if (count != 0) {
            selectedPosition = 5;
            selectImage();
        }
    }

    //TODO : Select Sixth Image
    @OnClick(R.id.llImageSix)
    public void llImageSixClick() {
        if (count != 0) {
            selectedPosition = 6;
            selectImage();
        }
    }

    //TODO : Import Image With FaceBook
    @OnClick(R.id.llFbImport)
    public void llFbImportClick() {


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
                Uri photoUri = FileProvider.getUriForFile(UploadPhotosActivity.this, getPackageName() + ".provider", file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            }

            try {
                intent.putExtra("return-data", true);
                startActivityForResult(intent, 0);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
            }
        }

    }

    //TODO : Imaport Image with Instagram
    @OnClick(R.id.llInstaImport)
    public void llInstaImportClick() {
        Intent intent = new Intent(this, InstagramImagesActivity.class);
        intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
        startActivity(intent);
    }

    //TODO : Import Image with Gallary
    @OnClick(R.id.llGallaryImport)
    public void llGallaryImportClick() {
        if (mayRequestPermissionForExternalStorage()) {
            Intent intent = new Intent(this, GalleryActivity.class);
            intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, 1);
            startActivityForResult(intent, 1);
        }
    }

    //TODO: Select Image Mehtod For All Constrain
    private void selectImage() {
        final CharSequence[] items = {"Gallery", "Take Photo",
                "Cancel"};

        TextViewRegular title = new TextViewRegular(this);
        title.setText("Add Photo");
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setPadding(10, 25, 10, 25);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(22);


        AlertDialog.Builder builder = new AlertDialog.Builder(UploadPhotosActivity.this);

        builder.setCustomTitle(title);
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Facebook")) {

                    //get image from fb
                    Intent intent = new Intent(UploadPhotosActivity.this, FacebookImagesActivity.class);
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
                            Uri photoUri = FileProvider.getUriForFile(UploadPhotosActivity.this, getPackageName() + ".provider", file);
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
                        Intent intent = new Intent(UploadPhotosActivity.this, GalleryActivity.class);
                        intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
                        startActivityForResult(intent, 1);
                    }


                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }


//                else if (items[item].equals("Instagram")) {
//
//                    //Intagram
//                    Intent intent = new Intent(UploadPhotosActivity.this, InstagramImagesActivity.class);
//                    intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
//                    startActivity(intent);
//
//                }
            }
        });
        builder.show();
    }

    //TODO: Get The Permission to Access the Camera
    private boolean mayRequestPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if ((checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE}, 1212);
            return false;
        }
    }

    //TODO : Get The Permission For Access The External Storage
    private boolean mayRequestPermissionForExternalStorage() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            return false;
        }
    }

    //TODO : Permission Result Store
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1212) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                mImageCaptureUri = Uri.fromFile(new File(Environment
                        .getExternalStorageDirectory(), "tmp_avatar_"
                        + String.valueOf(System.currentTimeMillis())
                        + ".jpg"));

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                } else {
                    File file = new File(mImageCaptureUri.getPath());
                    Uri photoUri = FileProvider.getUriForFile(UploadPhotosActivity.this, getPackageName() + ".provider", file);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                }

                try {
                    intent.putExtra("return-data", true);
                    startActivityForResult(intent, 0);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, getResources().getString(R.string.allow_permission_to_access_camera), Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Intent intent = new Intent(UploadPhotosActivity.this, GalleryActivity.class);
                intent.putExtra(RequestParamUtils.SELECTED_IMAGE_POSITION, selectedPosition);
                startActivityForResult(intent, 1);

            } else {
                Toast.makeText(this, getResources().getString(R.string.allow_permission_to_access_gallery), Toast.LENGTH_SHORT).show();
            }
        }
    }

    //TODO: when capture image by user at that time display activity result
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        Uri selectedImage = null;
        switch (requestCode) {
            case 0:
                //Camera
                if (resultCode == RESULT_OK) {
                    beginCrop(mImageCaptureUri);
                }
                break;
            case 1:
                //Gallery
                if (resultCode == RESULT_OK) {
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
                            photo = MediaStore.Images.Media.getBitmap(getContentResolver(), Crop.getOutput(imageReturnedIntent));
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
                    count = count + 1;
                }

                break;


        }

        if (count != 0) {
            llImportData.setVisibility(View.GONE);
            llNext.setVisibility(View.VISIBLE);
            civEditOne.setVisibility(View.VISIBLE);
            civEditTwo.setVisibility(View.VISIBLE);
            civEditThree.setVisibility(View.VISIBLE);
            civEditFour.setVisibility(View.VISIBLE);
            civEditFive.setVisibility(View.VISIBLE);
            civEditSix.setVisibility(View.VISIBLE);
            tvInfo.setText(getResources().getString(R.string.tap_to_change));

            if (count == 1) {
                ivImageTwo.setImageResource(R.drawable.image_not_found);
                ivImageThree.setImageResource(R.drawable.image_not_found);
                ivImageFour.setImageResource(R.drawable.image_not_found);
                ivImageFive.setImageResource(R.drawable.image_not_found);
                ivImageSix.setImageResource(R.drawable.image_not_found);
            }

        } else {
            llImportData.setVisibility(View.VISIBLE);
            llNext.setVisibility(View.GONE);
            civEditOne.setVisibility(View.GONE);
            civEditTwo.setVisibility(View.GONE);
            civEditThree.setVisibility(View.GONE);
            civEditFour.setVisibility(View.GONE);
            civEditFive.setVisibility(View.GONE);
            civEditSix.setVisibility(View.GONE);
        }
    }

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
        Crop.of(source, destination).withAspect(1, 1).start(this);
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


    //TODO : ADD The Capture Image in Gallary
    public void editGalleryImages(String key, String imageFile) {

        try {
            showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));

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
            asyncHttpClient.post(new URLS().EDIT_GALLERY_IMAGES, params, new ResponseHandler(this, this, "edit_gallery_images"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage() + " exception");
        }
    }

    //TODO: Responce of all api
    @Override
    public void onResponse(String response, String methodName) {

        dismissProgress();

        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("edit_gallery_images")) {

            //get all static data
            Log.e("response", response);

            try {
                final GalleryImages galleryImagesRider = new Gson().fromJson(
                        response, new TypeToken<GalleryImages>() {
                        }.getType());

                if (galleryImagesRider.error) {
                    //error
                    Toast.makeText(this, galleryImagesRider.message, Toast.LENGTH_SHORT).show();
                } else {
                    //updated images
                    SharedPreferences.Editor pre = getPreferences().edit();
                    pre.putString(RequestParamUtils.REGISTER, "registeruserdatepreference");

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


                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

}