package com.example.cupidlove.helper;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.cupidlove.R;

public class InstaLoginActivity extends AppCompatActivity {
    private String mUrl;
    private InstaLoginActivity.OAuthDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private static final String TAG = "Instagram-WebView";
    private Bundle bundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instagram_helper_login_activity);
        bundle = getIntent().getExtras();
        if (bundle != null) {
            mUrl = bundle.getString("mAuthUrl");
        } else {
            finish();
        }
        mSpinner = new ProgressDialog(this);
        mListener = InstagramApp.listener;
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");
        mWebView = findViewById(R.id.insta_login_webview);
        setUpWebView();
    }

    private void setUpWebView() {
        //      clearCache();
        CookieManager.getInstance().removeAllCookie();
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new InstaLoginActivity.OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(mUrl);

    }

    private class OAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(InstagramApp.mCallbackUrl)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                finish();
                return true;
            }
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, "Page error: " + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(description);
            finish();
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
//            if (title != null && title.length() > 0)
//            {
//                mTitle.setText(title);
//            }
            Log.d(TAG, "onPageFinished URL: " + url);
            if (mSpinner.isShowing())
                mSpinner.dismiss();
        }
    }

    public interface OAuthDialogListener {
        public abstract void onComplete(String accessToken);

        public abstract void onError(String error);
    }

}
