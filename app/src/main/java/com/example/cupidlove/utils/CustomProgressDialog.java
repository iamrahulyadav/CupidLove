package com.example.cupidlove.utils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.example.cupidlove.R;

/**
 * Created by Bhumi Shah on 11/8/2017.
 */

public class CustomProgressDialog {

    //TODO : VAriable Declaration
    public Activity context;
    ImageView iv1;
    public Dialog dialogView;
    public static CustomProgressDialog customProgressDialog;
    AnimationDrawable Anim;


    public static CustomProgressDialog getCustomProgressDialog(Activity context) {
        if (customProgressDialog == null) {
            customProgressDialog = new CustomProgressDialog(context);
        }
        return customProgressDialog;
    }


    public CustomProgressDialog(Activity context) {
        this.context = context;
    }

//    public void setbimap() {
//        new ConvertinBitmap().execute();
//    }


//    class ConvertinBitmap extends AsyncTask<Void, Void, Void> {
//
//        @Override
//        protected Void doInBackground(Void... voids) {
//            try {
//                for (int i = 0; i < Constant.progressDrawble.length; i++) {
//                    BitmapDrawable frame = (BitmapDrawable) context.getResources().getDrawable(Constant.progressDrawble[i]);
//                    Constant.bitmapDrawablesList.add(frame);
//
//                }
//            } catch (Exception e) {
//                Log.e("Exception is ", e.getMessage());
//
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            super.onPostExecute(aVoid);
//            Log.e("On Post ", "Called");
//            showCustomDialog(context);
//
//        }
//    }

//    public void showCustomDialog(Context context) {
//        Anim = new AnimationDrawable();
//        dialogView = new Dialog(context, android.R.style.DeviceDefault_Light_ButtonBar);
//        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialogView.setContentView(R.layout.layout_progress_dialog);
//
//        iv1 = (ImageView) dialogView.findViewById(R.id.iv1);
//        try {
//            for (int i = 0; i < Constant.progressDrawble.length; i++) {
//                Anim.addFrame(Constant.bitmapDrawablesList.get(i), 50);
//
//            }
//            for (int i = 0; i < Constant.progressDrawble.length; i++) {
//                Anim.addFrame(Constant.bitmapDrawablesList.get(i), 50);
//
//            }
//
//            Anim.setOneShot(false);
//            iv1.setBackgroundDrawable(Anim);
//            final Handler handler = new Handler();
//            handler.postDelayed(new Runnable() {
//                public void run() {
//
//                    Anim.start();
//                }
//
//            }, 50);
//
//        } catch (Exception e) {
//            Log.e("Exception is ", e.getMessage() + " ");
//        }
//
//        dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
//        dialogView.show();
//    }

    //TODO : Display Custom Dialog
        public void showCustomDialog() {
        dialogView = new Dialog(context, android.R.style.DeviceDefault_Light_ButtonBar);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogView.setContentView(R.layout.layout_progress_dialog);

        iv1 = (ImageView) dialogView.findViewById(R.id.iv1);
            GlideDrawableImageViewTarget ivSmily = new GlideDrawableImageViewTarget(iv1);
          if(!context.isDestroyed()) {
              Glide.with(context).load(R.raw.cupid_loder).into(ivSmily);
              dialogView.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
              dialogView.show();
          }


    }

    //TODO : Dissmiss Dialog
    public void dissmissDialog() {
        try {
            if (dialogView.isShowing()  && dialogView!=null) {
                dialogView.dismiss();
            }
        } catch (Exception e) {
            Log.e("Exception is ", e.getMessage());
        }
    }
}
