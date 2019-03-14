package com.k3.rtlion;

import android.app.Activity;
import android.content.Context;
import android.webkit.WebView;

public class WebInterface {

    private Activity activity;
    private Context context;
    private WebView webView;

    public WebInterface(Activity activity){
        this.activity = activity;
        this.context = activity.getApplicationContext();
    }
    public void initWebView(WebView webView){
        this.webView = webView;
    }
}
