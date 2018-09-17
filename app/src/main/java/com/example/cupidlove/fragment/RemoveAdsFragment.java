package com.example.cupidlove.fragment;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.vending.billing.IInAppBillingService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.example.cupidlove.R;
import com.example.cupidlove.activity.HomeActivity;
import com.example.cupidlove.customview.textview.TextViewRegular;
import com.example.cupidlove.utils.BaseActivity;
import com.example.cupidlove.utils.Config;
import com.example.cupidlove.utils.RequestParamUtils;
import com.example.cupidlove.utils.URLS;
import com.example.cupidlove.utils.apicall.AsyncHttpRequest;
import com.example.cupidlove.utils.apicall.Debug;
import com.example.cupidlove.utils.apicall.ResponseHandler;
import com.example.cupidlove.utils.apicall.ResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kaushal on 29-12-2017.
 */

public class RemoveAdsFragment extends Fragment implements ResponseListener, PurchasesUpdatedListener {


    //TODO : Bind All XML View With JAVA file
    @BindView(R.id.tvPurchase)
    TextViewRegular tvPurchase;

    //TODO : Variable Declaration
    View view;
    LayoutInflater inflater;
    ViewGroup container;
    ArrayList<String> skuList = new ArrayList<String>();
    private Bundle querySkus;

    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    private String mPremiumUpgradePrice;

    Boolean ad = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_remove_ads, container, false);
        ButterKnife.bind(this, view);
        ((BaseActivity) getActivity()).settvTitle(getResources().getString(R.string.remove_ads));
        ((BaseActivity) getActivity()).hideSearch();
        ((HomeActivity) getActivity()).adView.setVisibility(View.GONE);


        this.inflater = inflater;
        this.container = container;
        return view;
    }

    //TODO : Purchase Click
    @OnClick(R.id.tvPurchase)
    public void tvPurchaseClick() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        // Setting Dialog Title
        alertDialog.setTitle(getActivity().getResources().getString(R.string.remove_ads));
        // Setting Dialog Message
        alertDialog.setMessage(getActivity().getResources().getString(R.string.all_ads_Removed));
        // Setting Icon to Dialog
//        alertDialog.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton(getActivity().getResources().getString(R.string.hint_ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int which) {
                // Write your code here to invoke YES event
                removeAds();
            }
        });

        // Setting Negative "NO" Button
        alertDialog.setNegativeButton(getActivity().getResources().getString(R.string.hint_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to invoke NO event
                dialog.cancel();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }


    //TODO : Remove Ads
    private void removeAds() {
        String in_app_purchase = ((BaseActivity) getActivity()).getPreferences().getString(Config.INAPPPURCHASE, "");
        skuList.add(in_app_purchase);
//        skuList.add("gas");
        querySkus = new Bundle();
        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);

                try {
                    if (mService != null) {
                        Bundle skuDetails;// = mService.getSkuDetails(5, getActivity().getPackageName(), "inapp", querySkus);

                        Bundle querySkus = new Bundle();
                        querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

                        try {
                            skuDetails = mService.getSkuDetails(3, getActivity().getPackageName(), "inapp", querySkus);
                            Log.e("get", "getSkuDetails() - success return Bundle");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            Log.e("get", "getSkuDetails() - fail!");
                            return;
                        }

                        int response1 = skuDetails.getInt("RESPONSE_CODE");
                        Log.e("get", "getSkuDetails() - \"RESPONSE_CODE\" return " + String.valueOf(response1));

                        if (response1 != 0) return;


                        int response = skuDetails.getInt("RESPONSE_CODE");
                        if (response == 0) {
                            ArrayList<String> responseList = skuDetails.getStringArrayList("DETAILS_LIST");

                            for (String thisResponse : responseList) {
                                try {
                                    JSONObject object = new JSONObject(thisResponse);

                                    String sku = object.getString("productId");
                                    String title = object.getString("title");
                                    String price = object.getString("price");

                                    Log.i("log", "getSkuDetails() - \"DETAILS_LIST\":\"productId\" return " + sku);
                                    Log.i("log", "getSkuDetails() - \"DETAILS_LIST\":\"title\" return " + title);
                                    Log.i("log", "getSkuDetails() - \"DETAILS_LIST\":\"price\" return " + price);

//                                    if (!sku.equals(productID)) continue;

                                    Bundle buyIntentBundle = mService.getBuyIntent(3, getActivity().getPackageName(), sku, "inapp", "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ");

                                    Log.i("tag", "getBuyIntent() - success return Bundle");

                                    response = buyIntentBundle.getInt("RESPONSE_CODE");
                                    Log.i("tag", "getBuyIntent() - \"RESPONSE_CODE\" return " + String.valueOf(response));

//                                    if (response != 0)
//                                    {
//                                        if(sku.equals("com.potenza.cupidlove.removeads") && !ad) {
//                                            enableAd();
//                                        }
//                                        continue;
//                                    }

                                    PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
                                    getActivity().startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), 0, 0, 0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                } catch (RemoteException e) {
                                    e.printStackTrace();
                                    Log.w("tag", "getBuyIntent() - fail!");
                                } catch (IntentSender.SendIntentException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mService = null;
            }
        };

        Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");

        getActivity().bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);

    }


    @Override
    public void onPurchasesUpdated(@BillingClient.BillingResponse int responseCode, @Nullable List<Purchase> purchases) {
        if (responseCode == BillingClient.BillingResponse.OK
                && purchases != null) {
            for (Purchase purchase : purchases) {
                enableAd();
            }
        } else if (responseCode == BillingClient.BillingResponse.USER_CANCELED) {
            // Handle an error caused by a user cancelling the purchase flow.
        } else {
            // Handle any other error codes.
        }

    }

    //TODO : Enable Ads
    private void enableAd() {
        try {
            ((BaseActivity) getActivity()).showProgress("");

            RequestParams params = new RequestParams();
            params.put("AuthToken", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.AUTH_TOKEN, ""));
            params.put("id", ((BaseActivity) getActivity()).getPreferences().getString(RequestParamUtils.USER_ID, ""));
            params.put("enableadd", "0");

            Debug.e("enableAd", params.toString());
            AsyncHttpClient asyncHttpClient = AsyncHttpRequest.newRequest();

            ad = true;
            asyncHttpClient.post(new URLS().ENABLED_AD, params, new ResponseHandler(getActivity(), this, "enableAd"));
        } catch (Exception e) {
            Debug.e("enableAd Exception", e.getMessage());
        }

    }

    //TODO : Response of Enable or desable ads
    @Override
    public void onResponse(String response, String methodName) {
        Log.e("response", response);

        ((BaseActivity) getActivity()).dismissProgress();

        if (response == null || response.equals("")) {
            return;
        } else if (methodName.equals("enableAd")) {
            ad = false;
            Log.e(methodName + " Response :-", response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                SharedPreferences.Editor pre = ((BaseActivity) getActivity()).getPreferences().edit();
                pre.putBoolean(RequestParamUtils.ADENABLED, true);
                pre.commit();

                ((HomeActivity)getActivity()).loadFragment(new SwipeViewFragment(), getActivity().getResources().getString(R.string.start_playing));
                ((HomeActivity)getActivity()).initDrawer();

                Toast.makeText(getActivity(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        }
    }
}
