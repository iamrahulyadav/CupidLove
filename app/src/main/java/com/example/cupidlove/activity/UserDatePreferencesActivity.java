package com.example.cupidlove.activity;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.customview.edittext.EditTextRegular;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.utils.BaseSignUpActivity;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserDatePreferencesActivity extends BaseSignUpActivity implements ResponseListener, View.OnTouchListener, View.OnDragListener, View.OnClickListener {

    //TODO: Bind The All XML View With JAVA File

    @BindView(R.id.civCoffee)
    ImageView civCoffee;

    @BindView(R.id.civDrink)
    ImageView civDrink;

    @BindView(R.id.civFood)
    ImageView civFood;

    @BindView(R.id.civFun)
    ImageView civFun;

    @BindView(R.id.civOne)
    ImageView civOne;

    @BindView(R.id.civTwo)
    ImageView civTwo;

    @BindView(R.id.civThree)
    ImageView civThree;

    @BindView(R.id.civFour)
    ImageView civFour;

    @BindView(R.id.tvOne)
    TextViewRegular tvOne;

    @BindView(R.id.tvTwo)
    TextViewRegular tvTwo;

    @BindView(R.id.tvThree)
    TextViewRegular tvThree;

    @BindView(R.id.tvFour)
    TextViewRegular tvFour;

    @BindView(R.id.tvCoffee)
    TextViewRegular tvCoffee;

    @BindView(R.id.tvDrink)
    TextViewRegular tvDrink;

    @BindView(R.id.tvFood)
    TextViewRegular tvFood;

    @BindView(R.id.tvFun)
    TextViewRegular tvFun;

    @BindView(R.id.ivtmpOne)
    ImageView ivtmpOne;

    @BindView(R.id.ivtmpTwo)
    ImageView ivtmpTwo;

    @BindView(R.id.ivtmpThree)
    ImageView ivtmpThree;

    @BindView(R.id.ivtmpFour)
    ImageView ivtmpFour;

    @BindView(R.id.etAboutMe)
    EditTextRegular etAboutMe;

    //TODO : Varibla Declaration

    private String strTargetImage1 = "", strTargetImage2 = "", strTargetImage3 = "", strTargetImage4 = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_date_preferences);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        ButterKnife.bind(this);
        setScreenLayoutDirection();
        settvTitle(getResources().getString(R.string.date_preference));

        hideSearch();
        hideMenu();
        setTouchListener();

        setPreferencesView();
    }


    public void setPreferencesView() {
//        final FrameLayout.LayoutParams paramsImagePhoto = (FrameLayout.LayoutParams) civOne.getLayoutParams();
//        civOne.post(new Runnable() {
//            @Override
//            public void run() {
//                paramsImagePhoto.height = civOne.getWidth();
//                civOne.setLayoutParams(paramsImagePhoto);
//            }
//        });
//
//        final LinearLayout.LayoutParams paramsImagePhoto1 = (LinearLayout.LayoutParams) civCoffee.getLayoutParams();
//        civOne.post(new Runnable() {
//            @Override
//            public void run() {
//                paramsImagePhoto1.height = civCoffee.getWidth();
//                civCoffee.setLayoutParams(paramsImagePhoto1);
//            }
//        });
    }

    //TODO: Select Any Preferred Option
    @OnClick(R.id.tvDone)
    public void tvDoneClick() {

        checkData();
    }

    //TODO : Check Data
    public void checkData() {

        if (tvOne.getText().toString().equals("")) {
            Toast.makeText(this, getResources().getString(R.string.drag_arrange_prefered_dating_option), Toast.LENGTH_SHORT).show();
        } else {
            if (tvTwo.getText().toString().equals("")) {
                Toast.makeText(this, getResources().getString(R.string.drag_arrange_prefered_dating_option), Toast.LENGTH_SHORT).show();
            } else {
                if (tvThree.getText().toString().equals("")) {
                    Toast.makeText(this, getResources().getString(R.string.drag_arrange_prefered_dating_option), Toast.LENGTH_SHORT).show();
                } else {
                    if (tvFour.getText().toString().equals("")) {
                        Toast.makeText(this, getResources().getString(R.string.drag_arrange_prefered_dating_option), Toast.LENGTH_SHORT).show();
                    } else {
                        //all preferences are proper
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

                        if (etAboutMe.getText().toString().isEmpty()) {
                            Toast.makeText(this, getResources().getString(R.string.write_about_you), Toast.LENGTH_SHORT).show();
                        } else {
                            userPrefencesUpdate(str);
//                            Intent intent = new Intent(this, DiscoveryPreferencesActivity.class);
//                            startActivity(intent);
                        }
                    }
                }
            }
        }
    }

    //TODO: Set image touch listner
    public void setTouchListener() {
        civCoffee.setOnTouchListener(this);
        civDrink.setOnTouchListener(this);
        civFood.setOnTouchListener(this);
        civFun.setOnTouchListener(this);

        civCoffee.setOnDragListener(this);
        civDrink.setOnDragListener(this);
        civFood.setOnDragListener(this);
        civFun.setOnDragListener(this);

        civOne.setOnTouchListener(this);
        civTwo.setOnTouchListener(this);
        civThree.setOnTouchListener(this);
        civFour.setOnTouchListener(this);

        civOne.setOnDragListener(this);
        civTwo.setOnDragListener(this);
        civThree.setOnDragListener(this);
        civFour.setOnDragListener(this);

    }

    //TODO: generate touch event
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

    //TODO : Drage the Date Preferences
    @Override
    public boolean onDrag(final View targetView, DragEvent event) {
        int dragId = 0, dropId = 0;
        etAboutMe.setEnabled(false);
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

                ImageView target = (ImageView) targetView;
                ImageView dragged = (ImageView) event.getLocalState();
                boolean swipeText = true;
                dragId = dragged.getId();
                dropId = target.getId();
                Log.i("Target id", "" + target.getId());
                if (target.getId() != civCoffee.getId() && target.getId() != civFun.getId() && target.getId() != civFood.getId() && target.getId() != civDrink.getId()) {

                    boolean b = checkTarget(target);

                    if (b == true) {
                        checkIsAbletoDrop(dragged, target);
                    }

                    Drawable target_draw = target.getDrawable();
                    Drawable dragged_draw = dragged.getDrawable();
                    dragged.setImageDrawable(target_draw);
                    target.setImageDrawable(dragged_draw);
                    target.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                if (swipeText ) {

//                    Drawable target_draw = target.getDrawable();
//                    Drawable dragged_draw = dragged.getDrawable();

                    if (dragged.getId() == civCoffee.getId()) {
                        //Drop to One
                        ImageView container = (ImageView) targetView;

                        if (target.getId() == civOne.getId()) {
                            setOriginalImagew(tvOne.getText().toString());
                            setDraggedImagew(tvOne, ivtmpOne, getResources().getString(R.string.coffee), container);
                            tvCoffee.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civTwo.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            setDraggedImagew(tvTwo, ivtmpTwo, getResources().getString(R.string.coffee), container);
                            tvCoffee.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civThree.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            setDraggedImagew(tvThree, ivtmpThree, getResources().getString(R.string.coffee), container);
                            tvCoffee.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civFour.getId()) {
                            setOriginalImagew(tvFour.getText().toString());
                            setDraggedImagew(tvFour, ivtmpFour, getResources().getString(R.string.coffee), container);
                            tvCoffee.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        }
                    } else if (dragged.getId() == civDrink.getId()) {
                        ImageView container = (ImageView) targetView;
                        if (target.getId() == civOne.getId()) {
                            setOriginalImagew(tvOne.getText().toString());
                            setDraggedImagew(tvOne, ivtmpOne, tvDrink.getText().toString(), container);
                            tvDrink.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civTwo.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            setDraggedImagew(tvTwo, ivtmpTwo, tvDrink.getText().toString(), container);
                            tvDrink.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civThree.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            setDraggedImagew(tvThree, ivtmpThree, tvDrink.getText().toString(), container);
                            tvDrink.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civFour.getId()) {
                            setOriginalImagew(tvFour.getText().toString());
                            setDraggedImagew(tvFour, ivtmpFour, tvDrink.getText().toString(), container);
                            tvDrink.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        }
                    } else if (dragged.getId() == civFood.getId()) {
                        ImageView container = (ImageView) targetView;
                        if (target.getId() == civOne.getId()) {
                            setOriginalImagew(tvOne.getText().toString());
                            setDraggedImagew(tvOne, ivtmpOne, tvFood.getText().toString(), container);
                            tvFood.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civTwo.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            setDraggedImagew(tvTwo, ivtmpTwo, tvFood.getText().toString(), container);
                            tvFood.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civThree.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            setDraggedImagew(tvThree, ivtmpThree, tvFood.getText().toString(), container);
                            tvFood.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civFour.getId()) {
                            setOriginalImagew(tvFour.getText().toString());
                            setDraggedImagew(tvFour, ivtmpFour, tvFood.getText().toString(), container);
                            tvFood.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        }
                    } else if (dragged.getId() == civFun.getId()) {
                        ImageView container = (ImageView) targetView;
                        if (target.getId() == civOne.getId()) {
                            setOriginalImagew(tvOne.getText().toString());
                            setDraggedImagew(tvOne, ivtmpOne, tvFun.getText().toString(), container);
                            tvFun.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civTwo.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            setDraggedImagew(tvTwo, ivtmpTwo, tvFun.getText().toString(), container);
                            tvFun.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civThree.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            setDraggedImagew(tvThree, ivtmpThree, tvFun.getText().toString(), container);
                            tvFun.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        } else if (target.getId() == civFour.getId()) {
                            setOriginalImagew(tvFour.getText().toString());
                            setDraggedImagew(tvFour, ivtmpFour, tvFun.getText().toString(), container);
                            tvFun.setVisibility(View.INVISIBLE);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                        }
                    }

                    if (dragged.getId() == civOne.getId()) {
                        Log.e("Coffee Target  ", "Called");

                        if (target.getId() == civCoffee.getId() || target.getId() == civDrink.getId() || target.getId() == civFood.getId() || target.getId() == civFun.getId()) {
                            setOriginalImagew(tvOne.getText().toString());
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                            hideTv(tvOne, ivtmpOne);

                        } else if (target.getId() == civTwo.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            setDraggedImagew(tvTwo, ivtmpTwo, tvOne.getText().toString(), civTwo);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);
                            hideTv(tvOne, ivtmpOne);

                        } else if (target.getId() == civThree.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            setDraggedImagew(tvThree, ivtmpThree, tvOne.getText().toString(), civThree);
                            hideTv(tvOne, ivtmpOne);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);

                        } else if (target.getId() == civFour.getId()) {
                            setOriginalImagew(tvFour.getText().toString());
                            setDraggedImagew(tvFour, ivtmpFour, tvOne.getText().toString(), civFour);

                            hideTv(tvOne, ivtmpOne);
                            dragged.setImageDrawable(null);
                            dragged.setBackgroundResource(0);

                        }
                    } else if (dragged.getId() == civTwo.getId()) {
                        if (target.getId() == civCoffee.getId() || target.getId() == civDrink.getId() || target.getId() == civFood.getId() || target.getId() == civFun.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            hideTv(tvTwo, ivtmpTwo);

                        } else if (target.getId() == civOne.getId()) {
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            setOriginalImagew(tvOne.getText().toString());
                            setDraggedImagew(tvOne, ivtmpOne, tvTwo.getText().toString(), civOne);
                            hideTv(tvTwo, ivtmpTwo);

                        } else if (target.getId() == civThree.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            setDraggedImagew(tvThree, ivtmpThree, tvTwo.getText().toString(), civThree);
                            hideTv(tvTwo, ivtmpTwo);
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);

                        } else if (target.getId() == civFour.getId()) {
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            setOriginalImagew(tvFour.getText().toString());
                            setDraggedImagew(tvFour, ivtmpFour, tvTwo.getText().toString(), civFour);
                            hideTv(tvTwo, ivtmpTwo);

                        }
                    } else if (dragged.getId() == civThree.getId()) {
                        if (target.getId() == civCoffee.getId() || target.getId() == civDrink.getId() || target.getId() == civFood.getId() || target.getId() == civFun.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            hideTv(tvThree, ivtmpThree);

                        } else if (target.getId() == civOne.getId()) {
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            setOriginalImagew(tvOne.getText().toString());
                            setDraggedImagew(tvOne, ivtmpOne, tvThree.getText().toString(), civOne);
                            hideTv(tvThree, ivtmpThree);

                        } else if (target.getId() == civTwo.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            setDraggedImagew(tvTwo, ivtmpTwo, tvThree.getText().toString(), civTwo);
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            hideTv(tvThree, ivtmpThree);

                        } else if (target.getId() == civFour.getId()) {
                            setOriginalImagew(tvFour.getText().toString());
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            setDraggedImagew(tvFour, ivtmpFour, tvThree.getText().toString(), civFour);
                            hideTv(tvThree, ivtmpThree);

                        }
                    } else if (dragged.getId() == civFour.getId()) {
                        if (target.getId() == civCoffee.getId() || target.getId() == civDrink.getId() || target.getId() == civFood.getId() || target.getId() == civFun.getId()) {
                            setOriginalImagew(tvFour.getText().toString());
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            hideTv(tvFour, ivtmpFour);

                        } else if (target.getId() == civOne.getId()) {
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            setOriginalImagew(tvOne.getText().toString());
                            setDraggedImagew(tvOne, ivtmpOne, tvFour.getText().toString(), civOne);
                            hideTv(tvFour, ivtmpFour);

                        } else if (target.getId() == civTwo.getId()) {
                            setOriginalImagew(tvTwo.getText().toString());
                            setDraggedImagew(tvTwo, ivtmpTwo, tvFour.getText().toString(), civTwo);
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            hideTv(tvFour, ivtmpFour);

                        } else if (target.getId() == civThree.getId()) {
                            setOriginalImagew(tvThree.getText().toString());
                            setDraggedImagew(tvThree, ivtmpThree, tvFour.getText().toString(), civThree);
                            dragged.setBackgroundResource(0);
                            dragged.setImageDrawable(null);
                            hideTv(tvFour, ivtmpFour);

                        }
                    }
                }
                view.setVisibility(View.VISIBLE);

                break;

            case DragEvent.ACTION_DRAG_ENDED:
                etAboutMe.setEnabled(true);
                Log.e(null, "Drag ended");
                //Toast.makeText(this, "end draged", Toast.LENGTH_SHORT).show();
                view.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
        return true;
    }

    //TODO : SET The Dragged Image View
    public void setDraggedImagew(TextViewRegular tv, ImageView iv, String text, ImageView ImageView) {
        tv.setVisibility(View.VISIBLE);
        iv.setVisibility(View.INVISIBLE);
        tv.setText(text);
        if (text.equals(getResources().getString(R.string.coffee))) {
            ImageView.setBackgroundResource(R.drawable.ic_coffee);
        } else if (text.equals(getResources().getString(R.string.drink))) {
            ImageView.setBackgroundResource(R.drawable.ic_drink);
        } else if (text.equals(getResources().getString(R.string.food))) {
            ImageView.setBackgroundResource(R.drawable.ic_food);
        } else if (text.equals(getResources().getString(R.string.fun))) {
            ImageView.setBackgroundResource(R.drawable.ic_fun);
        }

    }


    //TODO : Set Original Image View
    public void setOriginalImagew(String text) {
        if (text.equals(getResources().getString(R.string.coffee))) {
            civCoffee.setBackgroundResource(R.drawable.ic_coffee);
            tvCoffee.setText(getResources().getString(R.string.coffee));
            tvCoffee.setVisibility(View.VISIBLE);
        } else if (text.equals(getResources().getString(R.string.drink))) {
            civDrink.setBackgroundResource(R.drawable.ic_drink);
            tvDrink.setText(getResources().getString(R.string.drink));
            tvDrink.setVisibility(View.VISIBLE);
        } else if (text.equals(getResources().getString(R.string.food))) {
            civFood.setBackgroundResource(R.drawable.ic_food);
            tvFood.setText(getResources().getString(R.string.food));
            tvFood.setVisibility(View.VISIBLE);
        } else if (text.equals(getResources().getString(R.string.fun))) {
            civFun.setBackgroundResource(R.drawable.ic_fun);
            tvFun.setText(getResources().getString(R.string.fun));
            tvFun.setVisibility(View.VISIBLE);
        }
    }

    //TODO : Hide Text View
    public void hideTv(TextViewRegular tv, ImageView iv) {
        if (tv.getId() == tvOne.getId()) {
            tvOne.setVisibility(View.INVISIBLE);
            tvOne.setText("");
            ivtmpOne.setVisibility(View.VISIBLE);
        } else if (tv.getId() == tvTwo.getId()) {
            tvTwo.setVisibility(View.INVISIBLE);
            tvTwo.setText("");
            ivtmpTwo.setVisibility(View.VISIBLE);
        } else if (tv.getId() == tvThree.getId()) {
            tvThree.setVisibility(View.INVISIBLE);
            tvThree.setText("");
            ivtmpThree.setVisibility(View.VISIBLE);
        } else if (tv.getId() == tvFour.getId()) {
            tvFour.setVisibility(View.INVISIBLE);
            tvFour.setText("");
            ivtmpFour.setVisibility(View.VISIBLE);
        }
    }

    //TODO: show textview
    public void showTvBottom(TextViewRegular tv, String text) {
        if (tv.getId() == tvCoffee.getId()) {
            tvCoffee.setVisibility(View.VISIBLE);
            tvCoffee.setText(text);
        } else if (tv.getId() == tvDrink.getId()) {
            tvDrink.setVisibility(View.VISIBLE);
            tvDrink.setText(text);
        } else if (tv.getId() == tvFood.getId()) {
            tvFood.setVisibility(View.VISIBLE);
            tvFood.setText(text);
        } else if (tv.getId() == tvFun.getId()) {
            tvFun.setVisibility(View.VISIBLE);
            tvFun.setText(text);
        }

    }

    //TODO: check dreg and drop image id
    private void checkIsAbletoDrop(ImageView dragged, ImageView target) {
        boolean b = false;
        if (dragged.getId() == civCoffee.getId())
            b = true;
        if (dragged.getId() == civDrink.getId())
            b = true;
        if (dragged.getId() == civFood.getId())
            b = true;
        if (dragged.getId() == civFun.getId())
            b = true;

        if (b)
            targetImage(target);
    }

    //TODO : Set The Target Image View
    private void targetImage(ImageView target) {
        if (target.getId() == R.id.civOne) {
            checkString(strTargetImage1, target);
        } else if (target.getId() == R.id.civTwo) {
            checkString(strTargetImage2, target);
        } else if (target.getId() == R.id.civThree) {
            checkString(strTargetImage3, target);
        } else if (target.getId() == R.id.civFour) {
            checkString(strTargetImage4, target);
        }

    }

    //TODO : Check The String Of Date Preferences
    private void checkString(String str, ImageView target) {
        if (str.equals(getResources().getString(R.string.coffee))) {
            setSwap(target, civCoffee);
        } else if (str.equals(getResources().getString(R.string.drink))) {
            setSwap(target, civDrink);
        } else if (str.equals(getResources().getString(R.string.food))) {
            setSwap(target, civFood);
        } else if (str.equals(getResources().getString(R.string.fun))) {
            setSwap(target, civFun);
        }


    }

    //TODO : Set The Swap View Of The Image
    private void setSwap(ImageView dragged, ImageView target) {

        Drawable target_draw = target.getDrawable();
        Drawable dragged_draw = dragged.getDrawable();
        dragged.setImageDrawable(target_draw);
        target.setImageDrawable(dragged_draw);

        if (target.getId() == civCoffee.getId()) {
            ((TextViewRegular) findViewById(R.id.tvCoffee)).setVisibility(View.VISIBLE);
        } else if (target.getId() == civDrink.getId()) {
            ((TextViewRegular) findViewById(R.id.tvDrink)).setVisibility(View.VISIBLE);
        } else if (target.getId() == civFood.getId()) {
            ((TextViewRegular) findViewById(R.id.tvFood)).setVisibility(View.VISIBLE);
        } else if (target.getId() == civFun.getId()) {
            ((TextViewRegular) findViewById(R.id.tvFun)).setVisibility(View.VISIBLE);
        }
    }

    //TODO : Set the Target Image
    private boolean checkTarget(ImageView target) {
        if (target.getId() == R.id.civOne) {
            boolean b = checknullornott(strTargetImage1);
            return b;
        } else if (target.getId() == R.id.civTwo) {
            boolean b = checknullornott(strTargetImage2);
            return b;
        } else if (target.getId() == R.id.civThree) {
            boolean b = checknullornott(strTargetImage3);
            return b;
        } else if (target.getId() == R.id.civFour) {
            boolean b = checknullornott(strTargetImage4);
            return b;
        } else
            return false;
    }

    //TODO : Check The Image Value is Null Or Not
    private boolean checknullornott(String straTargetImage1) {

        if (straTargetImage1.equals(""))
            return false;
        else
            return true;
    }

    @Override
    public void onClick(View v) {

    }


    //TODO : Update The User Date Preferance
    public void userPrefencesUpdate(String pref) {

        try {
            showProgress("");

            SharedPreferences.Editor pre = getPreferences().edit();

            RequestParams params = new RequestParams();
            params.put("AuthToken", getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("about", etAboutMe.getText().toString());
            pre.putString(RequestParamUtils.ABOUT, etAboutMe.getText().toString());

            params.put("date_pref", pref);
            pre.putString(RequestParamUtils.DATE_PREF, pref);

            params.put("ethnicity", getPreferences().getString(RequestParamUtils.ETHNICITY, ""));
            params.put("height", getPreferences().getString(RequestParamUtils.HEIGHT, ""));
            params.put("kids", getPreferences().getString(RequestParamUtils.KIDS, ""));
            params.put("que_ans", getPreferences().getString(RequestParamUtils.QUE_ANS, ""));
            params.put("que_id", getPreferences().getString(RequestParamUtils.QUE_ID, ""));
            params.put("religion", getPreferences().getString(RequestParamUtils.RELIGION, ""));

            params.put("gender_pref", "male");
            params.put("max_age_pref", "60");
            params.put("max_dist_pref", "200");
            params.put("min_age_pref", "18");
            params.put("min_dist_pref", "0");

            pre.commit();

            Debug.e("acceptOrRejectActivity", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            asyncHttpClient.post(new URLS().USER_PREFERENCE_UPDATE, params, new ResponseHandler(this, this, "userPrefencesUpdate"));
        } catch (Exception e) {
            Debug.e("acceptOrRejectActivity Exception", e.getMessage());
        }
    }

    //TODO : Set The Response When Detail Updated
    public void onResponse(String response, String methodName) {
        dismissProgress();

        Log.e("response", response);
        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("userPrefencesUpdate")) {
            try{
                JSONObject jsonObject = new JSONObject(response);
                if (!jsonObject.getBoolean("error")) {
                    //done

                    SharedPreferences.Editor pre = getPreferences().edit();
                    pre.putString(RequestParamUtils.REGISTER, "registerdiscoverypreference");
                    pre.commit();

                    Toast.makeText(this, getResources().getString(R.string.info_updated_successfully), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, DiscoveryPreferencesActivity.class);
                    startActivity(intent);

                } else {
                    Toast.makeText(this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }

}
