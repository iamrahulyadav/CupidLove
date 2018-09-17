package com.example.cupidlove.helper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.cupidlove.R;

/**
 * Created by Bhumi Shah on 8/8/2017.
 */

public class InstagramDialog extends Dialog {

    //TODO : Variable Declaration
    static final float[] DIMENSIONS_LANDSCAPE = {460, 260};
    static final float[] DIMENSIONS_PORTRAIT = {280, 420};
    static final FrameLayout.LayoutParams FILL = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.FILL_PARENT);
    static final int MARGIN = 4;
    static final int PADDING = 2;
    private String mUrl;
    private OAuthDialogListener mListener;
    private ProgressDialog mSpinner;
    private WebView mWebView;
    private LinearLayout mContent;
    private TextView mTitle;
    private static final String TAG = "Instagram-WebView";

    public InstagramDialog(Context context, String url,
                           OAuthDialogListener listener) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        mUrl = url;
        mListener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setLayout(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mSpinner = new ProgressDialog(getContext());
        mSpinner.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mSpinner.setMessage("Loading...");

//        mContent = new LinearLayout(getContext());
//        mContent.setOrientation(LinearLayout.VERTICAL);
//        setUpTitle();

        setContentView(R.layout.instagram_helper_login_activity);
        mWebView = findViewById(R.id.insta_login_webview);
        setUpWebView();

//        Display display = getWindow().getWindowManager().getDefaultDisplay();
//        final float scale = getContext().getResources().getDisplayMetrics().density;
//        float[] dimensions = (display.getWidth() < display.getHeight()) ? DIMENSIONS_PORTRAIT : DIMENSIONS_LANDSCAPE;
//
//        LayoutInflater inflater = getLayoutInflater();
//        View alertLayout = inflater.inflate(R.layout.instagram_helper_login_activity, null);
//
////        addContentView(mContent, new FrameLayout.LayoutParams( (int) (dimensions[0] * scale + 0.5f), (int) (dimensions[1] * scale + 0.5f)));
//        CookieSyncManager.createInstance(getContext());
//        CookieManager cookieManager = CookieManager.getInstance(); cookieManager.removeAllCookie();
    }

    //TODO : Set Title
    private void setUpTitle() {
        mTitle = new TextView(getContext());
        mTitle.setText("Instagram");
        mTitle.setTextColor(Color.WHITE);
        mTitle.setTypeface(Typeface.DEFAULT_BOLD);
        mTitle.setBackgroundColor(Color.BLACK);
        mTitle.setPadding(MARGIN + PADDING, MARGIN, MARGIN, MARGIN);
        mContent.addView(mTitle);
    }

    //TODO : Set Web View
    private void setUpWebView() {
        //      clearCache();
        CookieManager.getInstance().removeAllCookie();
        WebSettings mWebSettings = mWebView.getSettings();
        mWebSettings.setSavePassword(false);
        mWebSettings.setSaveFormData(false);
        mWebView.clearCache(true);
        mWebView.clearHistory();
        mWebView.setVerticalScrollBarEnabled(false);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setWebViewClient(new OAuthWebViewClient());
        mWebView.getSettings().setJavaScriptEnabled(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setSaveFormData(false);
        webSettings.setSavePassword(false);
        webSettings.setAppCacheEnabled(false);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        mWebView.loadUrl(mUrl);

    }

//    private void setUpWebView()
//    {
////        mWebView = new WebView(getContext());
//        mWebView.setVerticalScrollBarEnabled(false);
//        mWebView.setHorizontalScrollBarEnabled(false);
//        mWebView.setWebViewClient(new OAuthWebViewClient());
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.loadUrl(mUrl);
//    }

    private class OAuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d(TAG, "Redirecting URL " + url);

            if (url.startsWith(InstagramApp.mCallbackUrl)) {
                String urls[] = url.split("=");
                mListener.onComplete(urls[1]);
                InstagramDialog.this.dismiss();
                return true;
            }
            return false;
        }

        //TODO : When Receive Error
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            Log.d(TAG, "Page error: " + description);
            super.onReceivedError(view, errorCode, description, failingUrl);
            mListener.onError(description);
            InstagramDialog.this.dismiss();
        }

        //TODO : Page Started
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            Log.d(TAG, "Loading URL: " + url);
            super.onPageStarted(view, url, favicon);
            mSpinner.show();
        }

        //TODO : Page Finished
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
